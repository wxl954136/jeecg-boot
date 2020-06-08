package org.jeecg.modules.system.bank.entity;

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
 * @Description: 银行信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Data
@TableName("sys_bank")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_bank对象", description="银行信息")
public class SysBank implements Serializable {
    private static final long serialVersionUID = 1L;

    /**主键*/
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**更新日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**银行名称*/
    @Excel(name = "银行名称", width = 15)
    @ApiModelProperty(value = "银行名称")
    private java.lang.String bankName;
    /**开户行*/
    @Excel(name = "开户行", width = 15)
    @ApiModelProperty(value = "开户行")
    private java.lang.String bankAddress;
    /**银行卡号*/
    @Excel(name = "银行卡号", width = 15)
    @ApiModelProperty(value = "银行卡号")
    private java.lang.String bankCardNo;
    /**银行归属人*/
    @Excel(name = "银行归属人", width = 15)
    @ApiModelProperty(value = "银行归属人")
    private java.lang.String bankBelong;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String memo;
    /**删除标记(1:删0:正常*/
    @ApiModelProperty(value = "删除标记(1:删0:正常")
    private java.lang.String delFlag;
    /**排序*/
    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.lang.Integer bankSort;
    /**启用状态*/
    @Excel(name = "启用状态", width = 15, dicCode = "sys_enable_status")
    @Dict(dicCode = "sys_enable_status")
    @ApiModelProperty(value = "启用状态")
    private java.lang.String enableFlag;
    /**乐观锁*/
    @ApiModelProperty(value = "乐观锁")
    private java.lang.Integer updateCount;
    /**
     * 公司代码
     */
    private String gsdm;

}
