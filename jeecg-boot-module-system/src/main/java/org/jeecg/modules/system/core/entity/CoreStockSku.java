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
 * @Description: 核心库存表
 * @Author: jeecg-boot
 * @Date:   2020-06-10
 * @Version: V1.0
 */
@Data
@TableName("core_stock_sku")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="core_stock_sku对象", description="核心库存表")
public class CoreStockSku implements Serializable {
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
	/**批次id*/
	@Excel(name = "批次id", width = 15)
    @ApiModelProperty(value = "批次id")
    private String batchId;
	/**商品id*/
	@Excel(name = "商品id", width = 15)
    @ApiModelProperty(value = "商品id")
    private String skuId;
	/**仓库id*/
	@Excel(name = "仓库id", width = 15)
    @ApiModelProperty(value = "仓库id")
    private String storeId;
	/**库存数量*/
	@Excel(name = "库存数量", width = 15)
    @ApiModelProperty(value = "库存数量")
    private java.math.BigDecimal qty;



    @ApiModelProperty(value = "乐观锁")
    private Integer updateCount;
	/**公司代码*/
	@Excel(name = "公司代码", width = 15)
    @ApiModelProperty(value = "公司代码")
    private String gsdm;
}
