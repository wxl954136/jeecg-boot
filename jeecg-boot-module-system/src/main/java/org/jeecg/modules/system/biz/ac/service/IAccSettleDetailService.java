package org.jeecg.modules.system.biz.ac.service;

import org.jeecg.modules.system.biz.ac.entity.AccSettleDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 结算明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
public interface IAccSettleDetailService extends IService<AccSettleDetail> {

	public List<AccSettleDetail> selectByMainId(String mainId);
}
