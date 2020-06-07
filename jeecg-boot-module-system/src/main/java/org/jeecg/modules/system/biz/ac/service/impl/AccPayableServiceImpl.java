package org.jeecg.modules.system.biz.ac.service.impl;

import org.jeecg.modules.system.biz.ac.entity.AccPayable;
import org.jeecg.modules.system.biz.ac.entity.AccPayableDetail;
import org.jeecg.modules.system.biz.ac.mapper.AccPayableDetailMapper;
import org.jeecg.modules.system.biz.ac.mapper.AccPayableMapper;
import org.jeecg.modules.system.biz.ac.service.IAccPayableService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 应付款头表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
@Service
public class AccPayableServiceImpl extends ServiceImpl<AccPayableMapper, AccPayable> implements IAccPayableService {

	@Autowired
	private AccPayableMapper accPayableMapper;
	@Autowired
	private AccPayableDetailMapper accPayableDetailMapper;
	
	@Override
	@Transactional
	public void saveMain(AccPayable accPayable, List<AccPayableDetail> accPayableDetailList) {
		accPayableMapper.insert(accPayable);
		if(accPayableDetailList!=null && accPayableDetailList.size()>0) {
			for(AccPayableDetail entity:accPayableDetailList) {
				//外键设置
				entity.setHeadId(accPayable.getId());
				accPayableDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(AccPayable accPayable,List<AccPayableDetail> accPayableDetailList) {
		accPayableMapper.updateById(accPayable);
		
		//1.先删除子表数据
		accPayableDetailMapper.deleteByMainId(accPayable.getId());
		
		//2.子表数据重新插入
		if(accPayableDetailList!=null && accPayableDetailList.size()>0) {
			for(AccPayableDetail entity:accPayableDetailList) {
				//外键设置
				entity.setHeadId(accPayable.getId());
				accPayableDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		accPayableDetailMapper.deleteByMainId(id);
		accPayableMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			accPayableDetailMapper.deleteByMainId(id.toString());
			accPayableMapper.deleteById(id);
		}
	}
	
}
