package org.jeecg.modules.system.biz.so.service;

import org.jeecg.modules.system.biz.so.entity.BizSalesOutDetail;
import org.jeecg.modules.system.biz.so.entity.BizSalesOut;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 销售主表
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
public interface IBizSalesOutService extends IService<BizSalesOut> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(BizSalesOut bizSalesOut, List<BizSalesOutDetail> bizSalesOutDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(BizSalesOut bizSalesOut, List<BizSalesOutDetail> bizSalesOutDetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
