package org.jeecg.modules.system.core.service.impl;

import org.jeecg.modules.system.core.entity.BizFlowSerial;
import org.jeecg.modules.system.core.mapper.BizFlowSerialMapper;
import org.jeecg.modules.system.core.service.IBizFlowSerialService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

/**
 * @Description: biz_flow_serial
 * @Author: jeecg-boot
 * @Date:   2020-06-25
 * @Version: V1.0
 */
@Service
public class BizFlowSerialServiceImpl extends ServiceImpl<BizFlowSerialMapper, BizFlowSerial> implements IBizFlowSerialService {

    /**
     * memo:根据传入的值，获取重复的记录值,如采购新增和修改时所有的串号记录,不在数据库中查询
     * @param serials
     * @return
     */
    @Override
    public List<String> getDuplicateSerial(List<String> serials) {

        Set<String> set = new HashSet<>();
        Set<String> exist = new HashSet<>();

        for (String serial:serials) {
            if (set.contains(serial)) {
                exist.add(serial);
            } else {
                set.add(serial);
            }
        }
        List<String> listExist = new ArrayList<>(exist);

        return listExist;
    }
}
