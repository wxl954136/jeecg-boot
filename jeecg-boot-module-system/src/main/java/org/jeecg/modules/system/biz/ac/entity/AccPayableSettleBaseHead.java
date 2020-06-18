package org.jeecg.modules.system.biz.ac.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AccPayableSettleBaseHead {
    private static final long serialVersionUID = 1L;

    private String id;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String tradeMethod;

    private String sysOrgCode;

    private String bizNo;

    private String subjectsId;

    private Date bizDate;

    private String bizType;

    private String handler;

    private String delFlag;

    private String memo;

    private Integer updateCount;

    private String fromBizType;

    private String fromBizId;
    private String bankId;
    private String noteSource;
    private String gsdm;
}
