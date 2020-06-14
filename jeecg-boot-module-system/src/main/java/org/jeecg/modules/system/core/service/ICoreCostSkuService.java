package org.jeecg.modules.system.core.service;

import org.jeecg.modules.system.core.entity.CoreCostSku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @Description: core_cost_sku
 * @Author: jeecg-boot
 * @Date:   2020-06-12
 * @Version: V1.0
 */
public interface ICoreCostSkuService extends IService<CoreCostSku> {


    /**
     * 零售企业所用
     * 计算成本(日末加权平均) 系统默认为日末加权平均
     * @param costDate 需要计算成本的开始日期日期
     * @param skuIds 待计算的所有商品
     * @param bizType 当前业务单据类型
     */
    void setCalCoreCostSku(Date costDate, List<String> skuIds, String bizType, String gsdm);

    /**
     * 一般企业所用，在某段时间采购价不常变的情况
     * 计算成本(月末加权平均) 系统默认为日末加权平均
     * @param costDate 需要计算成本的开始日期,注意Date需要Format:yyyy-MM的格式
     * @param skuIds 待计算的所有商品
     * @param bizType 当前业务单据类型
     * select sku_id, date_format(cost_date, '%Y-%m') as s from core_cost_sku
     * where date_format(cost_date, '%Y-%m') = '2020-06'
     */
    void setCalCoreCostSkuBasisMonth(Date costDate, List<String> skuIds, String bizType, String gsdm);

}
