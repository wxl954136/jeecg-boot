package org.jeecg.modules.system.biz.common.entity;


import lombok.Data;
import java.util.Date;

@Data

public class BizCommonHead {

    private String id;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String sysOrgCode;

    private String bizNo;

    private String bizType;

    private Date bizDate;

    private String traderId;

    private String storeId;

    private String tradeMethod;

    private String handler;

    private String memo;

    private String delFlag;

    private Integer updateCount;

    private String gsdm;
}
