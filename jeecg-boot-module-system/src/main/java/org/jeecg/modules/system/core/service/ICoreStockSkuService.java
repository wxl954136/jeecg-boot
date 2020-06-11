package org.jeecg.modules.system.core.service;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.core.entity.CoreBaseDetail;
import org.jeecg.modules.system.core.entity.CoreBaseHead;
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
     * 公司代码调用的时候统一从Mapper中间给值，获取shiro中的登录用户取得
     * @param skuId  SKUID
     * @param storeId 仓库id
     * @return
     */
    CoreStockSku getCoreStockSkuBySkuAndStore(String skuId,  String storeId);

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
