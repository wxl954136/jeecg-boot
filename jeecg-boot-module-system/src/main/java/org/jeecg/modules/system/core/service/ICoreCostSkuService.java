package org.jeecg.modules.system.core.service;

import org.jeecg.modules.system.core.entity.CoreCostSku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: core_cost_sku
 * @Author: jeecg-boot
 * @Date:   2020-06-12
 * @Version: V1.0
 */
public interface ICoreCostSkuService extends IService<CoreCostSku> {


    /**
     * 计算成本
     * @param costDate 需要计算成本的开始日期日期
     * @param skuIds 待计算的所有商品
     * @param bizType 当前业务单据类型
     */
    void setCalCoreCostSku(String  costDate, List<String> skuIds,String bizType,String gsdm);

}
