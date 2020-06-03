package org.jeecg.modules.system.trader.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.trader.entity.SysTrader;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 往来单位
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface SysTraderMapper extends BaseMapper<SysTrader> {
    List<SysTrader> getTraderList(@Param("sysTrader")SysTrader sysTrader);
}
