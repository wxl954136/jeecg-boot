package org.jeecg.modules.system.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.core.entity.BizFlowSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: biz_flow_sku
 * @Author: jeecg-boot
 * @Date:   2020-06-12
 * @Version: V1.0
 */
public interface BizFlowSkuMapper extends BaseMapper<BizFlowSku> {

    /**
     *
     *


     -- 获取需要计算成本的天数list<CoreCostSku></CoreCostSku>
     select sku_id,cost_date from core_cost_sku
     where 1=1
     and sku_id = '需要打定条件，建议使用in,因为同一单商品会有多个'
     and gsdm = '10000'
     and cost_date >= '2020-06-01'
     group by sku_id,cost_date
     order by cost_date
     //循环找出对应商品的成


     -- 取每个商品的与最近的单据日期的成本,,如果取不到则为0
     select
     from core_sku
     -- 1获取当前需要计算成本的日期

     select biz_date,sku_id from biz_flow_sku
     where 1=1
     and sku_id in('1271420557538361346')
     and gsdm = '10000'
     and del_flag = '0'
     and biz_date >= '2020-06-01'
     group by sku_id,biz_date
     order by biz_date

     -- 2.获取截止到每天的库存结存
     select sku_id,sum(qty) from biz_flow_sku
     where 1=1
     and  sku_id = ''
     and biz_date in( '2020-06-01','----')
     and gsdm = '10000'
     and del_flag = '0'
     group by sku_id

    -- 3获取每天的采购数量
     select biz_date,sku_id,sum(qty) as 每天采购数量,sum(qty*price) 每天采购总金额 from biz_flow_sku
     where 1=1
     and  sku_id in ('第一步的商品id')
     and biz_date in( '2020-06-01','----') //第一步的所有日期
     and gsdm = '10000'
     and del_flag = '0'
     and bz_type in('CGRK')
     group by sku_id

     ----4
     （昨日库存结存 * 昨日成本 + 今日采购总金额）/（昨日库存+ 今日采购）




     */
}
