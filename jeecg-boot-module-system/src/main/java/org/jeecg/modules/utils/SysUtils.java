package org.jeecg.modules.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.shiro.vo.DefContants;
import org.jeecg.modules.system.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
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

    public static String getUsername(String gsdm,String usersign)
    {
        return gsdm.trim() + "-#-" + usersign.trim();
    }

    /**
     * 通过username拆解获得gsdm,主要是为了适配shiro验证
     * @param username
     * @return
     */
    public String getUsernameOfGsdm(String username)
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
    public String getUsernameOfName(String username)
    {
        String arr[]  = username.split("-#-");
        if (arr.length !=2) return null;
        return arr[1];
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
}
