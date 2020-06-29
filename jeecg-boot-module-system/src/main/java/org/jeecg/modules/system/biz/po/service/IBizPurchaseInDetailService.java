package org.jeecg.modules.system.biz.po.service;

import org.jeecg.modules.system.biz.po.entity.BizPurchaseInDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.core.vo.BizFlowSerialVo;

import java.util.List;

/**
 * @Description: 采购入库明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface IBizPurchaseInDetailService extends IService<BizPurchaseInDetail> {

	public List<BizPurchaseInDetail> selectByMainId(String mainId);

	/**
	 * 通过detailId 获取串号信息
	 * @param listDetailIds
	 * @return
	 */
	public List<BizFlowSerialVo> selectSerialInfoByDetailId(List<String> listDetailIds);
}
