package com.danqing.pojo;

/**
 * Description: shiro_ssm
 * Created by danqing on 2020/4/20 14:08
 */
public class ResponseJSON {
    private int code;
    private String msg;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
