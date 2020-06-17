package org.jeecg.modules.system.biz.ac.service.impl;

import org.jeecg.modules.system.biz.ac.entity.AccSettle;
import org.jeecg.modules.system.biz.ac.entity.AccSettleDetail;
import org.jeecg.modules.system.biz.ac.mapper.AccSettleDetailMapper;
import org.jeecg.modules.system.biz.ac.mapper.AccSettleMapper;
import org.jeecg.modules.system.biz.ac.service.IAccSettleService;
import org.jeecg.modules.system.core.utils.CoreUtils;
import org.jeecg.modules.utils.SysStatusEnum;
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
		accSettle.setGsdm(SysUtils.getLoginUser().getGsdm());
		accSettle.setUpdateCount(SysUtils.getUpdateCount(0));
		accSettle.setDelFlag("0");
		accSettle.setNoteSource(SysStatusEnum.NOTE_SOURCE_SYS.getValue());
		accSettleMapper.insert(accSettle);
		//保存明细表
		saveUpdateDetail(accSettle,null,accSettleDetailList);
	}

	@Override
	@Transactional
	public void updateMain(AccSettle accSettle,List<AccSettleDetail> accSettleDetailList) {
		accSettle.setGsdm(SysUtils.getLoginUser().getGsdm());
		accSettle.setUpdateCount(SysUtils.getUpdateCount(accSettle.getUpdateCount()));
		accSettle.setDelFlag("0"); //默认不删除
		accSettleMapper.updateById(accSettle);
		List<AccSettleDetail> oldAccSettleDetailList = accSettleDetailMapper.selectByMainId(accSettle.getId());
		saveUpdateDetail(accSettle,oldAccSettleDetailList,accSettleDetailList);
	}

	@Override
	@Transactional
	public void delMain(String id) {
		List<AccSettleDetail> oldItemList = accSettleDetailMapper.selectByMainId(id);
		AccSettle accSettle = accSettleMapper.selectById(id);
		accSettle.setDelFlag("1");
		accSettleMapper.updateById(accSettle);
		saveUpdateDetail(accSettle,oldItemList,null);
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
	 * @param accSettle
	 * @param oldList
	 * @param newList
	 * @return
	 */
	public boolean saveUpdateDetail(AccSettle accSettle,List<AccSettleDetail> oldList,List<AccSettleDetail> newList)
	{
		boolean result = true;
		try {
			Map<String, List<Object>> mapTransDataList = CoreUtils.getBizAddDeleteUpdateList(
					(List<Object>) (Object) oldList,
					(List<Object>) (Object) newList
			);
			saveUpdateActionData(accSettle,
					(List<AccSettleDetail>) (Object) mapTransDataList.get("NEW"),
					(List<AccSettleDetail>) (Object) mapTransDataList.get("UPD"),
					(List<AccSettleDetail>) (Object) mapTransDataList.get("DEL")
			);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	/**
	 *saveUpdateActionData保存数据至数据库明细表
	 * @param accSettle
	 * @param newTransList
	 * @param updTransList
	 * @param delTransList
	 */
	public void saveUpdateActionData(AccSettle accSettle,
									 List<AccSettleDetail> newTransList,
									 List<AccSettleDetail> updTransList,
									 List<AccSettleDetail> delTransList)
	{
		if (newTransList != null && newTransList.size() > 0 )
		{
			for(AccSettleDetail item  :  newTransList)
			{
				item.setHeadId(accSettle.getId());
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				item.setGsdm(accSettle.getGsdm());
				item.setBizType(accSettle.getBizType());
				item.setDelFlag("0");
				accSettleDetailMapper.insert(item);
			}
		}

		if (updTransList != null && updTransList.size() > 0 )
		{
			for(AccSettleDetail item  :  updTransList)
			{
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				accSettleDetailMapper.updateById(item);
			}
		}
		if (delTransList != null && delTransList.size() > 0 )
		{
			for(AccSettleDetail item  :  delTransList)
			{
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				item.setDelFlag("1");
				accSettleDetailMapper.updateById(item);
			}
		}
	}
}
