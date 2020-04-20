package com.danqing.pojo;

import java.util.Arrays;

/**
 * Description: shiro_ssm
 * Created by danqing on 2020/4/19 1:10
 */
public class Ids {
    private long[] Ids;

    public long[] getIds() {
        return Ids;
    }

    public void setIds(long[] ids) {
        Ids = ids;
    }

    @Override
    public String toString() {
        return "Ids{" +
                "Ids=" + Arrays.toString(Ids) +
                '}';
    }
}
