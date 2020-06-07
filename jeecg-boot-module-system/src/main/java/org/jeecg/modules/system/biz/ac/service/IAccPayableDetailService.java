package org.jeecg.modules.system.biz.ac.service;

import org.jeecg.modules.system.biz.ac.entity.AccPayableDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 应付款明细表
 * @Author: jeecg-boot
 * @Date:   2020-06-07
 * @Version: V1.0
 */
public interface IAccPayableDetailService extends IService<AccPayableDetail> {

	public List<AccPayableDetail> selectByMainId(String mainId);
}
