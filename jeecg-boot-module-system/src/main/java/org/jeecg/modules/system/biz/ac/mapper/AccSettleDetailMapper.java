package org.jeecg.modules.system.biz.ac.mapper;

import java.util.List;

import org.jeecg.modules.system.biz.ac.entity.AccPayableSettleBaseDetail;
import org.jeecg.modules.system.biz.ac.entity.AccSettleDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 结算明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
public interface AccSettleDetailMapper extends BaseMapper<AccSettleDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<AccSettleDetail> selectByMainId(@Param("mainId") String mainId);

	public List<AccSettleDetail> selectByPayableId(@Param("payableId") String payableId,@Param("gsdm") String gsdm);
 
}
