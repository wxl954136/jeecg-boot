package org.jeecg.modules.system.core.utils;

import org.jeecg.modules.system.core.entity.CoreStockBaseDetail;
import org.jeecg.modules.system.core.entity.CoreStockBaseHead;
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
    public static  List<CoreStockBaseDetail> getCoreBaseDetailList(List<?> objList)
    {
        //里面必须包括skuId,storeId,qty三个段来计算库存，否则计处存会有问题
        List<CoreStockBaseDetail> baseList =  new ArrayList<>();
        if (objList != null)
        {
            for(Object object:objList)
            {
                CoreStockBaseDetail baseDetail = new CoreStockBaseDetail();
                BeanUtils.copyProperties(object,baseDetail);
                baseList.add(baseDetail);
            }
        }
        return baseList;
    }
    /**
     * @param oldList
     * @param newList
     * @param headBase
     * @return
     */
    public static List<CoreStockSkuVo> getUpdateStockQtyForCoreStockSku(List<CoreStockBaseDetail> oldList, List<CoreStockBaseDetail> newList, CoreStockBaseHead headBase) {
        List<CoreStockSkuVo> listVo = new ArrayList<>();
        List<CoreStockBaseDetail> addList = new ArrayList<>();
        List<CoreStockBaseDetail> delList = new ArrayList<>();
        List<CoreStockBaseDetail> updateList = new ArrayList<>();
        Map<String, CoreStockBaseDetail> oldMap = new HashMap<>();
        Map<String, CoreStockBaseDetail> newMap = new HashMap<>();
        int ALTE = SysUtils.getNoteAlte(headBase.getBizType());  //根据单据类型判断库存是正还是负

        //提高效率，先放入Map
        if (null != oldList) {
            for (CoreStockBaseDetail oldItem : oldList) {
                if (oldItem != null) oldMap.put(oldItem.getId(), oldItem);
            }
        }
        if (null != newList) {
            for (CoreStockBaseDetail newItem : newList) {
                if (newItem != null) newMap.put(newItem.getId(), newItem);
            }
        }
        for (Map.Entry<String, CoreStockBaseDetail> entry : oldMap.entrySet()) {
            String mapKey = entry.getKey();
            CoreStockBaseDetail mapValue = entry.getValue();
            //修改后new的数据在新的数据中没有找到再视为删除，否则视为修改
            if (!newMap.containsKey(mapKey)) {
                if (mapKey != null) delList.add(mapValue);    //整理出删除的数据
            }
        }
        for (Map.Entry<String, CoreStockBaseDetail> entry : newMap.entrySet()) {
            if (entry == null) continue;
            String mapKey = entry.getKey();
            CoreStockBaseDetail mapValue = entry.getValue();
            //修改后new的数据在新的数据中没有找到再视为删除，否则视为修改
            if (!oldMap.containsKey(mapKey)) {
                addList.add(mapValue);  //整理出修改的数据
            } else {
                //整理出修改后的差值，便于计算库存
                CoreStockBaseDetail old = oldMap.get(mapKey);
                  //如果入入库，则为1,如果为出库则为-1 ,库存必须以正负数表示换货暂时没有考虑，如果是换货，bizType则应为明细中的
                double val = (mapValue.getQty().doubleValue() * ALTE) - (old.getQty().doubleValue() * ALTE);
                mapValue.setQty(new BigDecimal(val));
                updateList.add(mapValue);
            }
        }
        //把addList和updateList合并一次即可
        //addList.addAll(updateList); //一定不能合并，如果是退货的，注意在前面已经加了辅助变量ALTE,如果再加一次，会取反了
        //本逻辑适合所有退货与入库共用界面，切记前台必须是正数无论是退货或入库
        for (CoreStockBaseDetail item : addList) {
            CoreStockSkuVo stockSkuVo = new CoreStockSkuVo();
            stockSkuVo.setSkuId(item.getSkuId());
            stockSkuVo.setStoreId(headBase.getStoreId());
            stockSkuVo.setOld_qty(new BigDecimal(0* ALTE) );
            stockSkuVo.setNew_qty(new BigDecimal(item.getQty().doubleValue()* ALTE));
            listVo.add(stockSkuVo);
        }

        for (CoreStockBaseDetail item : delList) {
            CoreStockSkuVo stockSkuVo = new CoreStockSkuVo();
            stockSkuVo.setSkuId(item.getSkuId());
            stockSkuVo.setStoreId(headBase.getStoreId());
            stockSkuVo.setOld_qty(new BigDecimal(0 * ALTE));
            stockSkuVo.setNew_qty(new BigDecimal(0 - (item.getQty().doubleValue() * ALTE)));
            listVo.add(stockSkuVo);
        }
        for (CoreStockBaseDetail item : updateList) {
            CoreStockSkuVo stockSkuVo = new CoreStockSkuVo();
            stockSkuVo.setSkuId(item.getSkuId());
            stockSkuVo.setStoreId(headBase.getStoreId());
            stockSkuVo.setOld_qty(new BigDecimal(0) );
            stockSkuVo.setNew_qty(new BigDecimal(item.getQty().doubleValue()));
            listVo.add(stockSkuVo);
        }
        return listVo;
    }

    public static Map<String,List<Object>>  getBizAddDeleteUpdateList(List<Object>oldList, List<Object>newList)
    {

        Map<String,List<Object>> mapResult = new HashMap<>();
        Map<String, Object> oldMap = new HashMap<>();
        Map<String, Object> newMap = new HashMap<>();
        List<Object> addList = new ArrayList<>();
        List<Object> delList = new ArrayList<>();
        List<Object> updateList = new ArrayList<>();
        if (null != oldList) {
            for (Object oldItem : oldList) {
                if (oldItem != null){
                    oldMap.put(SysUtils.getClazzFieldValue(oldItem.getClass(),oldItem,"id").toString(), oldItem);
                }
            }
        }
        if (null != newList) {
            for (Object newItem : newList) {
                if (newItem != null){
                    newMap.put(SysUtils.getClazzFieldValue(newItem.getClass(),newItem,"id").toString(), newItem);
                }
            }
        }

        for (Map.Entry<String, Object> entry : oldMap.entrySet()) {
            String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            //修改后new的数据在新的数据中没有找到再视为删除，否则视为修改
            if (!newMap.containsKey(mapKey)) {
                if (mapKey != null) delList.add(mapValue);    //整理出删除的数据
            }
        }
        for (Map.Entry<String, Object> entry : newMap.entrySet()) {
            String mapKey = entry.getKey();
            Object mapValue = entry.getValue();
            //修改后new的数据在新的数据中没有找到再视为删除，否则视为修改
            if (!oldMap.containsKey(mapKey)) {
                addList.add(mapValue);  //整理出修改的数据
            } else {
                updateList.add(mapValue);
            }
        }

        mapResult.put("NEW",null);
        mapResult.put("DEL",null);
        mapResult.put("UPD",null);

        if(addList !=null && addList.size() > 0) {
            mapResult.put("NEW",addList);
        }

        if(updateList !=null && updateList.size() > 0) {
            mapResult.put("UPD",updateList);
        }

        if(delList !=null && delList.size() > 0) {
            mapResult.put("DEL",delList);
        }
        return mapResult;


    }


}
