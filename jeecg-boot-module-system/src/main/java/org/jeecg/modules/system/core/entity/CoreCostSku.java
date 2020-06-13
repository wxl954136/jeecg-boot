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
 * @Description: core_cost_sku
 * @Author: jeecg-boot
 * @Date:   2020-06-12
 * @Version: V1.0
 */
@Data
@TableName("core_cost_sku")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="core_cost_sku对象", description="core_cost_sku")
public class CoreCostSku implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**商品信息*/
	@Excel(name = "商品信息", width = 15)
    @ApiModelProperty(value = "商品信息")
    private String skuId;
	/**成本日期*/
	@Excel(name = "成本日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "成本日期")
    private Date costDate;
	/**月末存放yyyy-mm*/
	@Excel(name = "月末存放yyyy-mm", width = 15)
    @ApiModelProperty(value = "月末存放yyyy-mm")
    private String costMonth;
	/**库存*/
	@Excel(name = "成本", width = 15)
    @ApiModelProperty(value = "成本")
    private BigDecimal costPrice;


	/**公司代码*/
	@Excel(name = "公司代码", width = 15)
    @ApiModelProperty(value = "公司代码")
    private String gsdm;
}
