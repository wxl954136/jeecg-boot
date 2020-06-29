package org.jeecg.common.api.vo;

import lombok.Data;

import java.util.List;

@Data
public class Status {
    private Boolean success ; //true 成功   false失败
    private String code ; //1:成功  0:不成功
    private String message; //信息提示
    private List<Object> listObject; //特殊对象处理

    /**
     *
     * @param success
     * @param message
     */
    public Status(Boolean success,  String message) {
        this.success = success;
        this.message = message;
    }

    /**
     *
     * @param success
     * @param code
     * @param message
     */
    public Status(Boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

}
