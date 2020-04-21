<%--
  Created by IntelliJ IDEA.
  User: danqing
  Date: 2020/4/17
  Time: 19:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<head>
    <meta charset="utf-8">
    <title>Layui</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="../../static/layui/css/layui.css" media="all">
    <script src="../../static/js/jquery.min.js"></script>

    <!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->
</head>
<body>

<div class="layui-container layui-col-md10" style="margin-top: 20px;">
    <div class="layui-row">
        <button type="button" class="layui-btn layui-btn-normal" id="addUserPage"><i class="layui-icon"></i>添加用户</button>
    </div>
    <div class="layui-row">
        <table class="layui-hide" id="userTable" lay-filter="demo"></table>
    </div>
</div>
<script type="text/html" id="toolbarDemo">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm " lay-event="openUser">开启选中</button>
        <button class="layui-btn layui-btn-sm layui-btn layui-btn-warm" lay-event="forbidUser">禁用选中</button>
        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="deleteAllUser">删除选中</button>
    </div>
</script>

<script type="text/html" id="switchTpl">
    <!-- 这里的 checked 的状态只是演示 -->
    <input type="checkbox" name="status" user_id="{{d.id}}" id="checkstatus" lay-skin="switch" lay-text="开启|禁用" lay-filter="status" {{
           d.status== true ? 'checked' : '' }}>
</script>


<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看角色</a>
    <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>


<script src="../../static/layui/layui.js" charset="utf-8"></script>
<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>

    layui.use('table', function () {
        var table = layui.table, form = layui.form;


        //第一个实例
        table.render({
            elem: '#userTable'
            , height: 516
            , width: 1422
            , toolbar: '#toolbarDemo'
            , defaultToolbar: ['filter', 'exports', 'print']
            , url: '/user/listUser' //数据接口
            , page: true //开启分页
            , request: {
                pageName: 'page' //页码的参数名称，默认：page
                , limitName: 'nums' //每页数据量的参数名，默认：limit
            }
            , cols: [[ //表头
                {type: 'checkbox'}
                , {field: 'id', title: 'ID', width: 80, sort: true}
                , {field: 'name', title: '用户名', width: 120}
                , {field: 'password', title: '密码', width: 300}
                , {field: 'email', title: '邮箱', width: 230}
                , {field: 'status', title: '开启', width: 100, templet: '#switchTpl'}
                , {field: 'description', title: '描述', width: 150}
                , {field: 'activeCode', title: '激活码', width: 150}
                , {fixed: 'right', width: 230, align: 'center', toolbar: '#barDemo'}
            ]]
            , parseData: function (res) { //res 即为原始返回的数据
                return {
                    "code": res.code,
                    "msg": res.msg,
                    "count": res.count,
                    "data": res.data //解析数据列表
                };
            }
        });


        //监听工具条
        table.on('tool(demo)', function (obj) {
            var data = obj.data;
            if (obj.event === 'detail') {
                //查看权限
                var url = "/user/getUserRoles/" + data.id;
                $.get(url, function (result) {

                    layer.open({
                        title: data.name + "拥有的角色",
                        content: result
                    });
                });

                // layer.msg('ID：' + data.id + ' 的查看操作');
            } else if (obj.event === 'del') {
                //删除
                layer.confirm('真的删除 ' + data.name + " 吗？", function (index) {
                    $.ajax({
                        type: "DELETE",
                        url: "/user/deleteUser",
                        data: JSON.stringify(data.id),
                        dataType: "json",
                        contentType: "application/json;charset=UTF-8",
                        success: function (result) {
                            if (result.code==1) {
                                layer.alert("删除成功");
                                obj.del();
                            }
                            //无权限，或者删除失败
                            layer.alert(result.msg);
                        },
                        error: function (result) {
                            layer.alert("删除失败");
                        }
                    });
                    layer.close(index);
                });
            } else if (obj.event === 'edit') {
                //编辑
                layer.open({
                    type: 2,
                    fix: false, //不固定
                    area: ['700px', '500px'],
                    maxmin: true,
                    shadeClose: true,
                    shade: 0.4,
                    title: '编辑用户信息',
                    content: '/user/editUserPage/' + data.id,
                    //end - 层销毁后触发的回调
                    end: function () {
                        //重载表格
                        table.reload('userTable');
                    }
                });
            }
        });

        //工具栏事件
        table.on('toolbar(demo)', function (obj) {
            var checkStatus = table.checkStatus(obj.config.id);
            var data = checkStatus.data;
            var ids = new Array();
            //获取选中行的id
            for (var i = 0; i < data.length; i++) {
                ids.push(data[i].id);
            }
            switch (obj.event) {
                case 'openUser':
                    //设置status的值控制开启或者禁用
                    var jsonDate={"ids":ids,"status":"true"};
                    $.ajax({
                        type: "POST",
                        url: "/user/updateSelectUserStatus",
                        data: JSON.stringify(jsonDate),
                        dataType: "json",
                        contentType: "application/json;charset=UTF-8",
                        success: function (res) {
                            //如果没有权限  或者
                            if(res.code==-1){
                                layer.alert(res.msg);
                            }
                            //更新成功
                            if (res.code==1){
                                layer.alert("开启成功！");
                                //重载表格
                                table.reload('userTable');
                            }
                            //开启失败
                            if(res.code==2){
                                layer.alert("开启失败！");
                            }
                        },
                        error: function (result) {
                            layer.alert("开启失败！");
                        }
                    });
                    //重载表格
                    table.reload('userTable');
                    break;
                case 'forbidUser':
                    //禁用选中
                    // layer.alert(JSON.stringify(ids));
                    var jsonDate={"ids":ids,"status":"false"};
                    $.ajax({
                        type: "POST",
                        url: "/user/updateSelectUserStatus",
                        data: JSON.stringify(jsonDate),
                        dataType: "json",
                        contentType: "application/json;charset=UTF-8",
                        success: function (res) {
                            //如果没有权限
                            if(res.code==-1){
                                layer.alert(res.msg);
                            }
                            //禁用成功
                            if (res.code==1){
                                layer.alert("禁用成功！");
                                //重载表格
                                table.reload('userTable');
                            }
                            // 禁用失败
                            if(res.code==2){
                                layer.alert("禁用失败！");
                            }
                        },
                        error: function (result) {
                            layer.alert("禁用失败！");
                        }
                    });
                    //重载表格
                    table.reload('userTable');
                    break;
                case 'deleteAllUser':
                    //删除选中
                    $.ajax({
                        type: "DELETE",
                        url: "/user/deleteSelectUser",
                        data: JSON.stringify(ids),
                        dataType: "json",
                        contentType: "application/json;charset=UTF-8",
                        success: function (result) {
                            if (result.code==1) {
                                layer.alert("删除成功");
                                //重载表格
                                table.reload('userTable');
                            }
                            if(result.code==2){
                                layer.alert("删除失败");
                            }
                        },
                        error: function (result) {
                            layer.alert("删除失败");
                        }
                    });
                    //重载表格
                    table.reload('userTable');
                    break;
            };
        });

        //监听状态操作
        form.on('switch(status)', function (obj) {
            var id = $(this).attr("user_id");
            var status = obj.elem.checked;
            var jsonData = {"id": id, "status": status};
            //直接发送的jsond对象 其实就是form表单提交参数
            $.post(
                "/user/updateUserStatus",
                jsonData,function (res) {
                    //如果没有权限  或者   更新失败
                    if(res.code==-1 || res.code==2){
                        layer.alert(res.msg);
                        //重载表格
                        table.reload('userTable');
                    }
                    //更新成功
                    if (res.code==1){
                        layer.msg(res.msg);
                        if (obj.elem.checked) {
                            layer.tips("已开启", obj.othis);
                        } else {
                            layer.tips("已关闭", obj.othis);
                        }
                    }
                }
            );

        });

        $(function () {
            $("#addUserPage").click(function () {
                layer.open({
                    type: 2,
                    fix: false, //不固定
                    area: ['500px', '300px'],
                    maxmin: true,
                    shadeClose: true,
                    shade: 0.4,
                    title: '添加用户',
                    // content: '../../static/pages/addUser.jsp'
                    content: '/user/addUserPage',
                    //end - 层销毁后触发的回调
                    end: function () {
                        //重载表格
                        table.reload('userTable');
                    }
                });
            });

        });

    });


</script>

</body>
</html>
