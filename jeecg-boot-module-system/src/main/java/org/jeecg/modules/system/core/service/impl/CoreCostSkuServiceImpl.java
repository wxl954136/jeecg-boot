package org.jeecg.modules.system.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.system.core.entity.CoreBizEntity;
import org.jeecg.modules.system.core.entity.CoreCostSku;
import org.jeecg.modules.system.core.mapper.CoreCostSkuMapper;
import org.jeecg.modules.system.core.service.ICoreCostSkuService;
import org.jeecg.modules.utils.SysStatusEnum;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: core_cost_sku
 * @Author: luke
 * @Date:   2020-06-12
 * @Version: V1.0
 */
@Service
public class CoreCostSkuServiceImpl extends ServiceImpl<CoreCostSkuMapper, CoreCostSku> implements ICoreCostSkuService {

    @Autowired
    private CoreCostSkuMapper coreCostSkuMapper;

    //成本计算不需要和业务放在同一个事务里去，建议和Action放在一起，因为是和业务分开的

    /**
     * 日末加权平均计算方法
     * @param costDate 需要计算成本的开始日期日期
     * @param skuIds 待计算的所有商品
     * @param bizType 当前业务单据类型
     * @param gsdm
     */
    @Override
    @Transactional
    public synchronized void setCalCoreCostSku(Date costDate, List<String> skuIds,String bizType,String gsdm) {
        String sNow = DateUtils.date2Str(new Date(),DateUtils.date_sdf.get());
        String sCostDate = DateUtils.date2Str(costDate,DateUtils.date_sdf.get());
        if(sNow.equalsIgnoreCase(sCostDate))
        {
            //如果不是当天的采购入库单等影响成本的单据，则成本不需要计算
            if (!bizType.equalsIgnoreCase(SysStatusEnum.NOTE_PO_IN.getValue())) {
                return ;
            }
        }
        //获取待计算成本的日期及sku
        //还应该加上传过来的日期，也要算成本，因为第一次时修改至当在可能没成本
        List<CoreBizEntity> coreBizEntityList = coreCostSkuMapper.getAwaitCalCostSkuAndDate(sCostDate,skuIds,gsdm );
        //目的，把业务日期当天的都加进去  ====start
        Map<String, CoreBizEntity> tMap = new TreeMap<>();
        for (String sku : skuIds) {
            String key = sku + "-" + costDate.getTime();
            CoreBizEntity item = new CoreBizEntity();
            item.setCostDate(costDate);
            item.setSkuId(sku);
            tMap.put(key,item);
        }
        for(CoreBizEntity entity: coreBizEntityList){
            tMap.put(entity.getSkuId()+"-" + entity.getCostDate().getTime(),entity);
        }
        List<CoreBizEntity> listSkuAwaitCost = new ArrayList<>();
        Set<String> keySet = tMap.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            CoreBizEntity item = tMap.get(key);
            listSkuAwaitCost.add(item);
        }
        //算之前，先把成本删除
        String rSkuIds = String.format("'%s'", String.join("','", skuIds));
        coreCostSkuMapper.deleteCoreCostSku(sCostDate,rSkuIds,gsdm);
        //三个字段可用 skuId/costDate/price(实际这个成本再重算，没有意义)
        for (CoreBizEntity coreEntity : listSkuAwaitCost) {
            String currentDate = DateUtils.date2Str(coreEntity.getCostDate(),DateUtils.date_sdf.get());
            String currentSku = coreEntity.getSkuId();
            //1.1取昨天的库存结存skuId,qty可用
            CoreBizEntity coreYesterdayEntity = coreCostSkuMapper.getYesterdayStockQty(currentDate,currentSku,gsdm);
            //1.2 昨日库存
            BigDecimal bYesterdayStockQty = new BigDecimal(0);
            if (coreYesterdayEntity != null )  bYesterdayStockQty = coreYesterdayEntity.getQty() ;
            //2.1取昨天的成本 sku_id,cost_date,price
            CoreBizEntity coreYesterdayCostPrice = coreCostSkuMapper.getYesterdayCostPrice(currentDate,currentSku,gsdm);
            BigDecimal bYestodayCostPrice = new BigDecimal(0);
            if (coreYesterdayCostPrice != null ) bYestodayCostPrice = coreYesterdayCostPrice.getPrice();
            //3.1计算当天采购总数量及采购总金额  skuid,qty,price
            CoreBizEntity coreTodayPurchaseTotalQtyAndPrice =  coreCostSkuMapper.getTodayPurchaseTotalQtyAndPrice(currentDate,currentSku,gsdm);
            BigDecimal bTodayPurchaseTotalQty = new BigDecimal(0);
            BigDecimal bTodayPurchaseTotalPrice = new BigDecimal(0);
            if (coreTodayPurchaseTotalQtyAndPrice != null){
                bTodayPurchaseTotalQty = coreTodayPurchaseTotalQtyAndPrice.getQty();
                bTodayPurchaseTotalPrice = coreTodayPurchaseTotalQtyAndPrice.getPrice();
            }
            //4计算当天的成本
            // * (昨库存 * 昨天成本  + 今天采购数量*今天采购金额 )/ （昨天库存 ＋　今天采购）
            Double rCostPrice = (
                    bYesterdayStockQty.doubleValue() * bYestodayCostPrice.doubleValue() +
                            bTodayPurchaseTotalQty.doubleValue() * bTodayPurchaseTotalPrice.doubleValue()
            ) / (bYesterdayStockQty.doubleValue() + bTodayPurchaseTotalQty.doubleValue());

            if (bYesterdayStockQty.doubleValue() + bTodayPurchaseTotalQty.doubleValue() == 0)
            {
                //比如，初次使用该商品，新增采购记录，然后再删除，计算时会有这种问题，或者负库存情况
                continue;
            }
            CoreCostSku costSku = new CoreCostSku();
            costSku.setSkuId(currentSku);
            costSku.setCostDate(DateUtils.str2Date(currentDate,DateUtils.date_sdf.get()));
            costSku.setCostPrice(new BigDecimal(rCostPrice));
            costSku.setGsdm(gsdm);
            coreCostSkuMapper.insert(costSku);
        }
    }

    @Override
    public void setCalCoreCostSkuBasisMonth(Date costDate, List<String> skuIds, String bizType, String gsdm) {

    }
}
