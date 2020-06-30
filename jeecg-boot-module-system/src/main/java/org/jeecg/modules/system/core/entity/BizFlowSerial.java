package org.jeecg.modules.system.core.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: biz_flow_serial
 * @Author: jeecg-boot
 * @Date:   2020-06-25
 * @Version: V1.0
 */
@Data
@TableName("biz_flow_serial")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="biz_flow_serial对象", description="biz_flow_serial")
public class BizFlowSerial implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**关联采购及销售明细表*/
	@Excel(name = "关联采购及销售明细表", width = 15)
    @ApiModelProperty(value = "关联采购及销售明细表")
    private String headId;
	/**单据类型*/
	@Excel(name = "单据类型", width = 15)
    @ApiModelProperty(value = "单据类型")
    private String bizType;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**串号对应的id*/
	@Excel(name = "串号对应的id", width = 15)
    @ApiModelProperty(value = "串号对应的id")
    private String serialId;
	/**乐观锁*/
	@Excel(name = "乐观锁", width = 15)
    @ApiModelProperty(value = "乐观锁")
    private Integer updateCount;
	/**公司代码*/
	@Excel(name = "公司代码", width = 15)
    @ApiModelProperty(value = "公司代码")
    private String gsdm;
	/**delFlag*/

    @ApiModelProperty(value = "delFlag")
    private String delFlag;
}
