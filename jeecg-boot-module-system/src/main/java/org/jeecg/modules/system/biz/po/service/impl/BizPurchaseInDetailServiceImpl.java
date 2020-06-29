package org.jeecg.modules.system.biz.po.service.impl;

import org.jeecg.modules.system.biz.po.entity.BizPurchaseInDetail;
import org.jeecg.modules.system.biz.po.mapper.BizPurchaseInDetailMapper;
import org.jeecg.modules.system.biz.po.service.IBizPurchaseInDetailService;
import org.jeecg.modules.system.core.vo.BizFlowSerialVo;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 采购入库明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Service
public class BizPurchaseInDetailServiceImpl extends ServiceImpl<BizPurchaseInDetailMapper, BizPurchaseInDetail> implements IBizPurchaseInDetailService {
	
	@Autowired
	private BizPurchaseInDetailMapper bizPurchaseInDetailMapper;
	
	@Override
	public List<BizPurchaseInDetail> selectByMainId(String mainId) {
		return bizPurchaseInDetailMapper.selectByMainId(mainId);
	}


	@Override
	public List<BizFlowSerialVo> selectSerialInfoByDetailId(List<String> listDetailIds) {
		return bizPurchaseInDetailMapper.selectSerialInfoByDetailId(listDetailIds, SysUtils.getLoginUser().getGsdm());
	}
}
