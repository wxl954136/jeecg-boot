package org.jeecg;

import org.jeecg.common.util.PasswordUtil;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Test {
    public static void main(String args[]){

        BigDecimal x = new BigDecimal(10);
        double y = x.doubleValue() * -1;
        BigDecimal z = new BigDecimal(y);
        System.out.println(z);




    }
    public static void jpfs(){
        //手动填充密码方式
        String username = "10000-#-admin";
        String password = "123456";
        String salt = "RCGTeGiH";
        String result  = PasswordUtil.encrypt(username, password, salt);
        System.out.println("x=====" + result);
    }
}
