package org.jeecg.modules.system.bank.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.bank.entity.SysBank;

/**
 * @Description: 银行信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
public interface ISysBankService extends IService<SysBank> {
     void updateSysBank(SysBank sysBank) ;




}
