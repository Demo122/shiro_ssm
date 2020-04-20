<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>管理员登录-WeAdmin Frame型后台管理系统-WeAdmin 1.0</title>
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="shortcut icon" href="../../static/images/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="../../static/css/font.css">
    <link rel="stylesheet" href="../../static/css/weadmin.css">
    <script src="../../static/layui/layui.js"></script>
    <script src="../../static/js/jquery.min.js"></script>

</head>
<body class="login-bg">

<div class="login">
    <div class="message">后台管理</div>
    <div id="darkbannerwrap"></div>

    <form class="layui-form" method="post" action="login">
        <input name="name" placeholder="用户名" value="zhang3" type="text" lay-verify="required" class="layui-input">
        <hr class="hr15">
        <input name="password" lay-verify="required" placeholder="密码" value="12345" type="password" class="layui-input">
        <hr class="hr15">
        <input class="loginin" value="登录" lay-submit lay-filter="login" style="width:100%;" type="submit">
        <hr class="hr20">
    </form>
</div>

<script type="text/javascript">

    layui.extend({
        admin: '{/}../../static/js/admin'
    });
    layui.use(['form', 'admin'], function () {
        var form = layui.form;
        form.on('submit(login)', function (data) {
            $.ajax({
                type: "post",
                url: "login",
                data: JSON.stringify(data.field),
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                success: function (result) {
                    //code==100 登录成功
                    if (result.code == 100) {
                        // 我们可以利用http的重定向来跳转 window.location.replace(result.url)
                        window.location.replace(result.url);
                    }
                    //code==104 登录失败
                    if (result.code == 104) {
                        //  使用href来跳转
                        layer.alert(result.msg, function () {
                            window.location.href = result.url;
                        });
                    }
                    //code==0 被禁用
                    if (result.code == 0) {
                        layer.alert(result.msg);
                    }
                }
            });
            return false;
        })
    });

</script>
<!-- 底部结束 -->
</body>
</html>