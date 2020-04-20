<%--
  Created by IntelliJ IDEA.
  User: danqing
  Date: 2020/4/20
  Time: 1:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>权限不足</title>
    <link rel="stylesheet" href="../../static/layui/css/layui.css">
    <script src="../../static/js/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
</head>
<body>

<script src="../../static/layui/layui.js"></script>
<script>
    layui.use('layer', function(){
        var layer = layui.layer;
        layer.ready(function () {
            layer.alert(
                "权限不足,具体原因：${ex.message}",
                function () {
                    //do something
                    //当你在iframe页面关闭自身时
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index); //再执行关闭
                });
        });
    });

</script>
</body>

</html>
