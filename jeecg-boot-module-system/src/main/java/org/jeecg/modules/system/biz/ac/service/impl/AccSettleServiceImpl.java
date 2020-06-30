package org.jeecg.modules.system.biz.ac.service.impl;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.bank.entity.SysBank;
import org.jeecg.modules.system.bank.mapper.SysBankMapper;
import org.jeecg.modules.system.biz.ac.entity.AccPayable;
import org.jeecg.modules.system.biz.ac.entity.AccPayableDetail;
import org.jeecg.modules.system.biz.ac.entity.AccSettle;
import org.jeecg.modules.system.biz.ac.entity.AccSettleDetail;
import org.jeecg.modules.system.biz.ac.mapper.AccPayableDetailMapper;
import org.jeecg.modules.system.biz.ac.mapper.AccPayableMapper;
import org.jeecg.modules.system.biz.ac.mapper.AccSettleDetailMapper;
import org.jeecg.modules.system.biz.ac.mapper.AccSettleMapper;
import org.jeecg.modules.system.biz.ac.service.IAccSettleService;
import org.jeecg.modules.system.biz.common.entity.BizCommonDetail;
import org.jeecg.modules.system.biz.common.entity.BizCommonHead;
import org.jeecg.modules.system.core.utils.CoreUtils;
import org.jeecg.modules.system.mapper.SysCommonMapper;
import org.jeecg.modules.utils.SysStatusEnum;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
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
	private AccPayableMapper accPayableMapper;
	@Autowired
	private AccSettleMapper accSettleMapper;
	@Autowired
	private AccSettleDetailMapper accSettleDetailMapper;
	@Autowired
	private AccPayableDetailMapper accPayableDetailMapper;

	@Autowired
	private SysCommonMapper sysCommonMapper;
	@Autowired
	private SysBankMapper sysBankMapper;

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





	/**
	 * 当做业务单，如采购和销售时，同时产生应收付款和收付款
	 * @param oldHead
	 * @param newHead
	 * @param newList
	 */
	@Override
	public void savePayableAndSettle(BizCommonHead oldHead, BizCommonHead newHead, List<BizCommonDetail> newList) {
		Result<?> result = new Result<Object>();

		if (oldHead != null){
			//如果原来是现金产生的，则需要删除
			if(oldHead.getTradeMethod().equalsIgnoreCase(SysStatusEnum.SYS_PAYABLE_CASH.getValue())){
				//删除应付款
				AccPayable accPayable = accPayableMapper.selectByFromBizId(oldHead.getId(),oldHead.getGsdm());
				accPayableDetailMapper.deleteByMainId(accPayable.getId());
				accPayableMapper.deleteByFromBizId(oldHead.getId(),oldHead.getGsdm());
				//删除结算单
				List<AccSettleDetail> accSettleDetailList = accSettleDetailMapper.selectByPayableId(accPayable.getId(),oldHead.getGsdm());
				//现金时一个明细只有一条记录，可以一次性删除
				for(AccSettleDetail settleDetail : accSettleDetailList){
					//通过子表删父表
					accSettleMapper.deleteById(settleDetail.getHeadId());
				}
				for(AccSettleDetail settleDetail:accSettleDetailList){
					accSettleDetailMapper.deleteById(settleDetail.getId());
				}
			}else  //如果是应付款或者应收款，则如果有结算，不允许删除
			{
				AccPayable accPayable = accPayableMapper.selectByFromBizId(oldHead.getId(),oldHead.getGsdm());
				List<AccSettleDetail> accSettleDetailList = accSettleDetailMapper.selectByPayableId(accPayable.getId(),oldHead.getGsdm());

				if (accSettleDetailList !=null && accSettleDetailList.size() > 0){
					result.error500("该单据已经产生结算单，请联系相关人员删除结算单再修改");
					return ;
				}
				//删除结算单 好像这一步是无用的呢
				if (accSettleDetailList !=null && accSettleDetailList.size() == 0 ){
					//因为结算单是一对多，因此删除对头表时，只有一条记录方可以删除
					//accSettleMapper.deleteById(accSettleDetailList.get(0).getHeadId());
				}
			}
		}
		//修改和新增时，需要插入数据
		if (newHead != null)
		{
			String biz_type = newHead.getBizType(); //单据类型
			String trade_method = newHead.getTradeMethod(); //交易方式
			AccPayable accPayable = new AccPayable();
			accPayable.setNoteSource("AUTO");
			accPayable.setHandler(SysUtils.getLoginUser().getRealname());
			accPayable.setGsdm(newHead.getGsdm());
			accPayable.setBizDate(newHead.getBizDate());
			accPayable.setDelFlag("0");
			accPayable.setSubjectsId("暂未定［待科目确定］一定要给个默认值");
			accPayable.setUpdateCount(SysUtils.getUpdateCount(0));
			accPayable.setFromBizId(newHead.getId());
			accPayable.setFromBizType(newHead.getBizType());
			accPayable.setMemo("自动产生->单据来源:" + newHead.getBizNo());
			//如果是采购退货及零售则产生应收款

			//如果是采购/零售退货产生应付款
			if (biz_type.equalsIgnoreCase(SysStatusEnum.NOTE_PO_IN.getValue()) ||
					biz_type.equalsIgnoreCase(SysStatusEnum.NOTE_SALES_BACK.getValue())
			) {
				accPayable.setBizType(SysStatusEnum.NOTE_PAYABLE.getValue());
			}
			if (biz_type.equalsIgnoreCase(SysStatusEnum.NOTE_PO_BACK.getValue()) ||
					biz_type.equalsIgnoreCase(SysStatusEnum.NOTE_SALES_OUT.getValue())
			) {
				accPayable.setBizType(SysStatusEnum.NOTE_RECEIVABLE.getValue());
			}
			String newBizNo = SysUtils.getNewNoteNo(sysCommonMapper, "acc_payable", accPayable.getBizType().toUpperCase());
			accPayable.setBizNo(newBizNo);
			accPayableMapper.insert(accPayable);
			BigDecimal totalSourceAmount = new BigDecimal(0);
			for(BizCommonDetail entity:newList){
				totalSourceAmount = totalSourceAmount.add(entity.getQty().multiply(entity.getPrice()));
			}
			AccPayableDetail accPayableDetail = new AccPayableDetail();
			accPayableDetail.setHeadId(accPayable.getId());
			accPayableDetail.setBizType(accPayable.getBizType());
			accPayableDetail.setSourceAmount(totalSourceAmount);
			accPayableDetail.setTraderId(newHead.getTraderId());
			accPayableDetail.setUpdateCount(SysUtils.getUpdateCount(0));
			accPayableDetail.setDelFlag("0");
			accPayableDetail.setGsdm(newHead.getGsdm());
			accPayableDetail.setMemo("自动产生->来源" + newHead.getBizNo());
			accPayableDetailMapper.insert(accPayableDetail);
			//如果是现金，则要收成应收付款，同时生成结算单据
			if (trade_method.equalsIgnoreCase(SysStatusEnum.SYS_PAYABLE_CASH.getValue()) ||
					trade_method.equalsIgnoreCase(SysStatusEnum.SYS_RECEIVABLE_CASH.getValue())
			) {
				AccSettle accSettle = new AccSettle();
				accSettle.setNoteSource("AUTO");
				List<SysBank> sysBanks = sysBankMapper.selectByName("现金",newHead.getGsdm());
				if (sysBanks !=null)
				{
					accSettle.setBankId(sysBanks.get(0).getId());
				}
				accSettle.setGsdm(newHead.getGsdm());
				accSettle.setDelFlag("0");
				accSettle.setBizDate(newHead.getBizDate());
				accSettle.setSubjectsId("暂［待科目确定］定一定要给个默认值");
				accSettle.setUpdateCount(SysUtils.getUpdateCount(0));
				accSettle.setHandler(SysUtils.getLoginUser().getRealname());
				accSettle.setMemo("自动产生->单据来源:" + newHead.getBizNo() + "-" + accPayable.getBizNo());
				if (biz_type.equalsIgnoreCase(SysStatusEnum.NOTE_PO_IN.getValue()) ||
						biz_type.equalsIgnoreCase(SysStatusEnum.NOTE_SALES_BACK.getValue())
				) {
					accSettle.setBizType(SysStatusEnum.NOTE_SETTLE_OUT.getValue()); //付款单
				}
				if (biz_type.equalsIgnoreCase(SysStatusEnum.NOTE_PO_BACK.getValue()) ||
						biz_type.equalsIgnoreCase(SysStatusEnum.NOTE_SALES_OUT.getValue())
				) {
					accSettle.setBizType(SysStatusEnum.NOTE_SETTLE_IN.getValue()); //收款单
				}
				String settleNewBizNo = SysUtils.getNewNoteNo(sysCommonMapper, "acc_settle", accSettle.getBizType().toUpperCase());
				accSettle.setBizNo(settleNewBizNo);
				accSettleMapper.insert(accSettle);
				AccSettleDetail accSettleDetail = new AccSettleDetail();
				accSettleDetail.setHeadId(accSettle.getId());
				accSettleDetail.setBizType(accSettle.getBizType());
				accSettleDetail.setTargetAmount(totalSourceAmount);
				accSettleDetail.setPayableId(accPayable.getId());
				accSettleDetail.setUpdateCount(SysUtils.getUpdateCount(0));
				accSettleDetail.setDelFlag("0");
				accSettleDetail.setGsdm(newHead.getGsdm());
				accSettleDetail.setMemo("自动产生->单据来源:" + newHead.getBizNo() + "-" + accPayable.getBizNo());
				accSettleDetailMapper.insert(accSettleDetail);
			}
		}
	}
}
