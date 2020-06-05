package org.jeecg.modules.system.biz.so.service.impl;

import org.jeecg.modules.system.biz.so.entity.BizSalesOut;
import org.jeecg.modules.system.biz.so.entity.BizSalesOutDetail;
import org.jeecg.modules.system.biz.so.mapper.BizSalesOutDetailMapper;
import org.jeecg.modules.system.biz.so.mapper.BizSalesOutMapper;
import org.jeecg.modules.system.biz.so.service.IBizSalesOutService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 销售主表
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
@Service
public class BizSalesOutServiceImpl extends ServiceImpl<BizSalesOutMapper, BizSalesOut> implements IBizSalesOutService {

	@Autowired
	private BizSalesOutMapper bizSalesOutMapper;
	@Autowired
	private BizSalesOutDetailMapper bizSalesOutDetailMapper;
	
	@Override
	@Transactional
	public void saveMain(BizSalesOut bizSalesOut, List<BizSalesOutDetail> bizSalesOutDetailList) {
		bizSalesOutMapper.insert(bizSalesOut);
		if(bizSalesOutDetailList!=null && bizSalesOutDetailList.size()>0) {
			for(BizSalesOutDetail entity:bizSalesOutDetailList) {
				//外键设置
				entity.setHeadid(bizSalesOut.getId());
				bizSalesOutDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(BizSalesOut bizSalesOut,List<BizSalesOutDetail> bizSalesOutDetailList) {
		bizSalesOutMapper.updateById(bizSalesOut);
		
		//1.先删除子表数据
		bizSalesOutDetailMapper.deleteByMainId(bizSalesOut.getId());
		
		//2.子表数据重新插入
		if(bizSalesOutDetailList!=null && bizSalesOutDetailList.size()>0) {
			for(BizSalesOutDetail entity:bizSalesOutDetailList) {
				//外键设置
				entity.setHeadid(bizSalesOut.getId());
				bizSalesOutDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		bizSalesOutDetailMapper.deleteByMainId(id);
		bizSalesOutMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			bizSalesOutDetailMapper.deleteByMainId(id.toString());
			bizSalesOutMapper.deleteById(id);
		}
	}
	
}
