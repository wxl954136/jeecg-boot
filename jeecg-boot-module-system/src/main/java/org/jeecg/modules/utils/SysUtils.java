package org.jeecg.modules.utils;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.shiro.vo.DefContants;
import org.jeecg.modules.system.entity.SysUser;

import javax.servlet.http.HttpServletRequest;

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
}
