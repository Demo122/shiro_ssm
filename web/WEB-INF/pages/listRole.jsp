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
    <title>角色管理</title>
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

    </div>
    <div class="layui-row">
        <table class="layui-hide" id="roleTable" lay-filter="demo"></table>
    </div>
</div>

<script type="text/html" id="toolbarDemo">
    <div class="layui-btn-container">
        <button  class="layui-btn layui-btn-sm layui-btn-normal" lay-event="addRolePage">
            <i class="layui-icon"></i>添加角色</button>
    </div>
</script>


<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="editRolePermission">权限授予</a>
    <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
    <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
</script>


<script src="../../static/layui/layui.js" charset="utf-8"></script>
<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>

    layui.use('table', function () {
        var table = layui.table;


        //第一个实例
        table.render({
            elem: '#roleTable'
            , height: 516
            , width: 766
            , toolbar: '#toolbarDemo'
            , defaultToolbar: ['filter', 'exports', 'print']
            , url: '/role/listRole' //数据接口
            , page: true //开启分页
            , request: {
                pageName: 'page' //页码的参数名称，默认：page
                , limitName: 'nums' //每页数据量的参数名，默认：limit
            }
            , cols: [[ //表头
                {type: 'checkbox', width: 80}
                , {field: 'id', title: 'ID', width: 80, sort: true}
                , {field: 'name', title: '角色名称', width: 200}
                , {field: 'desc_', title: '描述', width: 200}
                , {fixed: 'right', title: '操作', width: 200, align: 'center', toolbar: '#barDemo'}
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
            if (obj.event === 'del') {
                layer.confirm('真的删除' + data.name + '么?', function (index) {
                    $.ajax({
                        type: "DELETE",
                        url: "/role/deleteRole",
                        data: JSON.stringify(data.id),
                        dataType: "json",
                        contentType: "application/json;charset=UTF-8",
                        success: function (result) {
                            layer.msg(result.msg);
                            //重载表格
                            table.reload('roleTable');
                        },
                        error: function (result) {
                            layer.alert(result.msg);
                        }
                    });
                    layer.close(index);
                });
            } else if (obj.event === 'edit') {
                //编辑
                layer.open({
                    type: 2,
                    fix: false, //不固定
                    area: ['500px', '300px'],
                    maxmin: true,
                    shadeClose: true,
                    shade: 0.4,
                    title: '编辑角色信息',
                    content: '/role/editRolePage?id=' + data.id,
                    //end - 层销毁后触发的回调
                    end: function () {
                        //重载表格
                        table.reload('roleTable');
                    }
                });
            }else if (obj.event ==='editRolePermission'){
                //权限授予
                layer.open({
                    type: 2,
                    fix: false, //不固定
                    area: ['735px', '550px'],
                    maxmin: true,
                    shadeClose: true,
                    shade: 0.4,
                    title: '角色的权限授予',
                    content: '/role/editRolePermissionPage?id='+data.id,
                    //end - 层销毁后触发的回调
                    end: function () {
                        //重载表格
                        table.reload('roleTable');
                    }
                });
            }
        });

        //工具栏事件
        table.on('toolbar(demo)', function (obj) {
            switch (obj.event) {
                case 'addRolePage':
                    var url = '/role/addRolePage';
                    layer.open({
                        type: 2,
                        fix: false, //不固定
                        area: ['500px', '300px'],
                        maxmin: true,
                        shadeClose: true,
                        shade: 0.4,
                        title: '添加角色',
                        content: url,
                        //end - 层销毁后触发的回调
                        end: function () {
                            //重载表格
                            table.reload('roleTable');
                        }
                    });
            };
        });

    });


</script>

</body>
</html>
