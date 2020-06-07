package org.jeecg;

import org.jeecg.common.util.PasswordUtil;

public class Test {
    public static void main(String args[]){
        String username = "10000-#-admin";
        String password = "123456";
        String salt = "RCGTeGiH";
        String result  = PasswordUtil.encrypt(username, password, salt);
        System.out.println("x=====" + result);
        /*
        String ttt = "10000-#-admin";
        String x[] = ttt.split("-#-");
        System.out.println(x[0] + "*******" + x[1]);
*/
    }
}
