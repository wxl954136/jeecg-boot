package org.jeecg.modules.system.biz.ac.service.impl;

import org.jeecg.modules.system.biz.ac.entity.AccSettle;
import org.jeecg.modules.system.biz.ac.entity.AccSettleDetail;
import org.jeecg.modules.system.biz.ac.mapper.AccSettleDetailMapper;
import org.jeecg.modules.system.biz.ac.mapper.AccSettleMapper;
import org.jeecg.modules.system.biz.ac.service.IAccSettleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 收付款结算头表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
@Service
public class AccSettleServiceImpl extends ServiceImpl<AccSettleMapper, AccSettle> implements IAccSettleService {

	@Autowired
	private AccSettleMapper accSettleMapper;
	@Autowired
	private AccSettleDetailMapper accSettleDetailMapper;
	
	@Override
	@Transactional
	public void saveMain(AccSettle accSettle, List<AccSettleDetail> accSettleDetailList) {
		accSettleMapper.insert(accSettle);
		if(accSettleDetailList!=null && accSettleDetailList.size()>0) {
			for(AccSettleDetail entity:accSettleDetailList) {
				//外键设置
				entity.setHeadId(accSettle.getId());
				accSettleDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(AccSettle accSettle,List<AccSettleDetail> accSettleDetailList) {
		accSettleMapper.updateById(accSettle);
		
		//1.先删除子表数据
		accSettleDetailMapper.deleteByMainId(accSettle.getId());
		
		//2.子表数据重新插入
		if(accSettleDetailList!=null && accSettleDetailList.size()>0) {
			for(AccSettleDetail entity:accSettleDetailList) {
				//外键设置
				entity.setHeadId(accSettle.getId());
				accSettleDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		accSettleDetailMapper.deleteByMainId(id);
		accSettleMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			accSettleDetailMapper.deleteByMainId(id.toString());
			accSettleMapper.deleteById(id);
		}
	}
	
}
