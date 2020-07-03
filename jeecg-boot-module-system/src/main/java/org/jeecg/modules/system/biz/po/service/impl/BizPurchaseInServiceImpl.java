package org.jeecg.modules.system.biz.po.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Status;
import org.jeecg.modules.system.biz.ac.service.IAccSettleService;
import org.jeecg.modules.system.biz.common.entity.BizCommonDetail;
import org.jeecg.modules.system.biz.common.entity.BizCommonHead;
import org.jeecg.modules.system.biz.po.entity.BizPurchaseIn;
import org.jeecg.modules.system.biz.po.entity.BizPurchaseInDetail;
import org.jeecg.modules.system.biz.po.mapper.BizPurchaseInDetailMapper;
import org.jeecg.modules.system.biz.po.mapper.BizPurchaseInMapper;
import org.jeecg.modules.system.biz.po.service.IBizPurchaseInDetailService;
import org.jeecg.modules.system.biz.po.service.IBizPurchaseInService;
import org.jeecg.modules.system.core.entity.*;
import org.jeecg.modules.system.core.mapper.BizFlowSerialMapper;
import org.jeecg.modules.system.core.mapper.BizSerialMapper;
import org.jeecg.modules.system.core.service.*;
import org.jeecg.modules.system.core.utils.CoreUtils;
import org.jeecg.modules.system.core.vo.BizFlowSerialVo;
import org.jeecg.modules.utils.SysStatusEnum;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
@Slf4j
public class BizPurchaseInServiceImpl extends ServiceImpl<BizPurchaseInMapper, BizPurchaseIn> implements IBizPurchaseInService {

	@Autowired
	private BizPurchaseInMapper bizPurchaseInMapper;
	@Autowired
	private BizPurchaseInDetailMapper bizPurchaseInDetailMapper;

    @Autowired
    private IBizPurchaseInDetailService bizPurchaseInDetailService;
	@Autowired
	private ICoreStockSkuService coreStockSkuService;
	@Autowired
	private ICoreCostSkuService coreCostSkuService;
	@Autowired
	private IBizFlowSkuService bizFlowSkuService;
	@Autowired
	private IBizFlowSerialService bizFlowSerialService;




	@Autowired
	private IBizSerialService bizSerialService;

	@Autowired
	private BizSerialMapper bizSerialMapper;

	@Autowired
	private BizFlowSerialMapper bizFlowSerialMapper;


	@Autowired
	private IAccSettleService accSettleService;

	@Override
	@Transactional
	public Status saveMain(BizPurchaseIn bizPurchaseIn, List<BizPurchaseInDetail> bizPurchaseInDetailList) {

		bizPurchaseIn.setGsdm(SysUtils.getLoginUser().getGsdm());
		bizPurchaseIn.setUpdateCount(SysUtils.getUpdateCount(0));
		bizPurchaseIn.setDelFlag("0"); //默认不删除
		bizPurchaseInMapper.insert(bizPurchaseIn);



		//保存明细表
		saveUpdateDetail(bizPurchaseIn,null,bizPurchaseInDetailList);
		//保存综合业务流水
		saveUpdateBizFlowSku(bizPurchaseIn,null,bizPurchaseInDetailList);

		//保存串号信息
		Status statusSerial = saveUpdateBizFlowSerial(bizPurchaseIn,bizPurchaseInDetailList,"NEW");
		if (!statusSerial.getSuccess()){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return statusSerial;
		}

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


		return new Status(true,"成功");
	}

	@Override
	@Transactional
	public Status updateMain(BizPurchaseIn bizPurchaseIn,List<BizPurchaseInDetail> bizPurchaseInDetailList) {
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
		//保存串号信息
		Status statusSerial = saveUpdateBizFlowSerial(bizPurchaseIn,bizPurchaseInDetailList,"UPD");
		if (!statusSerial.getSuccess()){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return statusSerial;
		}

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

		return new Status(true,"成功");



	}

	@Override
	@Transactional
	public Status delMain(String id) {
		List<BizPurchaseInDetail> oldItemList = bizPurchaseInDetailMapper.selectByMainId(id);
		BizPurchaseIn bizPurchaseIn = bizPurchaseInMapper.selectById(id);


		saveUpdateBizFlowSku(bizPurchaseIn,oldItemList,null);

/**
 * No.4保存记录至商品串号流水表biz_flow_serial
 */
		//保存串号信息
		Status statusSerial = saveUpdateBizFlowSerial(bizPurchaseIn,oldItemList,"DEL");
		if (!statusSerial.getSuccess()){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return statusSerial;
		}

		saveUpdateCostStockSku(bizPurchaseIn,oldItemList,null);

		saveUpdateTraderAccPayableAndSettle(bizPurchaseIn,null,oldItemList);


		List<String> skuList =  CoreUtils.getBizSkus((List<Object>)(Object)oldItemList,null);
		coreCostSkuService.setCalCoreCostSku(bizPurchaseIn.getBizDate()
				,skuList,bizPurchaseIn.getBizType(),
				bizPurchaseIn.getGsdm());
		coreCostSkuService.setCalCoreCostSkuBasisMonth(bizPurchaseIn.getBizDate()
				, skuList, bizPurchaseIn.getBizType(),
				bizPurchaseIn.getGsdm());


		bizPurchaseIn.setDelFlag("1"); //一律逻辑删除 主表一定要放至最后
		bizPurchaseInMapper.updateById(bizPurchaseIn);
		saveUpdateDetail(bizPurchaseIn,oldItemList,null);

		return new Status(true,"成功");
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
		if (null != newTransList   && newTransList.size() > 0 )
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

		if (null != updTransList  && updTransList.size() > 0 )
		{
			for(BizPurchaseInDetail item  :  updTransList)
			{
				item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
				bizPurchaseInDetailMapper.updateById(item);
			}
		}
		if (null != delTransList  && delTransList.size() > 0 )
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
			oldFlowList.forEach( ea -> {
				ea.setStoreId(bizPurchaseIn.getStoreId());
				ea.setBizDate(bizPurchaseIn.getBizDate());
				int ALPHA = SysUtils.getNoteAlte(ea.getBizType());  //根据单据类型判断库存是正还是负
				ea.setQty(new BigDecimal(ea.getQty().doubleValue() * ALPHA));
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
	 * 更新串号信息
	 * @param bizPurchaseIn
	 * @param bizPurchaseInDetailList
	 * @param optType 操作类型 NEW,UPD,DEL
	 */
	public Status saveUpdateBizFlowSerial(BizPurchaseIn bizPurchaseIn,List<BizPurchaseInDetail> bizPurchaseInDetailList,String optType)
	{

		List<String> newAllSerials = new ArrayList<>();
		List<BizFlowSerialVo> listNewAllBizFlowSerialVo = new ArrayList<>();
		for (BizPurchaseInDetail detail :bizPurchaseInDetailList ) {
			List<BizFlowSerialVo> listFlowSerial = detail.getListBizFlowSerial() ;
			if (null!=listFlowSerial && listFlowSerial.size() > 0){
				for (BizFlowSerialVo serialVo :listFlowSerial ) {
					//为bizSerial/bizSerialFlow赋值
                    //--------------id在前端已经自动产生
					serialVo.setHeadId(detail.getId());  //bizSerialFlow 取头表的id
					serialVo.setSerialId(serialVo.getId()); //bizSerialFlow 中的串号id 就是bizSerial.id
					serialVo.setBizId(detail.getId());  // bizSerial 再进行次强行关联至明细表
					serialVo.setSkuId(detail.getSkuId()); // bizSerial
                    serialVo.setBizDate(bizPurchaseIn.getBizDate()); // bizSerial
                    serialVo.setStoreId(bizPurchaseIn.getStoreId()); // bizSerial
					serialVo.setGsdm(SysUtils.getLoginUser().getGsdm()); // bizSerial
					serialVo.setBizType(SysStatusEnum.NOTE_PO_IN.getValue()); // bizSerial
					serialVo.setQty(1); // bizSerial
					serialVo.setDelFlag("0"); // bizSerial
					listNewAllBizFlowSerialVo.add(serialVo);
                    newAllSerials.addAll(Arrays.asList(serialVo.getSerial().split(",")));
				}
			}
		}
		//不经过数据库，直接判断串号
		if (null != newAllSerials   && newAllSerials.size() > 0 ) {
			Status status = bizFlowSerialService.izDuplicateSerial(newAllSerials);
			if (!status.getSuccess()){
				return status;
			}
//			List<String> listDups = bizFlowSerialService.getDuplicateSerial(newAllSerials);
//			if (listDups!=null && listDups.size() > 0){
//				String value = StringUtils.join(listDups, ",");
//				String result = "串号重复[" + value + "]";
//				return new Status(false,result);
//			}
		}

		List<BizSerial> listNewBizSerial = new ArrayList<>();  //biz_serial
        List<BizFlowSerial> listNewBizFlowSerial = new ArrayList<>(); //biz_flow_serial
        Map<String,List<Object>> newMapTrans = getTransObject(listNewAllBizFlowSerialVo);

        listNewBizSerial = (List<BizSerial>) (Object) newMapTrans.get("BIZSERIAL");
        listNewBizFlowSerial = (List<BizFlowSerial>) (Object) newMapTrans.get("BIZFLOWSERIAL");

        List<BizSerial> listOldBizSerial = new ArrayList<>();  //biz_serial
		List<BizFlowSerial> listOldBizFlowSerial = new ArrayList<>(); //biz_flow_serial
		if(optType.equalsIgnoreCase("UPD") || optType.equalsIgnoreCase("DEL") ){
 			List<BizFlowSerialVo>  listOldAllBizFlowSerialVo = bizPurchaseInDetailService.selectSerialInfoById(bizPurchaseIn.getId());
 			Map<String,List<Object>> oldMapTrans = getTransObject(listOldAllBizFlowSerialVo);
			listOldBizSerial = (List<BizSerial>) (Object) oldMapTrans.get("BIZSERIAL");
			listOldBizFlowSerial = (List<BizFlowSerial>) (Object) oldMapTrans.get("BIZFLOWSERIAL");
		}
		if(optType.equalsIgnoreCase("DEL")) {
			//删除没有新值，直接空
			listNewBizSerial = null;
			listNewBizFlowSerial = null;
		}
		//如果是采购入库，需要存入biz_serial串号登记表
		//一定要将新增和修改放开来做，以提升速度
		if (bizPurchaseIn.getBizType().equalsIgnoreCase(SysStatusEnum.NOTE_PO_IN.getValue()) && optType.equalsIgnoreCase("NEW")){
		    if (null != newAllSerials   && newAllSerials.size() > 0) {
                //经过数据库判断 是否在库
				Status status = bizSerialService.izStockBySerials(newAllSerials);
				if (!status.getSuccess()){
					return status;
				}
            }
		}
		//存储数据至biz_flow_serial
        Map<String, List<Object>> mapTransBizFlowSerialDataList = CoreUtils.getBizAddDeleteUpdateList(
                (List<Object>) (Object) listOldBizFlowSerial,
                (List<Object>) (Object) listNewBizFlowSerial
        );
        saveUpdateBizFlowSerial2BizFlowSerial(
        		(List<BizFlowSerial>) (Object) mapTransBizFlowSerialDataList.get("NEW"),
                (List<BizFlowSerial>) (Object) mapTransBizFlowSerialDataList.get("UPD"),
                (List<BizFlowSerial>) (Object) mapTransBizFlowSerialDataList.get("DEL")
        );
        //存储数据至biz_serial
        Map<String, List<Object>> mapTransBizSerialDataList = CoreUtils.getBizAddDeleteUpdateList(
                (List<Object>) (Object) listOldBizSerial,
                (List<Object>) (Object) listNewBizSerial
        );
		//存储数据至biz_flow_serial
        Status statusByBizSerial = saveUpdateBizFlowSerial2BizSerial(
        		(List<BizSerial>) (Object) mapTransBizSerialDataList.get("NEW"),
                (List<BizSerial>) (Object) mapTransBizSerialDataList.get("UPD"),
                (List<BizSerial>) (Object) mapTransBizSerialDataList.get("DEL")
        );
        if (!statusByBizSerial.getSuccess()){
        	return statusByBizSerial;
		}
		return  new Status(true,"成功[001]");

	}
	public Status saveUpdateBizFlowSerial2BizSerial(List<BizSerial> newList,List<BizSerial> updList,List<BizSerial> delList){
		//判断串号是否在库


        if(delList !=null && delList.size() > 0 ){
            for(BizSerial entity:delList){
				entity.setUpdateCount(SysUtils.getUpdateCount(entity.getUpdateCount()));
                bizSerialMapper.deleteById(entity.getId());
            }
        }
	    if(newList !=null && newList.size() > 0 ){

            List<String> newAllSerials = new ArrayList<>();
            for(BizSerial serial:newList){
                newAllSerials.addAll(Arrays.asList(serial.getSerial().split(",")));
            }
            Status status = bizSerialService.izStockBySerials(newAllSerials);
            if (!status.getSuccess()){
                return status;
            }

	        for(BizSerial entity:newList){
				entity.setUpdateCount(SysUtils.getUpdateCount(entity.getUpdateCount()));
				String serials[] = entity.getSerial().split(",");
				if (serials.length >= 1)  entity.setSerial1(serials[0]);
				if (serials.length >= 2)  entity.setSerial2(serials[1]);
				if (serials.length >= 3)  entity.setSerial3(serials[2]);
	            bizSerialMapper.insert(entity);
            }
        }
		if(updList !=null && updList.size() > 0 ){
			for(BizSerial entity:updList){
				entity.setUpdateCount(SysUtils.getUpdateCount(entity.getUpdateCount()));
				bizSerialMapper.updateById(entity);
			}
		}
		return  new Status(true,"成功[002]");
    }
    public void saveUpdateBizFlowSerial2BizFlowSerial(List<BizFlowSerial> newList,List<BizFlowSerial> updList,List<BizFlowSerial> delList){

		if(null != delList   && delList.size() > 0 ){
			for(BizFlowSerial entity:delList){
				entity.setUpdateCount(SysUtils.getUpdateCount(entity.getUpdateCount()));
				entity.setDelFlag("1");
				bizFlowSerialMapper.updateById(entity);
			}
		}
		if(null != newList   && newList.size() > 0 ){
			for(BizFlowSerial entity:newList){
				entity.setUpdateCount(SysUtils.getUpdateCount(entity.getUpdateCount()));
				bizFlowSerialMapper.insert(entity);
			}
		}
		if(null != updList  && updList.size() > 0 ){
			for(BizFlowSerial entity:updList){
				entity.setUpdateCount(SysUtils.getUpdateCount(entity.getUpdateCount()));
				bizFlowSerialMapper.updateById(entity);
			}
		}

    }
	public Map<String,List<Object>> getTransObject(List<BizFlowSerialVo> listVo){

	    Map<String,List<Object>> resultMap = new HashMap<>();
	    List<BizSerial> listSerial = new ArrayList<>();
        List<BizFlowSerial> listFlow = new ArrayList<>(); //biz_flow_serial
        for(BizFlowSerialVo entityVo : listVo){
            BizSerial bizSerial = new BizSerial();
            BeanUtils.copyProperties(entityVo,bizSerial);
            bizSerial.setUpdateCount(SysUtils.getUpdateCount(bizSerial.getUpdateCount()));
            listSerial.add(bizSerial);
            BizFlowSerial bizFlowSerial = new BizFlowSerial();
            BeanUtils.copyProperties(entityVo,bizFlowSerial);
            bizFlowSerial.setUpdateCount(SysUtils.getUpdateCount(bizFlowSerial.getUpdateCount()));
            listFlow.add(bizFlowSerial);
        }

        resultMap.put("BIZSERIAL",(List<Object>) (Object) listSerial);
        resultMap.put("BIZFLOWSERIAL",(List<Object>) (Object) listFlow);

	    return resultMap;
    }
	/**
	 * 更新财务核算，应收付款模块
	 * @param bizPurchaseIn
	 * @param newList
	 * @return
	 */
	public boolean saveUpdateTraderAccPayableAndSettle(BizPurchaseIn oldBizPurchaseIn,BizPurchaseIn bizPurchaseIn,List<BizPurchaseInDetail> newList)
	{
		BizCommonHead oldBizCommonHead = new BizCommonHead();
		if (null != oldBizPurchaseIn)		BeanUtils.copyProperties(oldBizPurchaseIn,oldBizCommonHead);
		else oldBizCommonHead = null;

		BizCommonHead newBizCommonHead = new BizCommonHead();
		if (null != bizPurchaseIn   ) BeanUtils.copyProperties(bizPurchaseIn,newBizCommonHead);
		else newBizCommonHead = null;

		List<BizCommonDetail> newCommonList = new ArrayList<>();
		if (null != newList   && newList.size() > 0)
		{
			for(BizPurchaseInDetail entity:newList){
				BizCommonDetail commonDetail = new BizCommonDetail();
				BeanUtils.copyProperties(entity,commonDetail);
				newCommonList.add(commonDetail);
			}
		}
		accSettleService.savePayableAndSettle(oldBizCommonHead,newBizCommonHead,newCommonList);
		return true;
	}









}
