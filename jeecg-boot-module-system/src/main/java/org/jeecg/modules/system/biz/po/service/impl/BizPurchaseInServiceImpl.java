package org.jeecg.modules.system.biz.po.service.impl;

import org.jeecg.modules.system.biz.po.entity.BizPurchaseIn;
import org.jeecg.modules.system.biz.po.entity.BizPurchaseInDetail;
import org.jeecg.modules.system.biz.po.mapper.BizPurchaseInDetailMapper;
import org.jeecg.modules.system.biz.po.mapper.BizPurchaseInMapper;
import org.jeecg.modules.system.biz.po.service.IBizPurchaseInService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 采购信息主表
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Service
public class BizPurchaseInServiceImpl extends ServiceImpl<BizPurchaseInMapper, BizPurchaseIn> implements IBizPurchaseInService {

	@Autowired
	private BizPurchaseInMapper bizPurchaseInMapper;
	@Autowired
	private BizPurchaseInDetailMapper bizPurchaseInDetailMapper;
	
	@Override
	@Transactional
	public void saveMain(BizPurchaseIn bizPurchaseIn, List<BizPurchaseInDetail> bizPurchaseInDetailList) {
		bizPurchaseInMapper.insert(bizPurchaseIn);
		if(bizPurchaseInDetailList!=null && bizPurchaseInDetailList.size()>0) {
			for(BizPurchaseInDetail entity:bizPurchaseInDetailList) {
				//外键设置
				entity.setHeadId(bizPurchaseIn.getId());
				bizPurchaseInDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(BizPurchaseIn bizPurchaseIn,List<BizPurchaseInDetail> bizPurchaseInDetailList) {
		bizPurchaseInMapper.updateById(bizPurchaseIn);
		
		//1.先删除子表数据
		bizPurchaseInDetailMapper.deleteByMainId(bizPurchaseIn.getId());
		
		//2.子表数据重新插入
		if(bizPurchaseInDetailList!=null && bizPurchaseInDetailList.size()>0) {
			for(BizPurchaseInDetail entity:bizPurchaseInDetailList) {
				//外键设置
				entity.setHeadId(bizPurchaseIn.getId());
				bizPurchaseInDetailMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		bizPurchaseInDetailMapper.deleteByMainId(id);
		bizPurchaseInMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			bizPurchaseInDetailMapper.deleteByMainId(id.toString());
			bizPurchaseInMapper.deleteById(id);
		}
	}
	
}
