package org.jeecg.modules.system.biz.ac.service.impl;

import org.jeecg.modules.system.biz.ac.entity.AccPayableSettleBaseDetail;
import org.jeecg.modules.system.biz.ac.entity.AccSettleDetail;
import org.jeecg.modules.system.biz.ac.mapper.AccSettleDetailMapper;
import org.jeecg.modules.system.biz.ac.service.IAccSettleDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 结算明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
@Service
public class AccSettleDetailServiceImpl extends ServiceImpl<AccSettleDetailMapper, AccSettleDetail> implements IAccSettleDetailService {
	
	@Autowired
	private AccSettleDetailMapper accSettleDetailMapper;
	
	@Override
	public List<AccSettleDetail> selectByMainId(String mainId) {
		return accSettleDetailMapper.selectByMainId(mainId);
	}


}
