package org.jeecg.modules.system.biz.po.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.system.core.entity.BizFlowSerial;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import java.util.List;

import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 采购入库明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */

@ApiModel(value="biz_purchase_in对象", description="采购信息主表")
@Data
@TableName("biz_purchase_in_detail")
public class BizPurchaseInDetail  implements Serializable {
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
	/**业务类型*/
	@Excel(name = "业务类型", width = 15)
	@ApiModelProperty(value = "业务类型")
	private String bizType;
	/**所属部门*/
	@ApiModelProperty(value = "所属部门")
	private String sysOrgCode;
	/**头表id*/
	@ApiModelProperty(value = "头表id")
	private String headId;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15, dictTable = "sys_sku", dicText = "full_name", dicCode = "id")
	@Dict(dictTable = "sys_sku", dicText = "full_name", dicCode = "id")
	@ApiModelProperty(value = "商品名称")
	private String skuId;
	/**数量*/
	@Excel(name = "数量", width = 15)
	@ApiModelProperty(value = "数量")
	private java.math.BigDecimal qty;
	/**单价*/
	@Excel(name = "单价", width = 15)
	@ApiModelProperty(value = "单价")
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
	/**删除标记*/
	@Excel(name = "删除标记", width = 15)
	@ApiModelProperty(value = "删除标记")
	private String delFlag;
	/**公司代码*/
	@Excel(name = "公司代码", width = 15)
	@ApiModelProperty(value = "公司代码")
	private String gsdm;


	@Transient
	@TableField(exist = false)
	private List<BizFlowSerial> listBizFlowSerial;
}
