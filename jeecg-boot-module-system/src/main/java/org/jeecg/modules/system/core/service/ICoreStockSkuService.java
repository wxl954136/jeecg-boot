package org.jeecg.modules.system.core.service;

import org.jeecg.modules.system.core.entity.CoreStockSku;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.core.entity.CoreStockSkuVo;

import java.util.List;

/**
 * @Description: 核心库存表
 * @Author: jeecg-boot
 * @Date:   2020-06-10
 * @Version: V1.0
 */
public interface ICoreStockSkuService extends IService<CoreStockSku> {

    /**
     *
     * @param entryList 直接传实体即可
     * @return
     */
    void updateCoreStoreSku(List<CoreStockSkuVo> entryList);

    /**
     *
     * @param entity 直接传实体即可
     * @return
     */
    boolean updateCoreStoreSku(CoreStockSku entity);




}
