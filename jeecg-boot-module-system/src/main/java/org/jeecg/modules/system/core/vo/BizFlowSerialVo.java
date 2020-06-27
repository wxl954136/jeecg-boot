package org.jeecg.modules.system.core.vo;

import lombok.Data;


@Data
public class BizFlowSerialVo {
    private static final long serialVersionUID = 1L;

    private String id;

    private String headId;

    private String bizType;

    private String serialId;

    private String serial1;
    private String serial2;
    private String serial3;



    private String gsdm;

    private String delFlag;
}
