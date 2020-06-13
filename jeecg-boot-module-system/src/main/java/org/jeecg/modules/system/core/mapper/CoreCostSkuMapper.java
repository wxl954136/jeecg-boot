package org.jeecg.modules.system.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.core.entity.CoreBizEntity;
import org.jeecg.modules.system.core.entity.CoreCostSku;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description: core_cost_sku
 * @Author: jeecg-boot
 * @Date:   2020-06-12
 * @Version: V1.0
 */
public interface CoreCostSkuMapper extends BaseMapper<CoreCostSku> {

    //CoreBizEntity统一使用此辅助类
    /**
     * 获取指定商品所有待计算成本的日期 ，一次性可以取N个商品
     * @param costDate
     * @param skuIds
     * @param gsdm
     * @return
     */
    //String ids = String.format("'%s'", String.join("','", userIds));
    //		// 1. 删除用户
    //		int line = userMapper.deleteLogicDeleted(ids);
    List<CoreBizEntity> getAwaitCalCostSkuAndDate(@Param("costDate")String costDate, @Param("skuIds")List<String> skuIds, @Param("gsdm")String gsdm);

    /**
     *取昨天的库存结存
     * @param bizDate
     * @param skuId
     * @param gsdm
     * @return
     */
    CoreBizEntity getYesterdayStockQty(@Param("bizDate")String bizDate,@Param("skuId")String skuId,@Param("gsdm")String gsdm);

    /**
     * 取昨天 的成本
     * @param costDate
     * @param skuId
     * @param gsdm
     * @return
     */

    CoreBizEntity getYesterdayCostPrice(@Param("costDate")String costDate,@Param("skuId")String skuId,@Param("gsdm")String gsdm);

    /**
     * 计算当天采购总数量及采购总金额
     * @param bizDate
     * @param skuId
     * @param gsdm
     * @return
     */
    CoreBizEntity getTodayPurchaseTotalQtyAndPrice(@Param("bizDate")String bizDate, @Param("skuId")String skuId,@Param("gsdm") String gsdm);

    /**
     * 彻底删除待被重算成本的成本记录
     */
    int deleteCoreCostSku(@Param("costDate") String costDate,@Param("skuIds") String skuIds,@Param("gsdm") String gsdm);
    /**
     * (昨库存 * 昨天成本  + 今天采购数量*今天采购金额 )/ （昨天库存 ＋　今天采购）
     */

}
