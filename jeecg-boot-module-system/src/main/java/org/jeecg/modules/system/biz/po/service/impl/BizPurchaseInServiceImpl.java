package org.jeecg.modules.system.biz.po.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.biz.po.entity.BizPurchaseIn;
import org.jeecg.modules.system.biz.po.entity.BizPurchaseInDetail;
import org.jeecg.modules.system.biz.po.mapper.BizPurchaseInDetailMapper;
import org.jeecg.modules.system.biz.po.mapper.BizPurchaseInMapper;
import org.jeecg.modules.system.biz.po.service.IBizPurchaseInService;
import org.jeecg.modules.system.core.entity.CoreStockBaseDetail;
import org.jeecg.modules.system.core.entity.CoreStockBaseHead;
import org.jeecg.modules.system.core.service.ICoreStockSkuService;
import org.jeecg.modules.system.core.utils.CoreUtils;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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


		//保存明细表
		saveUpdateDetail(bizPurchaseIn,null,bizPurchaseInDetailList);

		//保存库存
		saveUpdateCostStockSku(bizPurchaseIn,null,bizPurchaseInDetailList);


	}

	@Override
	@Transactional
	public void updateMain(BizPurchaseIn bizPurchaseIn,List<BizPurchaseInDetail> bizPurchaseInDetailList) {
		/**
		 * No.1:修改保存，保存头表
		 */
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

		/**
		 * No.4 保存记录至商品业务流水表biz_flow_sku
		 */
		/**
		 * No.5 保存记录至商品串号流水表biz_flow_serial
		 */
		/**
		 * No.6修改保存，更新至库存
		 */

		saveUpdateCostStockSku(bizPurchaseIn,oldBizPurChaseInDetail,bizPurchaseInDetailList);

		/**
		 * No.7保存数据至成本表
		 */

		/**
		 * No.8保存数据至成本表core_cost
		 */

		/**
		 * No.9保存数据至成本往来单位交易表acc_trade_amount
		 */

	}

	@Override
	@Transactional
	public void delMain(String id) {
		List<BizPurchaseInDetail> oldItemList = bizPurchaseInDetailMapper.selectByMainId(id);
		BizPurchaseIn bizPurchaseIn = bizPurchaseInMapper.selectById(id);
		bizPurchaseIn.setDelFlag("1"); //一律逻辑删除
		bizPurchaseInMapper.updateById(bizPurchaseIn);
		saveUpdateDetail(bizPurchaseIn,oldItemList,null);
		saveUpdateCostStockSku(bizPurchaseIn,oldItemList,null);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			delMain(id.toString());  //批处理调用单一的即可
		}
	}
	/**
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
	 *
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


	
}
