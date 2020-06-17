package org.jeecg.modules.system.biz.ac.entity;

import java.io.Serializable;
import java.util.Date;
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

/**
 * @Description: 收付款结算头表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
@ApiModel(value="acc_settle对象", description="收付款结算头表")
@Data
@TableName("acc_settle")
public class AccSettle implements Serializable {
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
	/**单据号*/
	@Excel(name = "单据号", width = 15)
    @ApiModelProperty(value = "单据号")
    private String bizNo;
	/**单据日期*/
	@Excel(name = "单据日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "单据日期")
    private Date bizDate;
	/**科目*/
	@Excel(name = "科目", width = 15)
    @ApiModelProperty(value = "科目")
    private String subjectsId;
	/**银行帐户*/
	@Excel(name = "银行帐户", width = 15, dictTable = "sys_bank", dicText = "bank_name", dicCode = "id")
    @Dict(dictTable = "sys_bank", dicText = "bank_name", dicCode = "id")
    @ApiModelProperty(value = "银行帐户")
    private String bankId;
	/**经手人*/
	@Excel(name = "经手人", width = 15)
    @ApiModelProperty(value = "经手人")
    private String handler;
	/**删除标记*/
	@Excel(name = "删除标记", width = 15)
    @ApiModelProperty(value = "删除标记")
    private String delFlag;
	/**业务单类型*/
	@Excel(name = "业务单类型", width = 15)
    @ApiModelProperty(value = "业务单类型")
    private String bizType;
	/**更新版本号*/
	@Excel(name = "更新版本号", width = 15)
    @ApiModelProperty(value = "更新版本号")
    private Integer updateCount;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String memo;

    /**单据来源(SYS:AUTO)*/
    @Excel(name = "单据来源(SYS:AUTO)", width = 15)
    @ApiModelProperty(value = "单据来源(SYS:AUTO)")
    private String noteSource;
    @ApiModelProperty(value = "公司代码")
    private String gsdm;
}
