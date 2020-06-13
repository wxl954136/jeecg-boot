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
public class CoreBizEntity {
    private String id;
    private String skuId;
    private Date bizDate;
    private Date costDate;
    private BigDecimal qty;
    private BigDecimal price;


}
