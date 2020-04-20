package com.danqing.pojo;

/**
 * Description: shiro_ssm
 * Created by danqing on 2020/4/20 16:42
 */
public enum ResponseStatusEnum {
    NO_Authority(-1),  //没有授权
    NOT_ENABLE(0),     //被禁用，没有开启
    Do_SUCCESSFUL(1),  //操作成功
    Do_FAIELD(2),     //操作失败
    LOGIN_SUCCESS(100), //登录成功
    LOGIN_FAIL(104);   //登录失败


    private final int status;

    ResponseStatusEnum(int status) {
        this.status=status;
    }

    public int getStatus(){
        return status;
    }
}
