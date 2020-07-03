package org.jeecg.modules.system.core.service;

import org.jeecg.common.api.vo.Status;
import org.jeecg.modules.system.core.entity.BizFlowSerial;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: biz_flow_serial
 * @Author: jeecg-boot
 * @Date:   2020-06-25
 * @Version: V1.0
 */
public interface IBizFlowSerialService extends IService<BizFlowSerial> {
    /**
     * memo:根据传入的值，获取重复的记录值,如采购新增和修改时所有的串号记录,不在数据库中查询
     * @param serials
     * @return
     */
    public List<String> getDuplicateSerial(List<String> serials);


    public Status izDuplicateSerial(List<String> serials) ;

}
