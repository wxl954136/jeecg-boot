package org.jeecg.modules.system.core.entity;

import lombok.Data;

@Data
public class CoreStockBaseDetail {
    private String id;
    private String skuId;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal price;
    private Double rate;  //注意类型错了，就copy不了
    private java.math.BigDecimal sourceAmount;
    private java.math.BigDecimal targetAmount;
    private String gsdm;
    private Integer updateCount;
}
