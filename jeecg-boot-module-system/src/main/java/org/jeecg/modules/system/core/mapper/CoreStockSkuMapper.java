package org.jeecg.modules.system.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.core.entity.CoreStockSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 核心库存表
 * @Author: jeecg-boot
 * @Date:   2020-06-10
 * @Version: V1.0
 */
public interface CoreStockSkuMapper extends BaseMapper<CoreStockSku> {
    /**
     *
     * @param skuId  SKUID
     * @param storeId 仓库id
     * @param gsdm 公司代码
     * @return
     */
    CoreStockSku getCoreStockSkuBySkuAndStore(@Param("skuId") String skuId, @Param("storeId") String storeId, @Param("gsdm") String gsdm);
    int updateCoreStoreSku(@Param("entity") CoreStockSku entity);
}
