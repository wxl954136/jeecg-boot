package org.jeecg.modules.system.trader.service;

import org.jeecg.modules.system.trader.entity.SysTrader;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 往来单位
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
public interface ISysTraderService extends IService<SysTrader> {
    List<SysTrader> getTraderList(SysTrader sysTrader);
}
