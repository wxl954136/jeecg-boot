package org.jeecg.modules.system.trader.service.impl;

import org.jeecg.modules.system.trader.entity.SysTrader;
import org.jeecg.modules.system.trader.mapper.SysTraderMapper;
import org.jeecg.modules.system.trader.service.ISysTraderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 往来单位
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Service
public class SysTraderServiceImpl extends ServiceImpl<SysTraderMapper, SysTrader> implements ISysTraderService {
    @Autowired
    private SysTraderMapper sysTraderMapper;
    @Override
    public List<SysTrader> getTraderList(SysTrader sysTrader) {
        return sysTraderMapper.getTraderList(sysTrader);
    }
}
