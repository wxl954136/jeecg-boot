package org.jeecg.modules.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.shiro.vo.DefContants;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysCommonMapper;
import org.jeecg.modules.system.model.SysNoteBizNoVo;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SysUtils {



    public static boolean izNewNote(String billNo) {
        boolean result = false;
        if (StringUtils.isEmpty(billNo) ||
                StringUtils.isBlank(billNo) ||
                SysStatusEnum.NOTE_NEW.getValue().equalsIgnoreCase(billNo)
        ) {
            result =  true;
        }

        return result;
    }

    /**
     *
     * @param sysCommonMapper 通用mapper
     * @param tableName 表名
     * @param bizTypeVal  单据类型
     * @return
     */
    public static synchronized  String getNewNoteNo(SysCommonMapper sysCommonMapper,String tableName,String bizTypeVal){


//生成新单
        SysNoteBizNoVo bizNoVo = new SysNoteBizNoVo();
        bizNoVo.setTableName(tableName);
        bizNoVo.setFieldBizType("biz_type");
//        bizNoVo.setFieldBizTypeVal(SysStatusEnum.NOTE_PO_IN.getValue());
        bizNoVo.setFieldBizTypeVal(bizTypeVal);
        bizNoVo.setFieldGsdm("gsdm");
        bizNoVo.setFieldGsdmVal(SysUtils.getLoginUser().getGsdm());
        String note_no = sysCommonMapper.getNoteLastBizNo(bizNoVo);

        String timestamp = new SimpleDateFormat("yyyyMMdd").format( new Date());
        String notePrefix = bizTypeVal + timestamp;
        String rSerial = "001";
        String rNoteNo = notePrefix + rSerial;
        if (!oConvertUtils.isEmpty(note_no) && note_no.indexOf(notePrefix)>=0)
        {
            String  serial = note_no.substring(notePrefix.length());
            int iSerial = Integer.parseInt(serial);
            DecimalFormat df = new DecimalFormat("0000");
            iSerial = iSerial + 1;
            String sSerial = df.format(iSerial);
            rNoteNo = notePrefix + sSerial;
        }
        return rNoteNo;
    }

    public static String getUsername(String gsdm,String usersign)
    {
        return gsdm.trim() + "-#-" + usersign.trim();
    }

    /**
     * 通过username拆解获得gsdm,主要是为了适配shiro验证
     * @param username
     * @return
     */
    public static String getUsernameOfGsdm(String username)
    {
        String arr[]  = username.split("-#-");
        if (arr.length !=2) return null;
        return arr[0];
    }

    /**
     * 通过username拆解获得name,主要是为了适配shiro验证
     * @param username
     * @return
     */
    public static  String getUsernameOfName(String username)
    {
        String arr[]  = username.split("-#-");
        if (arr.length !=2) return null;
        return arr[1];
    }

    /**
     * 获取递增版本号
     * @param updateCount
     * @return
     */
    public static Integer getUpdateCount(Integer updateCount){

        if (updateCount == null ) updateCount = 1 ;
        else
        {
            updateCount = updateCount + 1;
        }
        return updateCount ;
    }

    /**
     * 如果是入库，则将库存以正数计算，如果是出库，则为负数，如果是收款为正，如果是付款则为负-
     * @param bizType
     * @return
     */
    public static Integer getNoteAlte( String bizType){
        Integer ALTE = 1;
        if(SysStatusEnum.NOTE_PO_IN.getValue().equalsIgnoreCase(bizType))
        {
            ALTE = 1;
        }
        if(SysStatusEnum.NOTE_PO_BACK.getValue().equalsIgnoreCase(bizType))
        {
            ALTE = -1;
        }
        return ALTE ;
    }


    public static SysUser getSysUserFromRedis(HttpServletRequest request,RedisUtil redisUtil)
    {
        //获取的SysUser对象
        String token = request.getHeader(DefContants.X_ACCESS_TOKEN);
        SysUser loginUser = (SysUser)redisUtil.get(token);
        return loginUser;
    }
    public static LoginUser getLoginUser()
    {
        return (LoginUser) SecurityUtils.getSubject().getPrincipal();
    }

    public static Map getNormalRequstMap(HttpServletRequest request) {
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }
    public static Object getClazzFieldValue(Class clazz,Object object,String fieldName)
    {
        Object result = null;
        try {
            Class<?> clazzr = clazz;
            Object obj = object;
            Field[] fields = clazzr.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                if (name.equalsIgnoreCase(fieldName))
                {
                    field.setAccessible(true); // 私有属性必须设置访问权限
                    result = field.get(obj);
                    break;
                }
                // 这里可以编写你的业务代码
            }
        }catch(Exception eg){}
        return result;
    }
}
