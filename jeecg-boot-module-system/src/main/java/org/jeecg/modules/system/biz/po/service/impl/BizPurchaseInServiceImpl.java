package org.jeecg.modules.system.biz.po.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.lettuce.core.ScriptOutputType;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.modules.system.biz.po.entity.BizPurchaseIn;
import org.jeecg.modules.system.biz.po.entity.BizPurchaseInDetail;
import org.jeecg.modules.system.biz.po.mapper.BizPurchaseInDetailMapper;
import org.jeecg.modules.system.biz.po.mapper.BizPurchaseInMapper;
import org.jeecg.modules.system.biz.po.service.IBizPurchaseInService;
import org.jeecg.modules.system.core.entity.CoreBaseDetail;
import org.jeecg.modules.system.core.entity.CoreBaseHead;
import org.jeecg.modules.system.core.entity.CoreStockSkuVo;
import org.jeecg.modules.system.core.mapper.CoreStockSkuMapper;
import org.jeecg.modules.system.core.service.ICoreStockSkuService;
import org.jeecg.modules.system.core.utils.CoreUtils;
import org.jeecg.modules.utils.SysStatusEnum;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

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
	@Autowired
	private ICoreStockSkuService coreStockSkuService;
	
	@Override
	@Transactional
	public void saveMain(BizPurchaseIn bizPurchaseIn, List<BizPurchaseInDetail> bizPurchaseInDetailList) {
		bizPurchaseIn.setGsdm(SysUtils.getLoginUser().getGsdm());
		bizPurchaseIn.setUpdateCount(SysUtils.getUpdateCount(0));
		bizPurchaseIn.setDelFlag("0"); //默认不删除
		bizPurchaseInMapper.insert(bizPurchaseIn);
		if(bizPurchaseInDetailList!=null && bizPurchaseInDetailList.size()>0) {
			for(BizPurchaseInDetail entity:bizPurchaseInDetailList) {
				//外键设置
				entity.setGsdm(bizPurchaseIn.getGsdm());
				entity.setBizType(bizPurchaseIn.getBizType());
				entity.setDelFlag("0");
				entity.setUpdateCount(0);
				entity.setHeadId(bizPurchaseIn.getId());
				bizPurchaseInDetailMapper.insert(entity);
			}
		}
		List<CoreBaseDetail> newBaseList =  CoreUtils.getCoreBaseDetailList(bizPurchaseInDetailList);
		CoreBaseHead headBase = new CoreBaseHead();
		BeanUtils.copyProperties(bizPurchaseIn,headBase);
		coreStockSkuService.updateCoreStoreSku( CoreUtils.getUpdateStockQtyForCoreStockSku( null, newBaseList, headBase));

	}

	@Override
	@Transactional
	public void updateMain(BizPurchaseIn bizPurchaseIn,List<BizPurchaseInDetail> bizPurchaseInDetailList) {
		bizPurchaseIn.setGsdm(SysUtils.getLoginUser().getGsdm());

		bizPurchaseIn.setUpdateCount(SysUtils.getUpdateCount(bizPurchaseIn.getUpdateCount()));
		bizPurchaseIn.setDelFlag("0"); //默认不删除
		bizPurchaseInMapper.updateById(bizPurchaseIn);

		//取旧入库数量
		List<BizPurchaseInDetail> oldBizPurChaseInDetail = bizPurchaseInDetailMapper.selectByMainId(bizPurchaseIn.getId());

		//1.先删除子表数据
		bizPurchaseInDetailMapper.deleteByMainId(bizPurchaseIn.getId());
		
		//2.子表数据重新插入
		if(bizPurchaseInDetailList!=null && bizPurchaseInDetailList.size()>0) {
			for(BizPurchaseInDetail entity:bizPurchaseInDetailList) {
				//外键设置
				entity.setHeadId(bizPurchaseIn.getId());
				entity.setUpdateCount(SysUtils.getUpdateCount(entity.getUpdateCount()));
				entity.setGsdm(bizPurchaseIn.getGsdm());
				bizPurchaseInDetailMapper.insert(entity);
			}
		}

		//直接Listcopy有问题，取出对象copy
		List<CoreBaseDetail> oldBaseList = CoreUtils.getCoreBaseDetailList(oldBizPurChaseInDetail);
		List<CoreBaseDetail> newBaseList =  CoreUtils.getCoreBaseDetailList(bizPurchaseInDetailList);
		CoreBaseHead headBase = new CoreBaseHead();
		BeanUtils.copyProperties(bizPurchaseIn,headBase);

		coreStockSkuService.updateCoreStoreSku(
				CoreUtils.getUpdateStockQtyForCoreStockSku(
						oldBaseList,
						newBaseList,
						headBase));

	}

	@Override
	@Transactional
	public void delMain(String id) {

		List<CoreBaseDetail> oldBaseList =  CoreUtils.getCoreBaseDetailList(bizPurchaseInDetailMapper.selectByMainId(id));
		CoreBaseHead headBase = new CoreBaseHead();
		BeanUtils.copyProperties(bizPurchaseInMapper.selectById(id),headBase);
		coreStockSkuService.updateCoreStoreSku(
				CoreUtils.getUpdateStockQtyForCoreStockSku(
						oldBaseList,
						null,
						headBase));

		bizPurchaseInDetailMapper.deleteByMainId(id);
		bizPurchaseInMapper.deleteById(id);



	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			delMain(id.toString());  //批处理调用单一的即可
			/*
			bizPurchaseInDetailMapper.deleteByMainId(id.toString());
			bizPurchaseInMapper.deleteById(id);
			 */
		}
	}
	
}
