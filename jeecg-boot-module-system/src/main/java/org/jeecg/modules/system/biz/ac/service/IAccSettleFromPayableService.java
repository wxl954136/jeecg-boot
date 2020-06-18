package org.jeecg.modules.system.biz.ac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.biz.ac.entity.AccPayableSettleBaseDetail;
import org.jeecg.modules.system.biz.ac.entity.AccSettleDetail;

import java.util.List;

/**
 * @Description: 结算明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
public interface IAccSettleFromPayableService extends IService<AccPayableSettleBaseDetail> {



	List<AccPayableSettleBaseDetail> selectPayableAmount(List<String> traderIds,
                                                         List<String> bizTypes,
                                                         String gsdm);
}
