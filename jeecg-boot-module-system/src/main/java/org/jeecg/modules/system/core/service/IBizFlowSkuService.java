package org.jeecg.modules.system.core.service;

import org.jeecg.modules.system.core.entity.BizFlowSku;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: biz_flow_sku
 * @Author: jeecg-boot
 * @Date:   2020-06-12
 * @Version: V1.0
 */
public interface IBizFlowSkuService extends IService<BizFlowSku> {
    boolean updateBizFlowSku(List<BizFlowSku> oldBizFlowSkuList,List<BizFlowSku> newBizFlowSkuList);
}
