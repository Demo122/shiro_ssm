<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<div class="layui-container" style="margin: 20px">
    <form class="layui-form" action="">
        <div class="layui-form-item">
            <input type="hidden" name="id" value="${user.id}" >
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">用户名</label>
            <div class="layui-input-inline">
                <input type="text" name="name" value="${user.name}" required  lay-verify="username" placeholder="请输入标题" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">密码</label>
            <div class="layui-input-inline">
                <input type="password" name="password" value="${user.password}" required lay-verify="password" placeholder="请输入密码" autocomplete="off" class="layui-input">
            </div>
            <div class="layui-form-mid layui-word-aux"></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">邮箱</label>
            <div class="layui-input-inline">
                <input type="text" name="email" value="${user.email}" lay-verify="eamilVerify" placeholder="请输入邮箱" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">角色</label>
            <div class="layui-input-block">

                <c:forEach items="${roles}" var="role">
                    <input type="checkbox" id="roleId"  value="${role.id}" title="${role.desc_}" lay-skin="primary" >
                </c:forEach>
                <c:forEach items="${userRoles}" var="urole">
                    <input type="checkbox" id="roleId" value="${urole.id}" title="${urole.desc_}" lay-skin="primary" checked>
                </c:forEach>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">启用</label>
            <div class="layui-input-block">
                                                                    <%--  jsp里的el语法，， eq相等      --%>
                <input type="checkbox"   lay-skin="switch" lay-filter="status" lay-text="开启|禁用" ${user.status eq 'true' ? "checked":"" }>
<%--                <input type="checkbox" checked="" name="open" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">--%>

            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">描述</label>
            <div class="layui-input-block">
                <textarea name="description"   placeholder="请输入内容" class="layui-textarea">${user.description}</textarea>
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


        //自定义的验证
        form.verify({
            eamilVerify: function(value, item){
                //value：表单的值、item：表单的DOM对象
                if(value!=""){  //值不是空的时候再去走验证
                    if(!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value)){
                        return '邮箱格式不正确';
                    }
                }
            },
            username: function (value, item) { //value：表单的值、item：表单的DOM对象
                if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
                    return '用户名不能有特殊字符';
                }
                if (/(^\_)|(\__)|(\_+$)/.test(value)) {
                    return '用户名首尾不能出现下划线\'_\'';
                }
                if (/^\d+\d+\d$/.test(value)) {
                    return '用户名不能全为数字';
                }if(/^.{8,}$/.test(value)){
                    return '用户名长度要小于8';
                }
            },
            password: [
                /^[\S]{6,40}$/
                , '密码必须6到40位，且不能出现空格'
            ]

        });


        //定义开关状态变量
        var status=null;
        //监听状态操作
        form.on('switch(status)', function (date) {
            status = this.checked ? 'true':'false';
            // console.log(status);
        });

        //监听提交
        form.on('submit(formDemo)', function (data) {
             // layer.msg(JSON.stringify(data.field));
            //将status添加到data这个json对象
            data.field.status=status;
            // console.log(JSON.stringify(data.field));
            //将页面全部复选框选中的值拼接到一个数组中
            var arr_box = [];
            $("input:checkbox[id='roleId']:checked").each(function() {
                arr_box.push($(this).val());
            });
            //创建roleIds类的json数据对象，
            var roleIdsJSON={"roleIds":arr_box};
            //user类的json数据对象 data.field
            //创建json数据对象
            var jsonData={"user":data.field,"roleIds":roleIdsJSON};
            // layer.msg(JSON.stringify(data.field));
            $.ajax({
                type: "post",
                url: "/user/updateUser",
                data: JSON.stringify(jsonData),
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                success: function (result) {
                    layer.msg(result.msg, {
                            time: 300 //0.5秒关闭（如果不配置，默认是3秒）
                        },
                        function(){
                            //do something
                            //当你在iframe页面关闭自身时
                            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            parent.layer.close(index); //再执行关闭
                        });
                },
                error:function (result) {
                    layer.alert("更新用户失败");
                }
            });

            return false;
        });
    });
</script>
</body>
</html>