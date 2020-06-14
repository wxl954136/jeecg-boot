package org.jeecg;

import lombok.Data;

import java.util.Date;
@Data
public class Demo {
    private String id;
    private String name;
    private Date date;

    public Demo(String id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }
}
