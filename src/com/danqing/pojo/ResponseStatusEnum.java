package com.danqing.pojo;

/**
 * Description: shiro_ssm
 * Created by danqing on 2020/4/20 16:42
 */
public enum ResponseStatusEnum {
    NO_Authority(-1),
    Do_SUCCESSFUL(1),
    Do_FAIELD(2);

    private final int status;

    ResponseStatusEnum(int status) {
        this.status=status;
    }

    public int getStatus(){
        return status;
    }
}
