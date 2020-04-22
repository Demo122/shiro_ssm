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
<div class="layui-container" style="margin: 10px">
    <form class="layui-form" action="">
        <div class="layui-form-item">
            <input type="hidden" name="id" value="${role.id}" >
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">角色名称</label>
            <div class="layui-input-inline">
                <input type="text" name="name" id="rolename" value="${role.name}" required   placeholder="请输入角色名称" autocomplete="off" class="layui-input">
            </div>
            <div class="layui-form-mid " id="roleName_status"></div>

        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">角色描述</label>
            <div class="layui-input-inline">
                <input type="text" name="desc_" value="${role.desc_}" required  placeholder="请输入描述" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
                <button type="reset" class="layui-btn layui-btn-primary" id="rest">重置</button>
            </div>
        </div>
    </form>
</div>


<script>
    //Demo
    layui.use('form', function () {
        var form = layui.form;
        var roleName_status=0;

        //监听提交
        form.on('submit(formDemo)', function (data) {
            // layer.msg(JSON.stringify(data.field));
            if(roleName_status==1){
                $.ajax({
                    type: "post",
                    url: "/role/updateRole",
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
                        layer.alert("更新角色失败");
                    }
                });
            }
            if (roleName_status==2){
                layer.alert("该角色名已存在！");
            }

            return false;
        });

        //监听键盘弹起事件，查询用户名是否重复
        $(function () {
            //原始名字
            var originalName=$('#rolename').val();
            // alert(originalName);
            $("#rolename").change(function () {
                var name=$('#rolename').val();
                var data={"rolename":name};
                if(name==originalName){
                    console.log("没有修改!");
                    roleName_status=1;
                }else {
                    $.ajax({
                        type: "POST",
                        url:"/role/checkRolename",
                        data:data,
                        success:function (result) {
                            if (result.code==1){
                                roleName_status=result.code;
                                layer.msg(result.msg);
                                $("#roleName_status").css({"color":"green"});
                                $('#roleName_status').html(result.msg);
                            }
                            if (result.code==2){
                                roleName_status=result.code;
                                layer.msg(result.msg);
                                $("#roleName_status").css({"color":"red"});
                                $('#roleName_status').html(result.msg);
                            }
                        }
                    });
                }

            });

            $("#rest").click(function () {
                roleName_status=1;
                $('#roleName_status').html("");

            });
        });
    });
</script>
</body>
</html>