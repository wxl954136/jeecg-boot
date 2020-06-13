package org.jeecg.modules.system.biz.so.service;

import org.jeecg.modules.system.biz.so.entity.BizSalesOutDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 销售明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-13
 * @Version: V1.0
 */
public interface IBizSalesOutDetailService extends IService<BizSalesOutDetail> {

	public List<BizSalesOutDetail> selectByMainId(String mainId);
}
