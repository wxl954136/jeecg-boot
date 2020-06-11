package org.jeecg.modules.system.core.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 辅助类，主要用于计算库存
 */
 @Data
public class CoreStockSkuVo {
    private static final long serialVersionUID = 1L;
    private String storeId;
    private String skuId;

    private java.math.BigDecimal old_qty;
    private java.math.BigDecimal new_qty;
    private java.math.BigDecimal qty;

    public CoreStockSkuVo()
    {

    }
    public CoreStockSkuVo(String storeId, String skuId, BigDecimal old_qty, BigDecimal new_qty) {
        this.storeId = storeId;
        this.skuId = skuId;
        this.old_qty = old_qty;
        this.new_qty = new_qty;
        this.qty = qty;
    }

    public BigDecimal getQty() {
        BigDecimal val = new BigDecimal(this.getNew_qty().doubleValue() - this.getOld_qty().doubleValue());
        return val;
    }


}
