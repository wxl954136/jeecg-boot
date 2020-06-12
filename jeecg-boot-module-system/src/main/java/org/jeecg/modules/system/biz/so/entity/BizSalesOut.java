package org.jeecg.modules.system.biz.so.entity;

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
 * @Description: 销售主表
 * @Author: jeecg-boot
 * @Date:   2020-06-12
 * @Version: V1.0
 */
@ApiModel(value="biz_sales_out对象", description="销售主表")
@Data
@TableName("biz_sales_out")
public class BizSalesOut implements Serializable {
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
	/**单据类型*/
	@Excel(name = "单据类型", width = 15)
    @ApiModelProperty(value = "单据类型")
    private String bizType;
	/**单据编号*/
	@Excel(name = "单据编号", width = 15)
    @ApiModelProperty(value = "单据编号")
    private String bizNo;
	/**单据日期*/
	@Excel(name = "单据日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "单据日期")
    private Date bizDate;
	/**仓库*/
	@Excel(name = "仓库", width = 15, dictTable = "sys_store", dicText = "name", dicCode = "id")
    @Dict(dictTable = "sys_store", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "仓库")
    private String storeId;
	/**客户*/
	@Excel(name = "客户", width = 15, dictTable = "sys_trader", dicText = "name", dicCode = "id")
    @Dict(dictTable = "sys_trader", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "客户")
    private String traderId;
	/**收款方式*/
	@Excel(name = "收款方式", width = 15, dicCode = "sys_receivable_type")
    @Dict(dicCode = "sys_receivable_type")
    @ApiModelProperty(value = "收款方式")
    private String tradeMethod;
	/**经手人*/
	@Excel(name = "经手人", width = 15)
    @ApiModelProperty(value = "经手人")
    private String handler;
	/**收款银行*/
	@Excel(name = "收款银行", width = 15, dictTable = "sys_bank", dicText = "bank_name", dicCode = "id")
    @Dict(dictTable = "sys_bank", dicText = "bank_name", dicCode = "id")
    @ApiModelProperty(value = "收款银行")
    private String bankId;
	/**删除标记*/
	@Excel(name = "删除标记", width = 15)
    @ApiModelProperty(value = "删除标记")
    private String delFlag;
	/**版本*/
	@Excel(name = "版本", width = 15)
    @ApiModelProperty(value = "版本")
    private String updateCount;
	/**公司代码*/
	@Excel(name = "公司代码", width = 15)
    @ApiModelProperty(value = "公司代码")
    private String gsdm;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String memo;
}
