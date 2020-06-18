package org.jeecg.modules.system.biz.ac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.system.biz.ac.entity.AccPayableSettleBaseDetail;
import org.jeecg.modules.system.biz.ac.entity.AccSettleDetail;
import org.jeecg.modules.system.biz.ac.mapper.AccSettleDetailMapper;
import org.jeecg.modules.system.biz.ac.mapper.AccSettleFromPayableMapper;
import org.jeecg.modules.system.biz.ac.service.IAccSettleDetailService;
import org.jeecg.modules.system.biz.ac.service.IAccSettleFromPayableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 结算明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
@Service
public class AccSettleFromPayableServiceImpl extends ServiceImpl<AccSettleFromPayableMapper, AccPayableSettleBaseDetail> implements IAccSettleFromPayableService {
	
	@Autowired
	private AccSettleFromPayableMapper accSettleFromPayableMapper;

	@Override
	public List<AccPayableSettleBaseDetail> selectPayableAmount(List<String> traderIds, List<String> bizTypes, String gsdm) {
		return accSettleFromPayableMapper.selectPayableAmount(traderIds,bizTypes,gsdm);
	}
}
