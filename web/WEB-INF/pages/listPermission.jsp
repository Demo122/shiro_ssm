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
    <title>权限管理</title>
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
        <button type="button" class="layui-btn layui-btn-normal" id="addPermissionPage"><i class="layui-icon"></i>添加权限
        </button>
    </div>
    <div class="layui-row">
        <table class="layui-hide" id="permissionTable" lay-filter="demo"></table>
    </div>
</div>

<script type="text/html" id="toolbarDemo">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm layui-btn-danger" lay-event="deleteSelectPermission">全部删除</button>
    </div>
</script>


<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>
<script type="text/html" id="menu">
    {{#  if(!d.menu){ }}
    <span style="color: #F581B1;">否</span>
    {{#  } else { }}
        是
    {{#  } }}
</script>


<script src="../../static/layui/layui.js" charset="utf-8"></script>
<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>

    layui.use('table', function () {
        var table = layui.table;


        //第一个实例
        table.render({
            elem: '#permissionTable'
            , height: 516
            , width: 1340
            , toolbar: '#toolbarDemo'
            , defaultToolbar: ['filter', 'exports', 'print']
            , url: '/permission/listPermission' //数据接口
            , page: true //开启分页
            , request: {
                pageName: 'page' //页码的参数名称，默认：page
                , limitName: 'nums' //每页数据量的参数名，默认：limit
            }
            , cols: [[ //表头
                {type: 'checkbox', width: 80}
                , {field: 'id', title: 'ID', width: 80, sort: true}
                , {field: 'name', title: '名称', width: 200}
                , {field: 'category', title: '类别', width: 150}
                , {field: 'url', title: '访问链接', width: 300}
                , {field: 'menu', title: '是否菜单', width: 120, sort: true, templet: '#menu'}
                , {field: 'desc_', title: '描述', width: 200}
                , {fixed: 'right', width: 200, align: 'center', toolbar: '#barDemo'}
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
                        url: "/permission/deletePermission",
                        data: JSON.stringify(data.id),
                        dataType: "json",
                        contentType: "application/json;charset=UTF-8",
                        success: function (result) {
                            layer.msg(result.msg);
                            //重载表格
                            table.reload('permissionTable');
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
                    area: ['700px', '500px'],
                    maxmin: true,
                    shadeClose: true,
                    shade: 0.4,
                    title: '编辑权限信息',
                    content: '/permission/editPermissionPage/' + data.id,
                    //end - 层销毁后触发的回调
                    end: function () {
                        //重载表格
                        table.reload('permissionTable');
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
                case 'deleteSelectPermission':
                    var jsonDate={"ids":ids};
                    $.ajax({
                        type: "DELETE",
                        url: "/permission/deleteSelectPermission",
                        data: JSON.stringify(jsonDate),
                        dataType: "json",
                        contentType: "application/json;charset=UTF-8",
                        success: function (result) {
                            if (result.code==1) {
                                layer.alert("删除成功");
                                //重载表格
                                table.reload('permissionTable');
                            }
                            if(result.code==2){
                                layer.alert("删除失败");
                            }
                        },
                        error: function (result) {
                            layer.alert("删除失败");
                        }
                    });
            };
        });

        $(function () {
            $("#addPermissionPage").click(function () {
                var url = '/permission/addPermissionPage';
                layer.open({
                    type: 2,
                    fix: false, //不固定
                    area: ['700px', '500px'],
                    maxmin: true,
                    shadeClose: true,
                    shade: 0.4,
                    title: '添加权限',
                    // content: '../../static/pages/addUser.jsp'
                    content: url,
                    //end - 层销毁后触发的回调
                    end: function () {
                        //重载表格
                        table.reload('permissionTable');
                    }
                });
            });

        });

    });


</script>

</body>
</html>
