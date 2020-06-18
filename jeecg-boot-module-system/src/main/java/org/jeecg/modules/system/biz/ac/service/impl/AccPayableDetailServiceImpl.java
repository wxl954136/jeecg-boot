package org.jeecg.modules.system.biz.ac.service.impl;

import org.jeecg.modules.system.biz.ac.entity.AccPayableDetail;
import org.jeecg.modules.system.biz.ac.entity.AccPayableSettleBaseDetail;
import org.jeecg.modules.system.biz.ac.mapper.AccPayableDetailMapper;
import org.jeecg.modules.system.biz.ac.service.IAccPayableDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 应付款明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
@Service
public class AccPayableDetailServiceImpl extends ServiceImpl<AccPayableDetailMapper, AccPayableDetail> implements IAccPayableDetailService {
	
	@Autowired
	private AccPayableDetailMapper accPayableDetailMapper;
	
	@Override
	public List<AccPayableDetail> selectByMainId(String mainId) {
		return accPayableDetailMapper.selectByMainId(mainId);
	}


}
