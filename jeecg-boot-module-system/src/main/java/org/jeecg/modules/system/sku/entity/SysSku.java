package org.jeecg.modules.system.sku.entity;

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
 * @Description: 商品信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Data
@TableName("sys_sku")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_sku对象", description="商品信息")
public class SysSku implements Serializable {
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
	/**商品编码*/
	@Excel(name = "商品编码", width = 15)
    @ApiModelProperty(value = "商品编码")
    private String code;
	/**品牌*/
	@Excel(name = "品牌", width = 15)
    @ApiModelProperty(value = "品牌")
    private String brand;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
    private String name;
	/**颜色*/
	@Excel(name = "颜色", width = 15)
    @ApiModelProperty(value = "颜色")
    private String color;
	/**商品全称*/
	@Excel(name = "商品全称", width = 15)
    @ApiModelProperty(value = "商品全称")
    private String fullName;
	/**拼音*/


	/**商品分类*/
	@Excel(name = "商品分类", width = 15)
    @ApiModelProperty(value = "商品分类")
    private String classifyId;
	/**库龄 */
	@Excel(name = "库龄 ", width = 15)
    @ApiModelProperty(value = "库龄 ")
    private Integer stockAge;
	/**成本方式*/
	@Excel(name = "成本方式", width = 15, dicCode = "sys_cost_type")
	@Dict(dicCode = "sys_cost_type")
    @ApiModelProperty(value = "成本方式")
    private String costFlag;
	/**虚拟商品*/
	@Excel(name = "虚拟商品", width = 15, dicCode = "sys_confirm_status")
	@Dict(dicCode = "sys_confirm_status")
    @ApiModelProperty(value = "虚拟商品")
    private String virFlag;
	/**启用状态*/
	@Excel(name = "启用状态", width = 15, dicCode = "sys_enable_status")
	@Dict(dicCode = "sys_enable_status")
    @ApiModelProperty(value = "启用状态")
    private String enableFlag;
	/**排序*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private String skuSort;
	/**乐观锁*/
	@Excel(name = "乐观锁", width = 15)
    @ApiModelProperty(value = "乐观锁")
    private Integer updateCount;
	/**删除标记*/
	@Excel(name = "删除标记", width = 15)
    @ApiModelProperty(value = "删除标记")
    private String delFlag;
    @ApiModelProperty(value = "公司代码")
    private String gsdm;
}
