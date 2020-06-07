package org.jeecg.modules.system.biz.ac.mapper;

import java.util.List;
import org.jeecg.modules.system.biz.ac.entity.AccPayableDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 应付款明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
public interface AccPayableDetailMapper extends BaseMapper<AccPayableDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<AccPayableDetail> selectByMainId(@Param("mainId") String mainId);
}