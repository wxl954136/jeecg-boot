package org.jeecg.modules.system.core.utils;

import io.lettuce.core.ScriptOutputType;
import org.jeecg.modules.system.core.entity.CoreBaseDetail;
import org.jeecg.modules.system.core.entity.CoreBaseHead;
import org.jeecg.modules.system.core.entity.CoreStockSkuVo;
import org.jeecg.modules.utils.SysUtils;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreUtils {

    /**
     *
     * @param objList
     * @return
     */
    public static  List<CoreBaseDetail> getCoreBaseDetailList(List<?> objList)
    {
        List<CoreBaseDetail> baseList =  new ArrayList<>();
        for(Object object:objList)
        {
            CoreBaseDetail baseDetail = new CoreBaseDetail();
            BeanUtils.copyProperties(object,baseDetail);
            baseList.add(baseDetail);
        }
        return baseList;
    }
    /**
     * @param oldList
     * @param newList
     * @param headBase
     * @return
     */
    public static List<CoreStockSkuVo> getUpdateStockQtyForCoreStockSku(List<CoreBaseDetail> oldList, List<CoreBaseDetail> newList, CoreBaseHead headBase) {
        List<CoreStockSkuVo> listVo = new ArrayList<>();
        List<CoreBaseDetail> addList = new ArrayList<>();
        List<CoreBaseDetail> delList = new ArrayList<>();
        List<CoreBaseDetail> updateList = new ArrayList<>();
        Map<String, CoreBaseDetail> oldMap = new HashMap<>();
        Map<String, CoreBaseDetail> newMap = new HashMap<>();
        int ALTE = SysUtils.getNoteAlte(headBase.getBizType());  //根据单据类型判断库存是正还是负

        //提高效率，先放入Map
        if (null != oldList) {
            for (CoreBaseDetail oldItem : oldList) {
                if (oldItem != null) oldMap.put(oldItem.getId(), oldItem);
            }
        }
        if (null != newList) {
            for (CoreBaseDetail newItem : newList) {
                if (newItem != null) newMap.put(newItem.getId(), newItem);
            }
        }
        for (Map.Entry<String, CoreBaseDetail> entry : oldMap.entrySet()) {
            String mapKey = entry.getKey();
            CoreBaseDetail mapValue = entry.getValue();
            //修改后new的数据在新的数据中没有找到再视为删除，否则视为修改
            if (!newMap.containsKey(mapKey)) {
                if (mapKey != null) delList.add(mapValue);    //整理出删除的数据
            }
        }
        for (Map.Entry<String, CoreBaseDetail> entry : newMap.entrySet()) {
            if (entry == null) continue;
            String mapKey = entry.getKey();
            CoreBaseDetail mapValue = entry.getValue();
            //修改后new的数据在新的数据中没有找到再视为删除，否则视为修改
            if (!oldMap.containsKey(mapKey)) {
                addList.add(mapValue);  //整理出修改的数据
            } else {
                //整理出修改后的差值，便于计算库存
                CoreBaseDetail old = oldMap.get(mapKey);
                  //如果入入库，则为1,如果为出库则为-1 ,库存必须以正负数表示换货暂时没有考虑，如果是换货，bizType则应为明细中的
                double val = (mapValue.getQty().doubleValue() * ALTE) - (old.getQty().doubleValue() * ALTE);
                mapValue.setQty(new BigDecimal(val));
                updateList.add(mapValue);
            }
        }
        //把addList和updateList合并一次即可
        //addList.addAll(updateList); //一定不能合并，如果是退货的，注意在前面已经加了辅助变量ALTE,如果再加一次，会取反了
        //本逻辑适合所有退货与入库共用界面，切记前台必须是正数无论是退货或入库
        for (CoreBaseDetail item : addList) {
            CoreStockSkuVo stockSkuVo = new CoreStockSkuVo();
            stockSkuVo.setSkuId(item.getSkuId());
            stockSkuVo.setStoreId(headBase.getStoreId());
            stockSkuVo.setOld_qty(new BigDecimal(0* ALTE) );
            stockSkuVo.setNew_qty(new BigDecimal(item.getQty().doubleValue()* ALTE));
            listVo.add(stockSkuVo);
        }

        for (CoreBaseDetail item : delList) {
            CoreStockSkuVo stockSkuVo = new CoreStockSkuVo();
            stockSkuVo.setSkuId(item.getSkuId());
            stockSkuVo.setStoreId(headBase.getStoreId());
            stockSkuVo.setOld_qty(new BigDecimal(0 * ALTE));
            stockSkuVo.setNew_qty(new BigDecimal(0 - (item.getQty().doubleValue() * ALTE)));
            listVo.add(stockSkuVo);
        }
        for (CoreBaseDetail item : updateList) {
            CoreStockSkuVo stockSkuVo = new CoreStockSkuVo();
            stockSkuVo.setSkuId(item.getSkuId());
            stockSkuVo.setStoreId(headBase.getStoreId());
            stockSkuVo.setOld_qty(new BigDecimal(0) );
            stockSkuVo.setNew_qty(new BigDecimal(item.getQty().doubleValue()));
            listVo.add(stockSkuVo);
        }
        return listVo;
    }
}
