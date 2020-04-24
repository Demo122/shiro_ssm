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
    <script src="../../static/js/jquery.cookie.js" ></script>

</head>
<body class="login-bg">

<div class="login">
    <div class="message">后台管理</div>
    <div class="layui-tab layui-tab-brief" lay-filter="test1">
        <ul class="layui-tab-title">
            <li class="layui-this" lay-id="111">账户密码登录</li>
            <li lay-id="222">邮箱验证码登录</li>
        </ul>
        <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">
                <form class="layui-form" method="post">
                    <input name="name" placeholder="用户名 | 邮箱" value="11@qq.com" type="text" lay-verify="required"
                           class="layui-input">
                    <hr class="hr15">
                    <input name="password" lay-verify="required" placeholder="密码" value="12345" type="password"
                           class="layui-input">
                    <hr class="hr15">
                    <input class="loginin" value="登录" lay-submit lay-filter="login" style="width:100%;" type="submit">
                    <hr class="hr20">
                </form>
            </div>
            <div class="layui-tab-item">
                <form class="layui-form">
                    <input name="email" id="email" placeholder=" 邮箱" type="text" lay-verify="required"
                           class="layui-input">
                    <hr class="hr15">
                    <div class="layui-row">
                        <div class="layui-col-md6">
                            <input name="activeCode" lay-verify="required" placeholder="验证码" type="text"
                                   class="layui-input">
                        </div>
                        <div class="layui-col-md5 layui-col-md-offset1">
                            <input type="button" id="sendVerifyCode" value="发送验证码">
                        </div>
                    </div>

                    <hr class="hr15">
                    <input class="loginin" value="登录" lay-submit lay-filter="loginByEmailVerifyCode" style="width:100%;"
                           type="submit">
                    <hr class="hr20">
                </form>
            </div>
        </div>
    </div>

</div>

<script type="text/javascript">

    layui.extend({
        admin: '{/}../../static/js/admin'
    });
    layui.use(['form', 'admin', 'element'], function () {
        var form = layui.form;
        var element = layui.element;


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
                    if (result.code == -2) {
                        layer.alert(result.msg);
                    }
                }
            });
            return false;
        });

        form.on('submit(loginByEmailVerifyCode)', function (data) {
            //传的是 email 和 activeCode 字段
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
                    if (result.code == -2) {
                        //账户不存在
                        layer.alert(result.msg);
                    }
                }
            });
            return false;
        });


        //获取hash来切换选项卡，假设当前地址的hash为lay-id对应的值
        var layid = location.hash.replace(/^#test1=/, '');
        element.tabChange('test1', layid); //假设当前地址为：http://a.com#test1=222，那么选项卡会自动切换到“发送消息”这一项

        //监听Tab切换，以改变地址hash值
        element.on('tab(test1)', function () {
            location.hash = 'test1=' + this.getAttribute('lay-id');
        });

        $(function () {
            $('#sendVerifyCode').click(function () {
                var email = $('#email').val();
                var verifyEmail = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
                if (email == '') {
                    layer.alert('邮箱不能为空', {
                        time: 800
                    });
                    return this;
                } else if (!verifyEmail.test(email)) {
                    layer.alert('邮箱格式有误！', {
                        time: 800
                    });
                    return this;
                }
                $.ajax({
                    type: "post",
                    url: "sendVerifyCode",
                    data: {"email": email},
                    success: function (res) {
                        if (res.code == -2) {
                            //没有用户
                            layer.alert(res.msg);
                        }
                        if (res.code == 1) {
                            //成功
                            layer.msg(res.msg, {
                                time: 500
                            });
                        }
                        if (res.code == 2) {
                            //失败
                            layer.alert(res.msg);
                        }
                        //把倒计时存入cookie
                        $.cookie("total",60);
                        timekeeping();
                    }
                });
            });

            if ($.cookie("total") != undefined && $.cookie("total") != 'NaN' && $.cookie("total") != 'null') {//cookie存在倒计时
                timekeeping();
            } else {//cookie 没有倒计时
                $('#sendVerifyCode').attr("disabled", false);
            }

            function timekeeping() {
                //把按钮设置为不可以点击
                $('#sendVerifyCode').attr("disabled", true);
                var interval = setInterval(function () {//每秒读取一次cookie
                    //从cookie 中读取剩余倒计时
                    var total = $.cookie("total");
                    //在发送按钮显示剩余倒计时
                    $('#sendVerifyCode').val('请等待' + total + '秒');
                    //把剩余总倒计时减掉1
                    total--;
                    if (total == 0) {//剩余倒计时为零，则显示 重新发送，可点击
                        //清除定时器
                        clearInterval(interval);
                        //删除cookie
                        total = $.cookie("total", total, {expires: -1});
                        //显示重新发送
                        $('#sendVerifyCode').val('重新发送');
                        //把发送按钮设置为可点击
                        $('#sendVerifyCode').attr("disabled", false);
                    } else {//剩余倒计时不为零
                        //重新写入总倒计时
                        $.cookie("total", total);
                    }
                }, 1000);
            }

        });
    });

</script>
<!-- 底部结束 -->
</body>
</html>