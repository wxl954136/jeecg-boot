package org.jeecg.modules.system.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.core.entity.BizSerial;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: biz_serial
 * @Author: jeecg-boot
 * @Date:   2020-06-25
 * @Version: V1.0
 */
public interface BizSerialMapper extends BaseMapper<BizSerial> {
    List<BizSerial> selectInStoreSerials(List<String> listSerials ,String gsdm);

//
//    List<AccPayableSettleBaseDetail> selectPayableAmount(List<String> traderIds,
//                                                         List<String> bizTypes,
//                                                         String gsdm);

}
