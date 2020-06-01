package org.jeecg.modules.system.bank.service.impl;

import org.jeecg.modules.system.bank.entity.SysBank;
import org.jeecg.modules.system.bank.mapper.SysBankMapper;
import org.jeecg.modules.system.bank.service.ISysBankService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 银行信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Service
public class SysBankServiceImpl extends ServiceImpl<SysBankMapper, SysBank> implements ISysBankService {
    @Resource
    private SysBankMapper sysBankMapper;
    @Override
    public void updateSysBank(SysBank sysBank) {
        int count = sysBank.getUpdateCount();
        sysBank.setUpdateCount(++count);
        sysBankMapper.updateById(sysBank);
    }
}
