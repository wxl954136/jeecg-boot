package org.jeecg.modules.system.biz.so.mapper;

import java.util.List;
import org.jeecg.modules.system.biz.so.entity.BizSalesOutDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 销售明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-13
 * @Version: V1.0
 */
public interface BizSalesOutDetailMapper extends BaseMapper<BizSalesOutDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<BizSalesOutDetail> selectByMainId(@Param("mainId") String mainId);
}
