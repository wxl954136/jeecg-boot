package org.jeecg.modules.system.core.vo;

import lombok.Data;

import java.util.Date;

/**
 * 此类为BizFlowSerial及BizSerial的综合类
 */
@Data
public class BizFlowSerialVo {
    private static final long serialVersionUID = 1L;

    private String id;

    private String headId;
    private String bizId;

    private String bizType;
    private Date bizDate;

    private String skuId;

    private String storeId;
    private String serialId;
    private String serial;
    private String serial1;
    private String serial2;
    private String serial3;
    private Integer qty;
    private String gsdm;
    private String delFlag;
}
