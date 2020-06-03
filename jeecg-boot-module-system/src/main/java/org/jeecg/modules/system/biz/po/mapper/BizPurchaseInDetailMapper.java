package org.jeecg.modules.system.biz.po.mapper;

import java.util.List;
import org.jeecg.modules.system.biz.po.entity.BizPurchaseInDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 采购入库明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface BizPurchaseInDetailMapper extends BaseMapper<BizPurchaseInDetail> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<BizPurchaseInDetail> selectByMainId(@Param("mainId") String mainId);
}
