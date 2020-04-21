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
            <input type="hidden" name="id" value="${permission.id}" >
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">权限名称</label>
            <div class="layui-input-inline">
                <input type="text" name="name" value="${permission.name}" required   placeholder="请输入权限名称" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">url</label>
            <div class="layui-input-inline">
                <input type="text" name="url" value="${permission.url}" required  placeholder="请输入url" autocomplete="off" class="layui-input">
            </div>
            <div class="layui-form-mid layui-word-aux"></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">类别</label>
            <div class="layui-input-inline">
                <select name="category" lay-verify="">
                    <option value="">请选择一个类别</option>
                    <option value="用户管理" <c:if test="${'用户管理' eq permission.category}">selected</c:if>>用户管理</option>
                    <option value="角色管理" <c:if test="${'角色管理' eq permission.category}">selected</c:if>>角色管理</option>
                    <option value="权限管理" <c:if test="${'权限管理' eq permission.category}">selected</c:if>>权限管理</option>
                </select>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">菜单</label>
            <div class="layui-input-block">
                <c:if test="${'true' eq permission.menu}">
                    <input type="radio" name="menu" value="true" title="是" checked>
                    <input type="radio" name="menu" value="false" title="否" >
                </c:if>
                <c:if test="${'false' eq permission.menu}">
                    <input type="radio" name="menu" value="true" title="是" >
                    <input type="radio" name="menu" value="false" title="否" checked>
                </c:if>
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">描述</label>
            <div class="layui-input-block">
                <textarea name="desc_"   placeholder="请输入内容" class="layui-textarea">${permission.desc_}</textarea>
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





        //监听提交
        form.on('submit(formDemo)', function (data) {

            // layer.msg(JSON.stringify(data.field));

            $.ajax({
                type: "post",
                url: "/permission/updatePermission",
                data: JSON.stringify(data.field),
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                success: function (result) {
                    layer.msg(result.msg, {
                            time: 500 //0.5秒关闭（如果不配置，默认是3秒）
                        },
                        function(){
                            //do something
                            //当你在iframe页面关闭自身时
                            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            parent.layer.close(index); //再执行关闭
                        });
                },
                error:function (result) {
                    layer.alert("更新权限失败");
                }
            });

            return false;
        });
    });
</script>
</body>
</html>