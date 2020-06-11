package org.jeecg.modules.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Title: SysNoteBizNoVo
 * @Description: 重复校验VO
 * @Author 悠蓝王晓陆
 * @Date 2019-03-25
 * @Version V1.0
 */
@Data
@ApiModel(value="获取业务单据号对象",description="根据最大单生成新的业务单据号")
public class SysNoteBizNoVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 表名
	 */
	@ApiModelProperty(value="表名",name="tableName",example="biz_purchase_in")
	private String tableName;

 /*
	@ApiModelProperty(value="字段名",name="fieldName",example="id")
	private String fieldName;

	@ApiModelProperty(value="字段值",name="fieldVal",example="1000")
	private String fieldVal;
*/


	@ApiModelProperty(value="公司代码字段名称",name="fieldGsdm",example="1000")
	private String fieldGsdm;

	@ApiModelProperty(value="公司代码结果值",name="fieldGsdmVal",example="1000")
	private String fieldGsdmVal;

	@ApiModelProperty(value="业务单据类型",name="fieldBizType",example="CGRK")
	private String fieldBizType;

	@ApiModelProperty(value="业务单据类型值",name="fieldBizTypeVal",example="CGRK")
	private String fieldBizTypeVal;

	//0:未删除  1:删除
	@ApiModelProperty(value="删除标记",name="fieldBizType",example="delFlag")
	private String fieldDelFlag;

	@ApiModelProperty(value="删除标记值",name="fieldBizTypeVal",example="delFlag")
	private String fieldDelFlagVal;

	/**
	 * 数据ID
	*/
	@ApiModelProperty(value="数据ID",name="dataId",example="2000")
	private String dataId;

}