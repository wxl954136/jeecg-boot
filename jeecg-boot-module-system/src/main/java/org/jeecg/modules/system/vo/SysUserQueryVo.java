package org.jeecg.modules.system.vo;

import lombok.Data;
import org.jeecg.modules.system.entity.SysUser;

/**
 * @Author luke
 * @Date 2020/5/31 21:58
 * @Description: 与用户有关的查询，多条件，抽取出来
 * @Version 1.0
 */
@Data
public class SysUserQueryVo {
    private String username;
    private String password;
    private String gsdm;
    private String userfullname;
    public SysUserQueryVo(){

    }
    public SysUserQueryVo(String username,String gsdm)
    {
        this.username = username;
        this.gsdm = gsdm;

    }


}
