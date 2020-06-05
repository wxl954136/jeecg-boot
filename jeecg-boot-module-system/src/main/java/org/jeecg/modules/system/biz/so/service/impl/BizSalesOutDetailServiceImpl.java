package org.jeecg.modules.system.biz.so.service.impl;

import org.jeecg.modules.system.biz.so.entity.BizSalesOutDetail;
import org.jeecg.modules.system.biz.so.mapper.BizSalesOutDetailMapper;
import org.jeecg.modules.system.biz.so.service.IBizSalesOutDetailService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 销售明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-04
 * @Version: V1.0
 */
@Service
public class BizSalesOutDetailServiceImpl extends ServiceImpl<BizSalesOutDetailMapper, BizSalesOutDetail> implements IBizSalesOutDetailService {
	
	@Autowired
	private BizSalesOutDetailMapper bizSalesOutDetailMapper;
	
	@Override
	public List<BizSalesOutDetail> selectByMainId(String mainId) {
		return bizSalesOutDetailMapper.selectByMainId(mainId);
	}
}
