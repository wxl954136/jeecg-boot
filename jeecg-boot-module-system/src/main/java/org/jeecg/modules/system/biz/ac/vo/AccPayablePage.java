package org.jeecg.modules.system.biz.ac.vo;

import java.util.List;
import org.jeecg.modules.system.biz.ac.entity.AccPayable;
import org.jeecg.modules.system.biz.ac.entity.AccPayableDetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 应付款头表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
@Data
@ApiModel(value="acc_payablePage对象", description="应付款头表")
public class AccPayablePage {

	/**主键*/
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
	/**单据号*/
	@Excel(name = "单据号", width = 15)
	@ApiModelProperty(value = "单据号")
	private String bizNo;
	/**科目*/
	@Excel(name = "科目", width = 15)
	@ApiModelProperty(value = "科目")
	private String subjectsId;
	/**单据日期*/
	@Excel(name = "单据日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "单据日期")
	private Date bizDate;
	/**单据类型*/
	@Excel(name = "单据类型", width = 15)
	@ApiModelProperty(value = "单据类型")
	private String bizType;
	/**经手人*/
	@Excel(name = "经手人", width = 15)
	@ApiModelProperty(value = "经手人")
	private String handler;
	/**删除标记*/
	@Excel(name = "删除标记", width = 15)
	@ApiModelProperty(value = "删除标记")
	private String delFlag;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String memo;
	/**乐观锁*/
	@Excel(name = "乐观锁", width = 15)
	@ApiModelProperty(value = "乐观锁")
	private Integer updateCount;
	/**来源类型*/
	@Excel(name = "来源类型", width = 15)
	@ApiModelProperty(value = "来源类型")
	private String fromBizType;
	/**业务单来源单据号*/
	@Excel(name = "业务单来源单据号", width = 15)
	@ApiModelProperty(value = "业务单来源单据号")
	private String fromBizId;
	/**公司代码*/
	@Excel(name = "公司代码", width = 15)
	@ApiModelProperty(value = "公司代码")
	private String gsdm;
	/**单据来源(SYS:AUTO)*/
	@Excel(name = "单据来源(SYS:AUTO)", width = 15)
	@ApiModelProperty(value = "单据来源(SYS:AUTO)")
	private String noteSource;
	
	@ExcelCollection(name="应付款明细表")
	@ApiModelProperty(value = "应付款明细表")
	private List<AccPayableDetail> accPayableDetailList;
	
}
