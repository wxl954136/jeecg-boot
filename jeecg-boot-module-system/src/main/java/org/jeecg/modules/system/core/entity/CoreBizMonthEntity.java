package org.jeecg.modules.system.core.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 此类为了计算成本及库存等，做的公共表，可以在mybaties中以此实体类对象返回
 * 若此对象字段不全部为所需，可以在mybaties.xml中指名字段
 */
//此类为了计算成本及库存等，做的公共表，可以在mybaties中以此实体类对象返回


@Data
public class CoreBizMonthEntity {
    private String id;
    private String skuId;
    private String bizDate; //因为计算月末成本时，SQL中使用了date_format,不认Date类型
    private String costDate;//因为计算月末成本时，SQL中使用了date_format,不认Date类型
    private BigDecimal qty;
    private BigDecimal price;


}
