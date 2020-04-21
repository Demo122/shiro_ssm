package com.danqing.pojo;

import java.util.Arrays;

/**
 * Description: shiro_ssm
 * Created by danqing on 2020/4/19 1:10
 */
public class Ids {
    private long[] ids;

    public long[] getIds() {
        return ids;
    }

    public void setIds(long[] ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "Ids{" +
                "ids=" + Arrays.toString(ids) +
                '}';
    }
}
