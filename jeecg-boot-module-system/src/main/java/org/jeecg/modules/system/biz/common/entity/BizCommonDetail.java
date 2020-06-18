package org.jeecg.modules.system.biz.common.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BizCommonDetail   {
    private String id;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String bizType;

    private String sysOrgCode;

    private String headId;

    private String skuId;

    private java.math.BigDecimal qty;

    private java.math.BigDecimal price;

    private Double rate;

    private String memo;

    private Integer updateCount;

    private String delFlag;

    private String gsdm;
}
