package org.jeecg.modules.system.biz.po.service;

import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Status;
import org.jeecg.modules.system.biz.po.entity.BizPurchaseInDetail;
import org.jeecg.modules.system.biz.po.entity.BizPurchaseIn;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.core.vo.BizFlowSerialVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购信息主表
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface IBizPurchaseInService extends IService<BizPurchaseIn> {

	/**
	 * 添加一对多
	 * 
	 */
	public Status saveMain(BizPurchaseIn bizPurchaseIn, List<BizPurchaseInDetail> bizPurchaseInDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public Status updateMain(BizPurchaseIn bizPurchaseIn, List<BizPurchaseInDetail> bizPurchaseInDetailList);
	
	/**
	 * 删除一对多
	 */
	public Status delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);





}
