package org.jeecg;




import org.jeecg.modules.utils.SysUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public Test()
    {

    }
    public static void main(String args[]){



    }

    public static void testfs()
    {
        try{
           // Demo demo  = new Demo("13","luke",33);
            Class c1 = Demo.class;

            //创建此Class对象所表示类的一个新实例,
            //newInstance方法调用的是Person的空参数构造方法
            Object o = c1.newInstance();
            System.out.println(o.toString());
        }catch(Exception e){}

        /*
        try {
            Class<?> clazz = Demo.class;
            Object obj = demo;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                if (name.equalsIgnoreCase("id"))
                {
                    field.setAccessible(true); // 私有属性必须设置访问权限
                    Object resultValue = field.get(obj);
                    System.out.println(name + ": " + resultValue);
                }
                // 这里可以编写你的业务代码
            }
        }catch(Exception eg){}
*/

    }
    public static void jpfs(){
        //手动填充密码方式

    }
}
