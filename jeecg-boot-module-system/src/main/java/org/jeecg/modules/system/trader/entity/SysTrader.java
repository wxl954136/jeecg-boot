package org.jeecg.modules.system.trader.entity;

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
 * @Description: 往来单位
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Data
@TableName("sys_trader")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_trader对象", description="往来单位")
public class SysTrader implements Serializable {
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
	/**名称*/
	@Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;
	/**供应商*/
	@Excel(name = "供应商", width = 15, dicCode = "sys_confirm_status")
    @Dict(dicCode = "sys_confirm_status")
    @ApiModelProperty(value = "供应商")
    private String salerFlag;
	/**客户*/
	@Excel(name = "客户", width = 15, dicCode = "sys_confirm_status")
	@Dict(dicCode = "sys_confirm_status")
    @ApiModelProperty(value = "客户")
    private String buyerFlag;
	/**帐期(天)*/
	@Excel(name = "帐期(天)", width = 15)
    @ApiModelProperty(value = "帐期(天)")
    private Integer period;
	/**信用额度(元)*/
	@Excel(name = "信用额度(元)", width = 15)
    @ApiModelProperty(value = "信用额度(元)")
    private Integer creditAmout;
	/**联系人*/
	@Excel(name = "联系人", width = 15)
    @ApiModelProperty(value = "联系人")
    private String contacter;
	/**电话*/
	@Excel(name = "电话", width = 15)
    @ApiModelProperty(value = "电话")
    private String tel;
	/**地址*/
	@Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
    private String address;
	/**银行名称*/
	@Excel(name = "银行名称", width = 15)
    @ApiModelProperty(value = "银行名称")
    private String bankName;
	/**开户行*/
	@Excel(name = "开户行", width = 15)
    @ApiModelProperty(value = "开户行")
    private String bankAddress;
	/**银行卡号*/
	@Excel(name = "银行卡号", width = 15)
    @ApiModelProperty(value = "银行卡号")
    private String bankCardNo;
	/**银行归属*/
	@Excel(name = "银行归属", width = 15)
    @ApiModelProperty(value = "银行归属")
    private String bankBelong;
	/**启用*/
	@Excel(name = "启用", width = 15, dicCode = "sys_enable_status")
	@Dict(dicCode = "sys_enable_status")
    @ApiModelProperty(value = "启用")
    private String enableFlag;
	/**删除标记（1删，0正常）*/
	@Excel(name = "删除标记（1删，0正常）", width = 15)
    @ApiModelProperty(value = "删除标记（1删，0正常）")
    private String delFlag;
	/**乐观锁*/
	@Excel(name = "乐观锁", width = 15)
    @ApiModelProperty(value = "乐观锁")
    private Integer updateCount;
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String memo;
    /**公司代码*/
    @ApiModelProperty(value = "公司代码")
    private String gsdm;
}
