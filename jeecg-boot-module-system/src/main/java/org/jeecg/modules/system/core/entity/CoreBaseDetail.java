package org.jeecg.modules.system.core.entity;

import lombok.Data;

@Data
public class CoreBaseDetail {
    private String id;
    private String skuId;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal price;
    private java.math.BigDecimal sourceAmount;
    private java.math.BigDecimal targetAmount;
    private String gsdm;
    private Integer updateCount;
}
