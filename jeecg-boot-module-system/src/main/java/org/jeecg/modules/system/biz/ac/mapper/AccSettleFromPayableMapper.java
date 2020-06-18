package org.jeecg.modules.system.biz.ac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.biz.ac.entity.AccPayableSettleBaseDetail;
import org.jeecg.modules.system.biz.ac.entity.AccSettleDetail;

import java.util.List;

/**
 * @Description: 结算明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
public interface AccSettleFromPayableMapper extends BaseMapper<AccPayableSettleBaseDetail> {


	List<AccPayableSettleBaseDetail> selectPayableAmount(@Param("traderIds") List<String> traderIds,
                                                         @Param("bizTypes") List<String> bizTypes,
                                                         @Param("gsdm") String gsdm);
}
