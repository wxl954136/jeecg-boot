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
 * @Description: acc_trade_amount
 * @Author: jeecg-boot
 * @Date:   2020-06-17
 * @Version: V1.0
 */
@Data
@TableName("acc_trade_amount")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="acc_trade_amount对象", description="acc_trade_amount")
public class AccTradeAmount implements Serializable {
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
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**应收款及应付款业务单据号*/
	@Excel(name = "应收款及应付款业务单据号", width = 15)
    @ApiModelProperty(value = "应收款及应付款业务单据号")
    private String bizId;
	/**单据类型*/
	@Excel(name = "单据类型", width = 15)
    @ApiModelProperty(value = "单据类型")
    private String bizType;
	/**往来单位*/
	@Excel(name = "往来单位", width = 15)
    @ApiModelProperty(value = "往来单位")
    private String traderId;
	/**银行信息(结算时支出或收款银行)*/
	@Excel(name = "银行信息(结算时支出或收款银行)", width = 15)
    @ApiModelProperty(value = "银行信息(结算时支出或收款银行)")
    private String bankId;
	/**应收款及应付款金额*/
	@Excel(name = "应收款及应付款金额", width = 15)
    @ApiModelProperty(value = "应收款及应付款金额")
    private BigDecimal sourceAmount;
	/**目标金额，即结算（如付款和收款）*/
	@Excel(name = "目标金额，即结算（如付款和收款）", width = 15)
    @ApiModelProperty(value = "目标金额，即结算（如付款和收款）")
    private BigDecimal targetAmount;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
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
}
