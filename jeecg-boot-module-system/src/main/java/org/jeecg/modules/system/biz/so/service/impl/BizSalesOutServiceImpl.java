package org.jeecg.modules.system.biz.so.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.biz.so.entity.BizSalesOut;
import org.jeecg.modules.system.biz.so.entity.BizSalesOutDetail;
import org.jeecg.modules.system.biz.so.mapper.BizSalesOutDetailMapper;
import org.jeecg.modules.system.biz.so.mapper.BizSalesOutMapper;
import org.jeecg.modules.system.biz.so.service.IBizSalesOutService;
import org.jeecg.modules.system.core.entity.BizFlowSku;
import org.jeecg.modules.system.core.entity.CoreStockBaseDetail;
import org.jeecg.modules.system.core.entity.CoreStockBaseHead;
import org.jeecg.modules.system.core.service.IBizFlowSkuService;
import org.jeecg.modules.system.core.service.ICoreCostSkuService;
import org.jeecg.modules.system.core.service.ICoreStockSkuService;
import org.jeecg.modules.system.core.utils.CoreUtils;
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

public class BizSalesOutServiceImpl extends ServiceImpl<BizSalesOutMapper, BizSalesOut> implements IBizSalesOutService {

	@Autowired
	private BizSalesOutMapper bizSalesOutMapper;
	@Autowired
	private BizSalesOutDetailMapper bizSalesOutDetailMapper;
	@Autowired
	private ICoreStockSkuService coreStockSkuService;
	@Autowired
	private ICoreCostSkuService coreCostSkuService;

	@Autowired
	private IBizFlowSkuService bizFlowSkuService;

	@Override
	@Transactional
	public void saveMain(BizSalesOut bizSalesOut, List<BizSalesOutDetail> bizSalesOutDetailList) {
		bizSalesOut.setGsdm(SysUtils.getLoginUser().getGsdm());
		bizSalesOut.setUpdateCount(SysUtils.getUpdateCount(0));
		bizSalesOut.setDelFlag("0"); //默认不删除
		bizSalesOutMapper.insert(bizSalesOut);


		//保存明细表
		saveUpdateDetail(bizSalesOut,null,bizSalesOutDetailList);
		//保存综合业务流水
		saveUpdateBizFlowSku(bizSalesOut,null,bizSalesOutDetailList);
		//保存库存
		saveUpdateCostStockSku(bizSalesOut,null,bizSalesOutDetailList);

		List<String> skuList =  CoreUtils.getBizSkus(null,(List<Object>)(Object)bizSalesOutDetailList);
		coreCostSkuService.setCalCoreCostSku(bizSalesOut.getBizDate()
				,skuList,bizSalesOut.getBizType(),
				bizSalesOut.getGsdm());
		coreCostSkuService.setCalCoreCostSkuBasisMonth(bizSalesOut.getBizDate()
				, skuList, bizSalesOut.getBizType(),
				bizSalesOut.getGsdm());

	}

	@Override
	@Transactional
	public void updateMain(BizSalesOut bizSalesOut,List<BizSalesOutDetail> bizSalesOutDetailList) {
		/**
		 * No.1:修改保存，保存头表
		 */
		bizSalesOut.setGsdm(SysUtils.getLoginUser().getGsdm());
		BizSalesOut oldBizSalesOut =bizSalesOutMapper.selectById(bizSalesOut.getId());
		bizSalesOut.setUpdateCount(SysUtils.getUpdateCount(bizSalesOut.getUpdateCount()));
		bizSalesOut.setDelFlag("0"); //默认不删除
		bizSalesOutMapper.updateById(bizSalesOut);

		//取旧入库数量
		List<BizSalesOutDetail> oldBizSalesOutDetail = bizSalesOutDetailMapper.selectByMainId(bizSalesOut.getId());

		/**
		 * No.2修改保存，明细增删改保存至DB
		 */
		saveUpdateDetail(bizSalesOut,oldBizSalesOutDetail,bizSalesOutDetailList);

		/**
		 * No.3 保存记录至商品业务流水表biz_flow_sku
		 */
		saveUpdateBizFlowSku(bizSalesOut,oldBizSalesOutDetail,bizSalesOutDetailList);

		/**
		 * No.4保存记录至商品串号流水表biz_flow_serial
		 */
		/**
		 * No.5修改保存，更新至库存
		 */
		saveUpdateCostStockSku(bizSalesOut,oldBizSalesOutDetail,bizSalesOutDetailList);

		/**
		 * No.6保存数据至成本表
		 */
		List<String> skuList = CoreUtils.getBizSkus((List<Object>) (Object) oldBizSalesOutDetail, (List<Object>) (Object) bizSalesOutDetailList);
		Date calCostDate = (bizSalesOut.getBizDate().compareTo(oldBizSalesOut.getBizDate()) == 1 ? oldBizSalesOut.getBizDate() : bizSalesOut.getBizDate());
		//计算日末
		coreCostSkuService.setCalCoreCostSku(calCostDate
				, skuList, bizSalesOut.getBizType(),
				bizSalesOut.getGsdm());
		//计算月末
		coreCostSkuService.setCalCoreCostSkuBasisMonth(calCostDate
				, skuList, bizSalesOut.getBizType(),
				bizSalesOut.getGsdm());

		log.info("UPD:结束 计算成本");

		/**
		 * No.7保存数据至成本往来单位交易表acc_trade_amount
		 */


	}

	@Override
	@Transactional
	public void delMain(String id) {
		List<BizSalesOutDetail> oldItemList = bizSalesOutDetailMapper.selectByMainId(id);
		BizSalesOut bizSalesOut = bizSalesOutMapper.selectById(id);
		bizSalesOut.setDelFlag("1"); //一律逻辑删除

		bizSalesOutMapper.updateById(bizSalesOut);

		saveUpdateDetail(bizSalesOut,oldItemList,null);

		saveUpdateBizFlowSku(bizSalesOut,oldItemList,null);

		saveUpdateCostStockSku(bizSalesOut,oldItemList,null);

		List<String> skuList =  CoreUtils.getBizSkus((List<Object>)(Object)oldItemList,null);
		coreCostSkuService.setCalCoreCostSku(bizSalesOut.getBizDate()
				,skuList,bizSalesOut.getBizType(),
				bizSalesOut.getGsdm());
		coreCostSkuService.setCalCoreCostSkuBasisMonth(bizSalesOut.getBizDate()
				, skuList, bizSalesOut.getBizType(),
				bizSalesOut.getGsdm());
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
	 * @param bizSalesOut
	 * @param oldList
	 * @param newList
	 * @return
	 */
	public boolean saveUpdateDetail(BizSalesOut bizSalesOut,List<BizSalesOutDetail> oldList,List<BizSalesOutDetail> newList)
	{
		boolean result = true;
		try {
			Map<String, List<Object>> mapTransDataList = CoreUtils.getBizAddDeleteUpdateList(
					(List<Object>) (Object) oldList,
					(List<Object>) (Object) newList
			);
			saveUpdateActionData(bizSalesOut,
					(List<BizSalesOutDetail>) (Object) mapTransDataList.get("NEW"),
					(List<BizSalesOutDetail>) (Object) mapTransDataList.get("UPD"),
					(List<BizSalesOutDetail>) (Object) mapTransDataList.get("DEL")
			);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	/**
	 *saveUpdateActionData保存数据至数据库明细表
	 * @param bizSalesOut
	 * @param newTransList
	 * @param updTransList
	 * @param delTransList
	 */
	public void saveUpdateActionData(BizSalesOut bizSalesOut,
									 List<BizSalesOutDetail> newTransList,
									 List<BizSalesOutDetail> updTransList,
									 List<BizSalesOutDetail> delTransList)
	{
		if (newTransList != null && newTransList.size() > 0 )
		{
			for(BizSalesOutDetail item  :  newTransList)
			{
				item.setHeadId(bizSalesOut.getId());
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				item.setGsdm(bizSalesOut.getGsdm());
				item.setBizType(bizSalesOut.getBizType());
				item.setDelFlag("0");
				bizSalesOutDetailMapper.insert(item);
			}
		}

		if (updTransList != null && updTransList.size() > 0 )
		{
			for(BizSalesOutDetail item  :  updTransList)
			{
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				bizSalesOutDetailMapper.updateById(item);
			}
		}
		if (delTransList != null && delTransList.size() > 0 )
		{
			for(BizSalesOutDetail item  :  delTransList)
			{
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				item.setDelFlag("1");
				bizSalesOutDetailMapper.updateById(item);
			}
		}
	}

	/**
	 * saveUpdateCostStockSku保存数据至库存
	 * @param bizSalesOut
	 * @param oldList
	 * @param newList
	 * @return
	 */
	public boolean saveUpdateCostStockSku(BizSalesOut bizSalesOut,List<BizSalesOutDetail> oldList,List<BizSalesOutDetail> newList)
	{

		List<CoreStockBaseDetail> oldBaseList = CoreUtils.getCoreBaseDetailList(oldList);
		List<CoreStockBaseDetail> newBaseList =  CoreUtils.getCoreBaseDetailList(newList);
		CoreStockBaseHead headBase = new CoreStockBaseHead();
		BeanUtils.copyProperties(bizSalesOut,headBase);
		coreStockSkuService.updateCoreStoreSku(
				CoreUtils.getUpdateStockQtyForCoreStockSku(
						oldBaseList,
						newBaseList,
						headBase));
		return true;
	}

	public boolean saveUpdateBizFlowSku(BizSalesOut bizSalesOut,List<BizSalesOutDetail> oldList,List<BizSalesOutDetail> newList)
	{
		//整理之前一定要设置
		if(null!= oldList){
			oldList.forEach( e -> {
				e.setHeadId(bizSalesOut.getId());
			} );
		}
		if(null!= newList){
			newList.forEach( e -> {
				e.setHeadId(bizSalesOut.getId());
			} );
		}
		//detailId对应biz业务表中的id,在CoreUtils.getBizFlowSkuList进行设置
		List<BizFlowSku> oldFlowList = CoreUtils.getBizFlowSkuList(oldList);
		List<BizFlowSku> newFlowList = CoreUtils.getBizFlowSkuList(newList);


		if(null!= oldFlowList){
			oldFlowList.forEach( eo -> {
				eo.setStoreId(bizSalesOut.getStoreId());
				eo.setBizDate(bizSalesOut.getBizDate());
				int ALPHA = SysUtils.getNoteAlte(eo.getBizType());  //根据单据类型判断库存是正还是负
				eo.setQty(new BigDecimal(eo.getQty().doubleValue() * ALPHA));
			} );
		}
		if(null!= newFlowList){
			newFlowList.forEach( en -> {
				en.setStoreId(bizSalesOut.getStoreId());
				en.setBizDate(bizSalesOut.getBizDate());
				//是正负，需从明细表中进行判断，换货时，一正一负，是以明细中来检验
				int ALPHA = SysUtils.getNoteAlte(en.getBizType());  //根据单据类型判断库存是正还是负
				en.setQty(new BigDecimal(en.getQty().doubleValue() * ALPHA));
			} );
		}
		//修改时，得获取自己的id,否则，目前自己的id还是空的，因为原值id还是detail明细表的
		bizFlowSkuService.updateBizFlowSku(oldFlowList,newFlowList);
		return true;
	}


}
