<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="../../static/layui/css/layui.css">
    <script src="../../static/layui/layui.js"></script>
    <script src="../../static/js/jquery.min.js"></script>


    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
</head>
<body>
<div style="margin-top: 10px;">
    <form class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">用户名</label>
            <div class="layui-input-inline">
                <input type="text" name="name" required lay-verify="username" placeholder="用户名" autocomplete="off"
                       class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">密码框</label>
            <div class="layui-input-inline">
                <input type="password" name="password" required lay-verify="password" placeholder="请输入密码"
                       autocomplete="off" class="layui-input">
            </div>

        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </div>
    </form>
</div>


<script>
    //Demo
    layui.use('form', function () {
        var form = layui.form;

        //自定义验证规则
        form.verify({
            username: function (value, item) { //value：表单的值、item：表单的DOM对象
                if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
                    return '用户名不能有特殊字符';
                }
                if (/(^\_)|(\__)|(\_+$)/.test(value)) {
                    return '用户名首尾不能出现下划线\'_\'';
                }
                if (/^\d+\d+\d$/.test(value)) {
                    return '用户名不能全为数字';
                }if(/^.{0,6}$/.test(value)){
                    return '用户名长度不能小于6'
                }
            }

            //我们既支持上述函数式的方式，也支持下述数组的形式
            //数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
            , password: [
                /^[\S]{6,12}$/
                , '密码必须6到12位，且不能出现空格'
            ]
        });

        //监听提交
        form.on('submit(formDemo)', function (data) {
            // layer.msg(JSON.stringify(data.field));
            $.ajax({
                type: "POST",
                url: "/user/addUser",
                data: JSON.stringify(data.field),
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                success: function (result) {
                    console.log(result.msg);
                    layer.msg(result.msg, {
                            time: 300 //0.5秒关闭（如果不配置，默认是3秒）
                        },
                        function () {
                            //do something
                            //当你在iframe页面关闭自身时
                            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            parent.layer.close(index); //再执行关闭
                        });
                },
                error: function (result) {
                    layer.alert("添加用户失败");
                }
            });

            return false;
        });
    });
</script>
</body>
</html>