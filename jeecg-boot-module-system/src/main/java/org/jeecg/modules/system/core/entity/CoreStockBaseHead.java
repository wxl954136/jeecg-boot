package org.jeecg.modules.system.core.entity;

import lombok.Data;

@Data
public class CoreStockBaseHead {
    private String id;
    private String bizType;
    private String storeId;
    private String traderId;
    private String gsdm;
    private Integer updateCount;
}
