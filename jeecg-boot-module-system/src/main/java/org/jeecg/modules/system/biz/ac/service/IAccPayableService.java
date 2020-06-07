package org.jeecg.modules.system.biz.ac.service;

import org.jeecg.modules.system.biz.ac.entity.AccPayableDetail;
import org.jeecg.modules.system.biz.ac.entity.AccPayable;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 应付款头表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
public interface IAccPayableService extends IService<AccPayable> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(AccPayable accPayable, List<AccPayableDetail> accPayableDetailList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(AccPayable accPayable, List<AccPayableDetail> accPayableDetailList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
