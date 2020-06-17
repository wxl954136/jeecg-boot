package org.jeecg.modules.system.biz.po.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
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
import org.jeecg.modules.system.biz.po.entity.BizPurchaseIn;
import org.jeecg.modules.system.biz.po.entity.BizPurchaseInDetail;
import org.jeecg.modules.system.biz.po.mapper.BizPurchaseInDetailMapper;
import org.jeecg.modules.system.biz.po.mapper.BizPurchaseInMapper;
import org.jeecg.modules.system.biz.po.service.IBizPurchaseInService;
import org.jeecg.modules.system.core.entity.BizFlowSku;
import org.jeecg.modules.system.core.entity.CoreStockBaseDetail;
import org.jeecg.modules.system.core.entity.CoreStockBaseHead;
import org.jeecg.modules.system.core.service.IBizFlowSkuService;
import org.jeecg.modules.system.core.service.ICoreCostSkuService;
import org.jeecg.modules.system.core.service.ICoreStockSkuService;
import org.jeecg.modules.system.core.utils.CoreUtils;
import org.jeecg.modules.system.mapper.SysCommonMapper;
import org.jeecg.modules.utils.SysStatusEnum;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 采购信息主表
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Service
@Slf4j
public class BizPurchaseInServiceImpl extends ServiceImpl<BizPurchaseInMapper, BizPurchaseIn> implements IBizPurchaseInService {

	@Autowired
	private BizPurchaseInMapper bizPurchaseInMapper;
	@Autowired
	private BizPurchaseInDetailMapper bizPurchaseInDetailMapper;
	@Autowired
	private ICoreStockSkuService coreStockSkuService;
	@Autowired
	private ICoreCostSkuService coreCostSkuService;
	@Autowired
	private IBizFlowSkuService bizFlowSkuService;

	@Autowired
	private AccPayableMapper accPayableMapper;
	@Autowired
	private AccPayableDetailMapper accPayableDetailMapper;
	@Autowired
	private AccSettleMapper accSettleMapper;

	@Autowired
	private AccSettleDetailMapper accSettleDetailMapper;
	@Autowired
	private SysCommonMapper sysCommonMapper;

	@Autowired
	private SysBankMapper sysBankMapper;
	@Override
	@Transactional
	public void saveMain(BizPurchaseIn bizPurchaseIn, List<BizPurchaseInDetail> bizPurchaseInDetailList) {
		bizPurchaseIn.setGsdm(SysUtils.getLoginUser().getGsdm());
		bizPurchaseIn.setUpdateCount(SysUtils.getUpdateCount(0));
		bizPurchaseIn.setDelFlag("0"); //默认不删除
		bizPurchaseInMapper.insert(bizPurchaseIn);


		//保存明细表
		saveUpdateDetail(bizPurchaseIn,null,bizPurchaseInDetailList);
		//保存综合业务流水
		saveUpdateBizFlowSku(bizPurchaseIn,null,bizPurchaseInDetailList);
		//保存库存
		saveUpdateCostStockSku(bizPurchaseIn,null,bizPurchaseInDetailList);
		//往来帐
		saveUpdateTraderAccPayableAndSettle(null,bizPurchaseIn,bizPurchaseInDetailList);


		List<String> skuList =  CoreUtils.getBizSkus(null,(List<Object>)(Object)bizPurchaseInDetailList);
		coreCostSkuService.setCalCoreCostSku(bizPurchaseIn.getBizDate()
				,skuList,bizPurchaseIn.getBizType(),
				bizPurchaseIn.getGsdm());
		coreCostSkuService.setCalCoreCostSkuBasisMonth(bizPurchaseIn.getBizDate()
				, skuList, bizPurchaseIn.getBizType(),
				bizPurchaseIn.getGsdm());
	}

	@Override
	@Transactional
	public void updateMain(BizPurchaseIn bizPurchaseIn,List<BizPurchaseInDetail> bizPurchaseInDetailList) {
		/**
		 * No.1:修改保存，保存头表
		 */
		//计算成本时，从最小日期单据日期开始算起,因为会影响到成本
		BizPurchaseIn oldBizPurchaseIn =bizPurchaseInMapper.selectById(bizPurchaseIn.getId());
		bizPurchaseIn.setGsdm(SysUtils.getLoginUser().getGsdm());
		bizPurchaseIn.setUpdateCount(SysUtils.getUpdateCount(bizPurchaseIn.getUpdateCount()));
		bizPurchaseIn.setDelFlag("0"); //默认不删除
		bizPurchaseInMapper.updateById(bizPurchaseIn);



		//取旧入库数量
		List<BizPurchaseInDetail> oldBizPurChaseInDetail = bizPurchaseInDetailMapper.selectByMainId(bizPurchaseIn.getId());

		/**
		 * No.2修改保存，明细增删改保存至DB
		 */
		saveUpdateDetail(bizPurchaseIn,oldBizPurChaseInDetail,bizPurchaseInDetailList);

		/**
		 * No.3 保存记录至商品业务流水表biz_flow_sku
		 */
		saveUpdateBizFlowSku(bizPurchaseIn,oldBizPurChaseInDetail,bizPurchaseInDetailList);

		/**
		 * No.4保存记录至商品串号流水表biz_flow_serial
		 */
		/**
		 * No.5修改保存，更新至库存
		 */
		saveUpdateCostStockSku(bizPurchaseIn,oldBizPurChaseInDetail,bizPurchaseInDetailList);


		/**
		 * No.6保存数据至成本往来单位交易表acc_trade_amount
		 */
		saveUpdateTraderAccPayableAndSettle(oldBizPurchaseIn,bizPurchaseIn,bizPurchaseInDetailList);
		/**
		 * No.7保存数据至成本表
		 */
		log.info("UPD:开始计算成本一定要放至最后");


		List<String> skuList = CoreUtils.getBizSkus((List<Object>) (Object) oldBizPurChaseInDetail, (List<Object>) (Object) bizPurchaseInDetailList);
		Date calCostDate = (bizPurchaseIn.getBizDate().compareTo(oldBizPurchaseIn.getBizDate()) == 1 ? oldBizPurchaseIn.getBizDate() : bizPurchaseIn.getBizDate());
		//计算日末
		coreCostSkuService.setCalCoreCostSku(calCostDate
				, skuList, bizPurchaseIn.getBizType(),
				bizPurchaseIn.getGsdm());
		//计算月末

		coreCostSkuService.setCalCoreCostSkuBasisMonth(calCostDate
				, skuList, bizPurchaseIn.getBizType(),
				bizPurchaseIn.getGsdm());
		log.info("UPD:结束 计算成本");




	}

	@Override
	@Transactional
	public void delMain(String id) {
		List<BizPurchaseInDetail> oldItemList = bizPurchaseInDetailMapper.selectByMainId(id);
		BizPurchaseIn bizPurchaseIn = bizPurchaseInMapper.selectById(id);
		bizPurchaseIn.setDelFlag("1"); //一律逻辑删除

		bizPurchaseInMapper.updateById(bizPurchaseIn);

		saveUpdateDetail(bizPurchaseIn,oldItemList,null);

		saveUpdateBizFlowSku(bizPurchaseIn,oldItemList,null);


		saveUpdateCostStockSku(bizPurchaseIn,oldItemList,null);

		saveUpdateTraderAccPayableAndSettle(bizPurchaseIn,null,oldItemList);


		List<String> skuList =  CoreUtils.getBizSkus((List<Object>)(Object)oldItemList,null);
		coreCostSkuService.setCalCoreCostSku(bizPurchaseIn.getBizDate()
				,skuList,bizPurchaseIn.getBizType(),
				bizPurchaseIn.getGsdm());
		coreCostSkuService.setCalCoreCostSkuBasisMonth(bizPurchaseIn.getBizDate()
				, skuList, bizPurchaseIn.getBizType(),
				bizPurchaseIn.getGsdm());
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			delMain(id.toString());  //批处理调用单一的即可
		}
	}
	/**
	 * saveUpdateDetail
	 * @mark ：当是修改数据时，更新明表
	 * @auth 王晓陆
	 * @param bizPurchaseIn
	 * @param oldList
	 * @param newList
	 * @return
	 */
	public boolean saveUpdateDetail(BizPurchaseIn bizPurchaseIn,List<BizPurchaseInDetail> oldList,List<BizPurchaseInDetail> newList)
	{
		boolean result = true;
		try {
			Map<String, List<Object>> mapTransDataList = CoreUtils.getBizAddDeleteUpdateList(
					(List<Object>) (Object) oldList,
					(List<Object>) (Object) newList
			);
			saveUpdateActionData(bizPurchaseIn,
					(List<BizPurchaseInDetail>) (Object) mapTransDataList.get("NEW"),
					(List<BizPurchaseInDetail>) (Object) mapTransDataList.get("UPD"),
					(List<BizPurchaseInDetail>) (Object) mapTransDataList.get("DEL")
			);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	/**
	 *saveUpdateActionData保存数据至数据库明细表
	 * @param bizPurchaseIn
	 * @param newTransList
	 * @param updTransList
	 * @param delTransList
	 */
	public void saveUpdateActionData(BizPurchaseIn bizPurchaseIn,
									 List<BizPurchaseInDetail> newTransList,
									 List<BizPurchaseInDetail> updTransList,
									 List<BizPurchaseInDetail> delTransList)
	{
		if (newTransList != null && newTransList.size() > 0 )
		{
			for(BizPurchaseInDetail item  :  newTransList)
			{
				item.setHeadId(bizPurchaseIn.getId());
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				item.setGsdm(bizPurchaseIn.getGsdm());
				item.setBizType(bizPurchaseIn.getBizType());
				item.setDelFlag("0");
				bizPurchaseInDetailMapper.insert(item);
			}
		}

		if (updTransList != null && updTransList.size() > 0 )
		{
			for(BizPurchaseInDetail item  :  updTransList)
			{
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				bizPurchaseInDetailMapper.updateById(item);
			}
		}
		if (delTransList != null && delTransList.size() > 0 )
		{
			for(BizPurchaseInDetail item  :  delTransList)
			{
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				item.setDelFlag("1");
				bizPurchaseInDetailMapper.updateById(item);
			}
		}
	}

	/**
	 * saveUpdateCostStockSku保存数据至库存
	 * @param bizPurchaseIn
	 * @param oldList
	 * @param newList
	 * @return
	 */
	public boolean saveUpdateCostStockSku(BizPurchaseIn bizPurchaseIn,List<BizPurchaseInDetail> oldList,List<BizPurchaseInDetail> newList)
	{

		List<CoreStockBaseDetail> oldBaseList = CoreUtils.getCoreBaseDetailList(oldList);
		List<CoreStockBaseDetail> newBaseList =  CoreUtils.getCoreBaseDetailList(newList);
		CoreStockBaseHead headBase = new CoreStockBaseHead();
		BeanUtils.copyProperties(bizPurchaseIn,headBase);
		coreStockSkuService.updateCoreStoreSku(
				CoreUtils.getUpdateStockQtyForCoreStockSku(
						oldBaseList,
						newBaseList,
						headBase));
		return true;
	}

	public boolean saveUpdateBizFlowSku(BizPurchaseIn bizPurchaseIn,List<BizPurchaseInDetail> oldList,List<BizPurchaseInDetail> newList)
	{
		//整理之前一定要设置
		if(null!= oldList){
			oldList.forEach( e -> {
				e.setHeadId(bizPurchaseIn.getId());
			} );
		}
		if(null!= newList){
			newList.forEach( e -> {
				e.setHeadId(bizPurchaseIn.getId());
			} );
		}
		//detailId对应biz业务表中的id,在CoreUtils.getBizFlowSkuList进行设置
		List<BizFlowSku> oldFlowList = CoreUtils.getBizFlowSkuList(oldList);
		List<BizFlowSku> newFlowList = CoreUtils.getBizFlowSkuList(newList);
		if(null!= oldFlowList){
			oldFlowList.forEach( eo -> {
				eo.setStoreId(bizPurchaseIn.getStoreId());
				eo.setBizDate(bizPurchaseIn.getBizDate());
				int ALPHA = SysUtils.getNoteAlte(eo.getBizType());  //根据单据类型判断库存是正还是负
				eo.setQty(new BigDecimal(eo.getQty().doubleValue() * ALPHA));
			} );
		}
		if(null!= newFlowList){
			newFlowList.forEach( en -> {
				en.setStoreId(bizPurchaseIn.getStoreId());
				en.setBizDate(bizPurchaseIn.getBizDate());
				//是正负，需从明细表中进行判断，换货时，一正一负，是以明细中来检验
				int ALPHA = SysUtils.getNoteAlte(en.getBizType());  //根据单据类型判断库存是正还是负
				en.setQty(new BigDecimal(en.getQty().doubleValue() * ALPHA));
			} );
		}
		//修改时，得获取自己的id,否则，目前自己的id还是空的，因为原值id还是detail明细表的
		bizFlowSkuService.updateBizFlowSku(oldFlowList,newFlowList);
		return true;
	}

	/**
	 * 更新财务核算，应收付款模块
	 * @param bizPurchaseIn
	 * @param newList
	 * @return
	 */
	public boolean saveUpdateTraderAccPayableAndSettle(BizPurchaseIn oldBizPurchaseIn,BizPurchaseIn bizPurchaseIn,List<BizPurchaseInDetail> newList)
	{
		Result<?> result = new Result<Object>();
		//表示是修改
		if (oldBizPurchaseIn != null){
			//如果原来是现金产生的，则需要删除
			if(oldBizPurchaseIn.getTradeMethod().equalsIgnoreCase(SysStatusEnum.SYS_PAYABLE_CASH.getValue())){
				//删除应付款
				AccPayable accPayable = accPayableMapper.selectByFromBizId(oldBizPurchaseIn.getId(),oldBizPurchaseIn.getGsdm());
				accPayableDetailMapper.deleteByMainId(accPayable.getId());
				accPayableMapper.deleteByFromBizId(oldBizPurchaseIn.getId(),oldBizPurchaseIn.getGsdm());
				//删除结算单
				List<AccSettleDetail> accSettleDetailList = accSettleDetailMapper.selectByPayableId(accPayable.getId(),oldBizPurchaseIn.getGsdm());
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
				AccPayable accPayable = accPayableMapper.selectByFromBizId(oldBizPurchaseIn.getId(),oldBizPurchaseIn.getGsdm());
				List<AccSettleDetail> accSettleDetailList = accSettleDetailMapper.selectByPayableId(accPayable.getId(),oldBizPurchaseIn.getGsdm());

				if (accSettleDetailList !=null && accSettleDetailList.size() > 0){
					result.error500("该单据已经产生结算单，请联系相关人员删除结算单再修改");
					return false;
				}
				//删除结算单 好像这一步是无用的呢
				if (accSettleDetailList !=null && accSettleDetailList.size() == 0 ){
					//因为结算单是一对多，因此删除对头表时，只有一条记录方可以删除
					accSettleMapper.deleteById(accSettleDetailList.get(0).getHeadId());
				}
			}
		}
		//修改和新增时，需要插入数据
		if (bizPurchaseIn != null)
		{
			String biz_type = bizPurchaseIn.getBizType(); //单据类型
			String trade_method = bizPurchaseIn.getTradeMethod(); //交易方式
			AccPayable accPayable = new AccPayable();
			accPayable.setNoteSource("AUTO");
			accPayable.setHandler(SysUtils.getLoginUser().getRealname());
			accPayable.setGsdm(bizPurchaseIn.getGsdm());
			accPayable.setDelFlag("0");
			accPayable.setSubjectsId("暂未定［待科目确定］一定要给个默认值");
			accPayable.setUpdateCount(SysUtils.getUpdateCount(0));
			accPayable.setFromBizId(bizPurchaseIn.getId());
			accPayable.setFromBizType(bizPurchaseIn.getBizType());
			accPayable.setMemo("自动产生->单据来源:" + bizPurchaseIn.getBizNo());
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
			for(BizPurchaseInDetail entity:newList){
				totalSourceAmount = totalSourceAmount.add(entity.getQty().multiply(entity.getPrice()));
			}
			AccPayableDetail accPayableDetail = new AccPayableDetail();
			accPayableDetail.setHeadId(accPayable.getId());
			accPayableDetail.setBizType(accPayable.getBizType());
			accPayableDetail.setSourceAmount(totalSourceAmount);
			accPayableDetail.setTraderId(bizPurchaseIn.getTraderId());
			accPayableDetail.setUpdateCount(SysUtils.getUpdateCount(0));
			accPayableDetail.setDelFlag("0");
			accPayableDetail.setGsdm(bizPurchaseIn.getGsdm());
			accPayableDetail.setMemo("自动产生->来源" + bizPurchaseIn.getBizNo());
			accPayableDetailMapper.insert(accPayableDetail);
			//如果是现金，则要收成应收付款，同时生成结算单据
			if (trade_method.equalsIgnoreCase(SysStatusEnum.SYS_PAYABLE_CASH.getValue()) ||
					trade_method.equalsIgnoreCase(SysStatusEnum.SYS_RECEIVABLE_CASH.getValue())
			) {
				AccSettle accSettle = new AccSettle();
				accSettle.setNoteSource("AUTO");
				List<SysBank> sysBanks = sysBankMapper.selectByName("现金",bizPurchaseIn.getGsdm());
				if (sysBanks !=null)
				{
					accSettle.setBankId(sysBanks.get(0).getId());
				}
				accSettle.setGsdm(bizPurchaseIn.getGsdm());
				accSettle.setDelFlag("0");
				accSettle.setSubjectsId("暂［待科目确定］定一定要给个默认值");
				accSettle.setUpdateCount(SysUtils.getUpdateCount(0));
				accSettle.setHandler(SysUtils.getLoginUser().getRealname());
				accSettle.setMemo("自动产生->单据来源:" + bizPurchaseIn.getBizNo() + "-" + accPayable.getBizNo());
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
				accSettleDetail.setGsdm(bizPurchaseIn.getGsdm());
				accSettleDetail.setMemo("自动产生->单据来源:" + bizPurchaseIn.getBizNo() + "-" + accPayable.getBizNo());
				accSettleDetailMapper.insert(accSettleDetail);
			}
		}
		return true;
	}
	
}
