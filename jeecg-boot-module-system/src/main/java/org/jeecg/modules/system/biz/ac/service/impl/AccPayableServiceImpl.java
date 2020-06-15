package org.jeecg.modules.system.biz.ac.service.impl;

import org.jeecg.modules.system.biz.ac.entity.AccPayable;
import org.jeecg.modules.system.biz.ac.entity.AccPayableDetail;
import org.jeecg.modules.system.biz.ac.mapper.AccPayableDetailMapper;
import org.jeecg.modules.system.biz.ac.mapper.AccPayableMapper;
import org.jeecg.modules.system.biz.ac.service.IAccPayableService;
import org.jeecg.modules.system.core.utils.CoreUtils;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;
import java.util.Map;

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
		accPayable.setGsdm(SysUtils.getLoginUser().getGsdm());
		accPayable.setUpdateCount(SysUtils.getUpdateCount(0));
		accPayable.setDelFlag("0");
		accPayableMapper.insert(accPayable);
		//保存明细表
		saveUpdateDetail(accPayable,null,accPayableDetailList);

		/*
		if(accPayableDetailList!=null && accPayableDetailList.size()>0) {
			for(AccPayableDetail entity:accPayableDetailList) {
				//外键设置
				entity.setHeadId(accPayable.getId());
				accPayableDetailMapper.insert(entity);
			}
		}
		
		 */
	}

	@Override
	@Transactional
	public void updateMain(AccPayable accPayable,List<AccPayableDetail> accPayableDetailList) {

//		AccPayable oldAccPayable =accPayableMapper.selectById(accPayable.getId());
		accPayable.setGsdm(SysUtils.getLoginUser().getGsdm());
		accPayable.setUpdateCount(SysUtils.getUpdateCount(accPayable.getUpdateCount()));
		accPayable.setDelFlag("0"); //默认不删除
		accPayableMapper.updateById(accPayable);
//取旧入库数量
		List<AccPayableDetail> oldAccPayableDetail = accPayableDetailMapper.selectByMainId(accPayable.getId());
		saveUpdateDetail(accPayable,oldAccPayableDetail,accPayableDetailList);


	}

	@Override
	@Transactional
	public void delMain(String id) {
		List<AccPayableDetail> oldItemList = accPayableDetailMapper.selectByMainId(id);
		AccPayable accPayable = accPayableMapper.selectById(id);
		accPayable.setDelFlag("1");
		accPayableMapper.updateById(accPayable);
		saveUpdateDetail(accPayable,oldItemList,null);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {

		for(Serializable id:idList) {
			delMain(id.toString());
		}
	}

	/**
	 * saveUpdateDetail
	 * @mark ：当是修改数据时，更新明表
	 * @auth 王晓陆
	 * @param accPayableb
	 * @param oldList
	 * @param newList
	 * @return
	 */
	public boolean saveUpdateDetail(AccPayable accPayableb,List<AccPayableDetail> oldList,List<AccPayableDetail> newList)
	{
		boolean result = true;
		try {
			Map<String, List<Object>> mapTransDataList = CoreUtils.getBizAddDeleteUpdateList(
					(List<Object>) (Object) oldList,
					(List<Object>) (Object) newList
			);
			saveUpdateActionData(accPayableb,
					(List<AccPayableDetail>) (Object) mapTransDataList.get("NEW"),
					(List<AccPayableDetail>) (Object) mapTransDataList.get("UPD"),
					(List<AccPayableDetail>) (Object) mapTransDataList.get("DEL")
			);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 *saveUpdateActionData保存数据至数据库明细表
	 * @param accPayableb
	 * @param newTransList
	 * @param updTransList
	 * @param delTransList
	 */
	public void saveUpdateActionData(AccPayable accPayableb,
									 List<AccPayableDetail> newTransList,
									 List<AccPayableDetail> updTransList,
									 List<AccPayableDetail> delTransList)
	{
		if (newTransList != null && newTransList.size() > 0 )
		{
			for(AccPayableDetail item  :  newTransList)
			{
				item.setHeadId(accPayableb.getId());
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				item.setGsdm(accPayableb.getGsdm());
				item.setBizType(accPayableb.getBizType());
				item.setDelFlag("0");

				accPayableDetailMapper.insert(item);
			}
		}

		if (updTransList != null && updTransList.size() > 0 )
		{
			for(AccPayableDetail item  :  updTransList)
			{
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				accPayableDetailMapper.updateById(item);
			}
		}
		if (delTransList != null && delTransList.size() > 0 )
		{
			for(AccPayableDetail item  :  delTransList)
			{
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				item.setDelFlag("1");
				accPayableDetailMapper.updateById(item);
			}
		}
	}
	
}
