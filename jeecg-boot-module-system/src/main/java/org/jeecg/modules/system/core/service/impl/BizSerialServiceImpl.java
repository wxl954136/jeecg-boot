package org.jeecg.modules.system.core.service.impl;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Status;
import org.jeecg.modules.system.core.entity.BizSerial;
import org.jeecg.modules.system.core.mapper.BizSerialMapper;
import org.jeecg.modules.system.core.service.IBizSerialService;
import org.jeecg.modules.utils.SysUtils;
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
     * @return
     */
    @Override
    public List<BizSerial> selectInStoreSerials(List<String> listSerials) {
        return bizSerialMapper.selectInStoreSerials(listSerials,SysUtils.getLoginUser().getGsdm());
    }

    /**
     * 串号该合并时需要合并，如三串号三条记录变成同一条记录，使用逗号隔开返回
     * 12345
     * 12353
     * 23456
     * 转换为 -------> 12345,12353,23456
     * @param listSerials   特别注意，如果串号中有逗号，需分开转成行放至变量中
     * BizSerial : 使用字段为:id,serial
     * @return
     */
    @Override
    public List<BizSerial> selectCorrectInStoreSerials(List<String> listSerials) {
        List<BizSerial> listBizSerials = this.selectInStoreSerials(listSerials);
        Map<String,BizSerial> map = new HashMap<>();
        for(BizSerial serial : listBizSerials){
            if(map.containsKey(serial.getId())){
                BizSerial entity = map.get(serial.getId());
                entity.setSerial(entity.getSerial() + "," + serial.getSerial());
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

    /**
     *通过采购单id来获取串号信息
     * @param bizId
     * @return
     */
    @Override
    public List<BizSerial> selectBizSerialByPurchaseInDetailId(String bizId) {
        return bizSerialMapper.selectBizSerialByPurchaseInDetailId(bizId,SysUtils.getLoginUser().getGsdm());
    }

    /**
     * 判断 串号是否在库，并返回再库串号
     * @param serials
     * @return
     */
    @Override
    public Status izStockBySerials(List<String> serials) {
        List<BizSerial>  listBizSerial = this.selectCorrectInStoreSerials(serials);
        if (listBizSerial !=null && listBizSerial.size() > 0){
            List<Object> listResult = new ArrayList<>();
            for(BizSerial serial: listBizSerial)
            {
                listResult.add(serial.getSerial());
            }
            String value = StringUtils.join(listResult, "|");
            String result = "串号已经在库[" + value + "]";
            return new Status(false,result,listResult);
        }
        return new Status(true,"成功[izStockBySerials]");
    }
}
