package org.jeecg.modules.system.biz.ac.service;

import org.jeecg.modules.system.biz.ac.entity.AccSettleDetail;
import org.jeecg.modules.system.biz.ac.entity.AccSettle;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 收付款结算头表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
public interface IAccSettleService extends IService<AccSettle> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(AccSettle accSettle, List<AccSettleDetail> accSettleDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(AccSettle accSettle, List<AccSettleDetail> accSettleDetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
