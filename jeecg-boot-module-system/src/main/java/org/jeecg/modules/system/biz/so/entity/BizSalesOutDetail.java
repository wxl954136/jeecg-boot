package org.jeecg.modules.system.biz.so.entity;

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
 * @Description: 销售明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-12
 * @Version: V1.0
 */
@ApiModel(value="biz_sales_out对象", description="销售主表")
@Data
@TableName("biz_sales_out_detail")
public class BizSalesOutDetail implements Serializable {
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
	/**头表关联*/
	@ApiModelProperty(value = "头表关联")
	private String headid;
	/**单据类型*/
	@Excel(name = "单据类型", width = 15)
	@ApiModelProperty(value = "单据类型")
	private String bizType;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15, dictTable = "sys_sku", dicText = "full_name", dicCode = "id")
	@Dict(dictTable = "sys_sku", dicText = "full_name", dicCode = "id")
	@ApiModelProperty(value = "商品名称")
	private String skuId;
	/**数量*/
	@Excel(name = "数量", width = 15)
	@ApiModelProperty(value = "数量")
	private java.math.BigDecimal qty;
	/**价格*/
	@Excel(name = "价格", width = 15)
	@ApiModelProperty(value = "价格")
	private java.math.BigDecimal price;
	/**税率*/
	@Excel(name = "税率", width = 15)
	@ApiModelProperty(value = "税率")
	private Double rate;
	/**备注*/
	@Excel(name = "备注", width = 15)
	@ApiModelProperty(value = "备注")
	private String memo;
	/**乐观锁*/
	@Excel(name = "乐观锁", width = 15)
	@ApiModelProperty(value = "乐观锁")
	private Integer updateCount;
	/**公司代码*/
	@Excel(name = "公司代码", width = 15)
	@ApiModelProperty(value = "公司代码")
	private String gsdm;
}
