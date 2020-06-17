package org.jeecg.modules.system.bank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.jeecg.modules.system.bank.entity.SysBank;

import java.util.List;

/**
 * @Description: 银行信息
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
public interface SysBankMapper extends BaseMapper<SysBank> {
    List<SysBank> selectByName(@Param("bankName")String bankName, @Param("gsdm")String gsdm);
}
