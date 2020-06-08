package org.jeecg.modules.system.biz.ac.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 应付款明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
@ApiModel(value="acc_payable对象", description="应付款头表")
@Data
@TableName("acc_payable_detail")
public class AccPayableDetail implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ID_WORKER_STR)
	@ApiModelProperty(value = "主键")
	private String id;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
	private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
	private Date updateTime;
	/**所属部门*/
	@ApiModelProperty(value = "所属部门")
	private String sysOrgCode;
	/**主表关联*/
	@ApiModelProperty(value = "主表关联")
	private String headId;
	/**往来单位*/
	@Excel(name = "往来单位", width = 15, dictTable = "sys_trader", dicText = "name", dicCode = "id")
	@Dict(dictTable = "sys_trader", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "往来单位")
	private String traderId;
	/**应付款金*/
	@Excel(name = "应付款金", width = 15)
	@ApiModelProperty(value = "应付款金")
	private java.math.BigDecimal sourceAmount;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String memo;
	/**乐观锁*/
	@Excel(name = "乐观锁", width = 15)
	@ApiModelProperty(value = "乐观锁")
	private Integer updateCount;

	@ApiModelProperty(value = "删除标记")
	private String delFlag;
	/**公司代码*/
	@Excel(name = "公司代码", width = 15)
	@ApiModelProperty(value = "公司代码")
	private String gsdm;
}
