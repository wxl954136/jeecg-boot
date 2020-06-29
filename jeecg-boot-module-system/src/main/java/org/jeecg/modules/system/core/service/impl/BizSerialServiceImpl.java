package org.jeecg.modules.system.core.service.impl;

import org.jeecg.modules.system.core.entity.BizSerial;
import org.jeecg.modules.system.core.mapper.BizSerialMapper;
import org.jeecg.modules.system.core.service.IBizSerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

/**
 * @Description: biz_serial
 * @Author: jeecg-boot
 * @Date:   2020-06-25
 * @Version: V1.0
 */
@Service
public class BizSerialServiceImpl extends ServiceImpl<BizSerialMapper, BizSerial> implements IBizSerialService {

    @Autowired
    BizSerialMapper bizSerialMapper;
    /**
     * 根据串号获取在库的串号序列，bizserial.id,bizserial.serial1字段可用
     * @param listSerials
     * @param gsdm
     * @return
     */
    @Override
    public List<BizSerial> selectInStoreSerials(List<String> listSerials, String gsdm) {
        return bizSerialMapper.selectInStoreSerials(listSerials,gsdm);
    }

    /**
     * 串号该合并时需要合并，如三串号三条记录变成同一条记录，使用逗号隔开返回
     * 12345
     * 12353
     * 23456
     * 转换为 -------> 12345,12353,23456
     * @param listSerials   特别注意，如果串号中有逗号，需分开转成行放至变量中
     * @param gsdm
     * BizSerial : 使用字段为:id,serial1
     * @return
     */
    @Override
    public List<BizSerial> selectCorrectInStoreSerials(List<String> listSerials, String gsdm) {
        List<BizSerial> listBizSerials = this.selectInStoreSerials(listSerials,gsdm);
        Map<String,BizSerial> map = new HashMap<>();
        for(BizSerial serial : listBizSerials){
            if(map.containsKey(serial.getId())){
                BizSerial entity = map.get(serial.getId());
                entity.setSerial1(entity.getSerial1() + "," + serial.getSerial1());
                map.put(serial.getId() ,entity);
            }else
            {
                map.put(serial.getId(),serial);
            }
        }
        Collection<BizSerial> valueCollection = map.values();
        List<BizSerial> valueList = new ArrayList<>(valueCollection);
        return valueList;
    }
}
