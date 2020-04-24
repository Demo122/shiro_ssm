package com.danqing.util;

import java.util.Random;

/**
 * Description: shiro_ssm
 * Created by danqing on 2020/4/23 21:25
 */
public class GenerateVerifyCode {

    public static String getVerifyCode() {

        String str="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb=new StringBuilder(6);
        for(int i=0;i<6;i++)
        {
            char ch=str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
}
