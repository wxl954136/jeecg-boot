package org.jeecg.modules.system.biz.ac.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AccPayableSettleBaseDetail{


    private String id;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private String bizNo;

    private Date updateTime;

    private String sysOrgCode;

    private String headId;

    private String traderId;

    private String traderName;
    private java.math.BigDecimal sourceAmount;
    private java.math.BigDecimal targetAmount;
    private java.math.BigDecimal diffAmount;
    private String memo;
    private String bizType;
    private Integer updateCount;
    private BigDecimal qty;
    private BigDecimal price;
    private String delFlag;

    private String gsdm;

    private String payableId;
    private String payableBizNo;







}
