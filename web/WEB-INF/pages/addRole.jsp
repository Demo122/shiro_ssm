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
            <label class="layui-form-label">角色名称</label>
            <div class="layui-input-inline">
                <input type="text" name="name" id="rolename" lay-verify="required" placeholder="请输入角色名称" autocomplete="off"
                       class="layui-input">
            </div>
            <div class="layui-form-mid " id="roleName_status"></div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">描述</label>
            <div class="layui-input-inline">
                <input type="text" name="desc_"  lay-verify="required" placeholder="请输入角色描述"
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
        var roleName_status=0;

        //监听提交
        form.on('submit(formDemo)', function (data) {
            // layer.msg(JSON.stringify(data.field));
            //用户可用才能提交添加
            if(roleName_status==1){
                $.ajax({
                    type: "POST",
                    url: "/role/addRole",
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
                        layer.alert("添加角色失败");
                    }
                });
            }
            if (roleName_status==2){
                layer.alert("角色名已存在！");
            }

            return false;
        });


        //监听键盘弹起事件，查询用户名是否重复
        $(function () {
            $("#rolename").change(function () {
                var name=$('#rolename').val();
                var data={"rolename":name};
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
            });
        });

    });

</script>
</body>
</html>