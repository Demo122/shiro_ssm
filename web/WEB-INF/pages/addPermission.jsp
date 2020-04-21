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
<div style="margin: 20px;">
    <form class="layui-form" action="">
        <div class="layui-form-item">
            <label class="layui-form-label">权限名称</label>
            <div class="layui-input-inline">
                <input type="text" name="name" id="permissionName"  lay-verify="required|permissionName" placeholder="权限名称" autocomplete="off"
                       class="layui-input">
            </div>
            <div class="layui-form-mid " id="permissionName_status"></div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">访问链接</label>
            <div class="layui-input-inline">
                <input type="text" name="url" id="url" required placeholder="访问链接"
                       lay-verify="required|permission_url"    autocomplete="off" class="layui-input">
            </div>
            <div class="layui-form-mid " id="url_status"></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">类别</label>
            <div class="layui-input-inline">
                <select name="category" lay-verify="">
                    <option value="">请选择一个类别</option>
                    <option value="用户管理">用户管理</option>
                    <option value="角色管理">角色管理</option>
                    <option value="权限管理">权限管理</option>
                </select>
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">菜单</label>
            <div class="layui-input-block">
                <input type="radio" name="menu" value="true" title="是">
                <input type="radio" name="menu" value="false" title="否" checked>
            </div>
        </div>
        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">描述</label>
            <div class="layui-input-block">
                <textarea name="desc_" placeholder="请输入内容" class="layui-textarea"></textarea>
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
        var permissionName_status=0;
        var url_status=0;

        //自定义验证规则
        form.verify({
            permissionName: function (value, item) { //value：表单的值、item：表单的DOM对象
                if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
                    return '权限名不能有特殊字符';
                }
                if (/(^\_)|(\__)|(\_+$)/.test(value)) {
                    return '权限名首尾不能出现下划线\'_\'';
                }
                if (/^\d+\d+\d$/.test(value)) {
                    return '权限名不能全为数字';
                }
                if (!/^.{0,12}$/.test(value)) {
                    return '权限名长度不能大于12'
                }
            },
            permission_url: function (value, item) { //value：表单的值、item：表单的DOM对象
                if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
                    return '权限名不能有特殊字符';
                }
                if (/[\u4e00-\u9fa5]/.test(value)) {
                    return 'url中不能包含中文';
                }
            }
        });

        //监听提交
        form.on('submit(formDemo)', function (data) {
            // layer.msg(JSON.stringify(data.field));
            //权限名和url都可用才能提交
            if(permissionName_status==1&&url_status==1){
                $.ajax({
                    type: "POST",
                    url: "/permission/addPermission",
                    data: JSON.stringify(data.field),
                    dataType: "json",
                    contentType: "application/json;charset=UTF-8",
                    success: function (result) {
                        // console.log(result.msg);
                        layer.msg(result.msg, {
                                time: 500 //0.5秒关闭（如果不配置，默认是3秒）
                            },
                            function () {
                                //do something
                                //当你在iframe页面关闭自身时
                                var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index); //再执行关闭
                            });
                    },
                    error: function (result) {
                        layer.alert(result.msg);
                    }
                });
            }
            if(permissionName_status==2){
                layer.alert("权限名已存在！");
            }
            if (url_status==2){
                layer.alert("url已存在！");
            }


            return false;
        });


        $(function () {
            //监听键盘弹起事件，查询权限名是否重复
            $("#permissionName").change(function () {
                var name=$('#permissionName').val();
                var data={"permissionName":name};
                $.ajax({
                    type: "POST",
                    url:"/permission/checkPermissionName/",
                    data:data,
                    success:function (result) {
                        if (result.code==1){
                            permissionName_status=result.code;
                            layer.msg(result.msg);
                            $("#permissionName_status").css({"color":"green"});
                            $('#permissionName_status').html(result.msg);
                        }
                        if (result.code==2){
                            permissionName_status=result.code;
                            layer.msg(result.msg);
                            $("#permissionName_status").css({"color":"red"});
                            $('#permissionName_status').html(result.msg);
                        }
                    }
                });
            });

            //监听键盘弹起事件，查询url是否重复
            $("#url").change(function () {
                var name=$('#url').val();
                var data={"url":name};
                $.ajax({
                    type: "POST",
                    url:"/permission/checkUrl/",
                    data:data,
                    success:function (result) {
                        if (result.code==1){
                            url_status=result.code;
                            layer.msg(result.msg);
                            $("#url_status").css({"color":"green"});
                            $('#url_status').html(result.msg);
                        }
                        if (result.code==2){
                            url_status=result.code;
                            layer.msg(result.msg);
                            $("#url_status").css({"color":"red"});
                            $('#url_status').html(result.msg);
                        }
                    }
                });
            });
        });


    });
</script>
</body>
</html>