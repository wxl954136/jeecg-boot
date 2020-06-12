package org.jeecg.modules.system.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.core.entity.CoreStockSku;
import org.jeecg.modules.system.core.entity.CoreStockSkuVo;
import org.jeecg.modules.system.core.mapper.CoreStockSkuMapper;
import org.jeecg.modules.system.core.service.ICoreStockSkuService;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 核心库存表
 * @Author: jeecg-boot
 * @Date:   2020-06-10
 * @Version: V1.0
 */
@Service
public class CoreStockSkuServiceImpl extends ServiceImpl<CoreStockSkuMapper, CoreStockSku> implements ICoreStockSkuService {
    @Autowired
    private CoreStockSkuMapper coreStockSkuMapper;




    /**
     * 更新库存一律通过此通道
     * @param entryList 直接传实体即可
     */
    @Override
    @Transactional
    public synchronized void updateCoreStoreSku(List<CoreStockSkuVo> entryList) {
        for(CoreStockSkuVo item:entryList){
            //获取当前库存数量
            CoreStockSku obj =
                    coreStockSkuMapper.getCoreStockSkuBySkuAndStore(item.getSkuId(), item.getStoreId(), SysUtils.getLoginUser().getGsdm());
            if (obj != null)
            {
                //传过来的值一定是整理之后的需要增加的值，如果是出库动作，一律转换为负值即可
                if (item.getQty().doubleValue() == 0) return ;  //当是0的时候，不需要更新
                double val = obj.getQty().doubleValue()  + item.getQty().doubleValue();
                obj.setQty(new BigDecimal(val));
                obj.setUpdateTime(new Date());
                obj.setUpdateBy(SysUtils.getLoginUser().getUsername());
                obj.setUpdateCount(SysUtils.getUpdateCount(obj.getUpdateCount()));
                updateCoreStoreSku(obj);
            }else
            {
                CoreStockSku entity = new CoreStockSku();
                BeanUtils.copyProperties(item,entity);
                entity.setGsdm(SysUtils.getLoginUser().getGsdm());
                entity.setUpdateCount(SysUtils.getUpdateCount(entity.getUpdateCount()));
                this.save(entity);
            }
        }
    }

    @Override
    public boolean updateCoreStoreSku(CoreStockSku entity) {
        return coreStockSkuMapper.updateCoreStoreSku(entity) > 0;
    }





}
