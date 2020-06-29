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
 * @Description: biz_serial
 * @Author: jeecg-boot
 * @Date:   2020-06-25
 * @Version: V1.0
 */
@Data
@TableName("biz_serial")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="biz_serial对象", description="biz_serial")
public class BizSerial implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "id")
    private String id;
	/**如采购入库.其它入库，期初导入等*/
	@Excel(name = "如采购入库.其它入库，期初导入等", width = 15)
    @ApiModelProperty(value = "如采购入库.其它入库，期初导入等")
    private String bizId;
	/**单据类型*/
	@Excel(name = "单据类型", width = 15)
    @ApiModelProperty(value = "单据类型")
    private String bizType;
    /**创建日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date bizDate;
	/**商品型号*/
	@Excel(name = "商品型号", width = 15)
    @ApiModelProperty(value = "商品型号")
    private String skuId;

    @Excel(name = "仓库", width = 15)
    @ApiModelProperty(value = "仓库")
    private String storeId;

	@Excel(name = "串号(1)", width = 15)
    @ApiModelProperty(value = "串号(1)")
    private String serial1;
	/**serial2*/
	@Excel(name = "串号2", width = 15)
    @ApiModelProperty(value = "serial2")
    private String serial2;
	/**serial3*/
	@Excel(name = "串号3", width = 15)
    @ApiModelProperty(value = "serial3")
    private String serial3;
    /**数量*/
    @ApiModelProperty(value = "数量")
    private Integer qty;
	/**乐观锁*/
	@Excel(name = "乐观锁", width = 15)
    @ApiModelProperty(value = "乐观锁")
    private Integer updateCount;
	/**公司代码*/
	@Excel(name = "公司代码", width = 15)
    @ApiModelProperty(value = "公司代码")
    private String gsdm;
}
