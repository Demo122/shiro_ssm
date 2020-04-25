package com.danqing.util;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Description: shiro_ssm
 * Created by danqing on 2020/4/23 21:31
 */
// 需要用户名密码邮件发送实例
//文件名 SendEmail2.java
//本实例以QQ邮箱为例，你需要在qq后台设置

public class SendEmail {
    public static void sendEmailCode(String receiveEmail,String verifyCode)
    {
        // 收件人电子邮箱
        String to = receiveEmail;

        // 发件人电子邮箱
        String from = "gdanqing964@163.com";

        // 指定发送邮件的主机为 smtp.163.com
        String host = "smtp.163.com";  // 邮件服务器


        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        //设置邮件会话参数
        Properties properties = new Properties();

        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        //邮箱发送服务器端口,这里设置为465端口
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.auth", "true");

        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("gdanqing964@163.com", "ROCEUNLHHQVKECBL");
            }
        });
        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);

            // Set From: 头部头字段 发件人昵称和字符集
            message.setFrom(new InternetAddress(from,"权限管理系统","UTF-8"));

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: 头部头字段
            message.setSubject("shiro_ssm权限管理系统登录");

            // 设置消息体
            message.setText("这是邮箱登录验证码："+verifyCode);

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....");
        }catch (MessagingException | UnsupportedEncodingException mex) {
            mex.printStackTrace();
        }
    }
}