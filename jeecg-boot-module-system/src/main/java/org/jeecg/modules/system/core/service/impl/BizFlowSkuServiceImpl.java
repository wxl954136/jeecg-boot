package org.jeecg.modules.system.core.service.impl;

import org.jeecg.modules.system.core.entity.BizFlowSku;
import org.jeecg.modules.system.core.mapper.BizFlowSkuMapper;
import org.jeecg.modules.system.core.service.IBizFlowSkuService;
import org.jeecg.modules.system.core.utils.CoreUtils;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: biz_flow_sku
 * @Author: jeecg-boot
 * @Date:   2020-06-12
 * @Version: V1.0
 */
@Service
public class BizFlowSkuServiceImpl extends ServiceImpl<BizFlowSkuMapper, BizFlowSku> implements IBizFlowSkuService {
    @Autowired
    private BizFlowSkuMapper bizFlowSkuMapper;
    @Override
    public boolean updateBizFlowSku(List<BizFlowSku> oldBizFlowSkuList, List<BizFlowSku> newBizFlowSkuList) {
        Map<String, List<Object>> mapTransDataList = CoreUtils.getBizFlowSkuList(
                (List<Object>) (Object) oldBizFlowSkuList,
                (List<Object>) (Object) newBizFlowSkuList
        );
        saveUpdateActionData(
                (List<BizFlowSku>) (Object) mapTransDataList.get("NEW"),
                (List<BizFlowSku>) (Object) mapTransDataList.get("UPD"),
                (List<BizFlowSku>) (Object) mapTransDataList.get("DEL")
        );
        return false;
    }
    public void saveUpdateActionData( List<BizFlowSku> newTransList,
                                      List<BizFlowSku> updTransList,
                                      List<BizFlowSku> delTransList)
    {
        //特别注意，巧妙 的运用了detailId作为表的iD
        if (newTransList != null && newTransList.size() > 0 )
        {
            for(BizFlowSku item  :  newTransList)
            {
                //其他动作不用作，因为在转换为该对象时，已经从相关业务中做了同步更新
                item.setId(item.getDetailId()); //因为是插入，所以可以自动生成
                bizFlowSkuMapper.insert(item);
            }
        }

        if (updTransList != null && updTransList.size() > 0 )
        {
            for(BizFlowSku item  :  updTransList)
            {
                item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
                bizFlowSkuMapper.updateById(item);
            }
        }
        if (delTransList != null && delTransList.size() > 0 )
        {
            for(BizFlowSku item  :  delTransList)
            {
                item.setUpdateCount(SysUtils.getUpdateCount(item.getUpdateCount()));
                item.setDelFlag("1");
                bizFlowSkuMapper.updateById(item);
            }
        }
    }

}
