package org.jeecg.modules.system.core.service;

import org.jeecg.common.api.vo.Status;
import org.jeecg.modules.system.core.entity.BizSerial;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: biz_serial
 * @Author: jeecg-boot
 * @Date:   2020-06-25
 * @Version: V1.0
 */
public interface IBizSerialService extends IService<BizSerial> {

    /**
     * 根据串号获取所有在库的串号，注意，当id相同时，此时没有合并
     * @param listSerials
     * @param gsdm
     * @return
     */
    List<BizSerial> selectInStoreSerials(List<String> listSerials , String gsdm);

    /**
     * 如果三个串号，合并成一个串号，即在serial1字段中合并成如下格式 123455，123566,12355,针对于多串号的情况
     * @param listSerials
     * @param gsdm
     * @return
     */
    List<BizSerial> selectCorrectInStoreSerials(List<String> listSerials , String gsdm);

    List<BizSerial> selectBizSerialByPurchaseInDetailId(String bizId);

    /**
     * 判断 串号是否在库
     * @param serials
     * @return
     */
    Status izStockBySerials(List<String> serials);

}
