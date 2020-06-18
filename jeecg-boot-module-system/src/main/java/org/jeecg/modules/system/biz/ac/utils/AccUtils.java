package org.jeecg.modules.system.biz.ac.utils;

import org.jeecg.modules.system.biz.ac.entity.AccPayableSettleBaseDetail;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class AccUtils {

    /**
     *将业务传过来的数据转换为公共的类对象
     * @param objList
     * @return
     */
    public static List<AccPayableSettleBaseDetail> getAccBaseDetail(List<?> objList)
    {
        //里面必须包括skuId,storeId,qty三个段来计算库存，否则计处存会有问题
        List<AccPayableSettleBaseDetail> baseList =  new ArrayList<>();
        if (objList != null)
        {
            for(Object object:objList)
            {
                AccPayableSettleBaseDetail baseDetail = new AccPayableSettleBaseDetail();
                BeanUtils.copyProperties(object,baseDetail);
                baseList.add(baseDetail);
            }
        }
        return baseList;
    }
}
