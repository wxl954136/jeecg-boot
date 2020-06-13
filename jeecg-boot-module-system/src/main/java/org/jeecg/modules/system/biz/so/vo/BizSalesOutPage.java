package org.jeecg.modules.system.biz.so.vo;

import java.util.List;
import org.jeecg.modules.system.biz.so.entity.BizSalesOut;
import org.jeecg.modules.system.biz.so.entity.BizSalesOutDetail;
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
 * @Description: 销售主表
 * @Author: jeecg-boot
 * @Date:   2020-06-13
 * @Version: V1.0
 */
@Data
@ApiModel(value="biz_sales_outPage对象", description="销售主表")
public class BizSalesOutPage {

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
	@Excel(name = "仓库", width = 15)
	@ApiModelProperty(value = "仓库")
	private String storeId;
	/**客户*/
	@Excel(name = "客户", width = 15)
	@ApiModelProperty(value = "客户")
	private String traderId;
	/**收款方式*/
	@Excel(name = "收款方式", width = 15)
	@ApiModelProperty(value = "收款方式")
	private String tradeMethod;
	/**经手人*/
	@Excel(name = "经手人", width = 15)
	@ApiModelProperty(value = "经手人")
	private String handler;
	/**收款银行*/
	@Excel(name = "收款银行", width = 15)
	@ApiModelProperty(value = "收款银行")
	private String bankId;
	/**删除标记*/
	@Excel(name = "删除标记", width = 15)
	@ApiModelProperty(value = "删除标记")
	private String delFlag;
	/**版本*/
	@Excel(name = "版本", width = 15)
	@ApiModelProperty(value = "版本")
	private Integer updateCount;
	/**公司代码*/
	@Excel(name = "公司代码", width = 15)
	@ApiModelProperty(value = "公司代码")
	private String gsdm;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String memo;
	
	@ExcelCollection(name="销售明细表")
	@ApiModelProperty(value = "销售明细表")
	private List<BizSalesOutDetail> bizSalesOutDetailList;
	
}
