<%--
  Created by IntelliJ IDEA.
  User: danqing
  Date: 2020/4/22
  Time: 0:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>穿梭框组件</title>
    <link rel="stylesheet" href="../../static/layui/css/layui.css">
    <script src="../../static/layui/layui.js"></script>
    <script src="../../static/js/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
</head>
<body>
<div>
    <input type="hidden" id="id" value="${role.id}" >
    <input type="hidden" id="name" value="${role.name}" >
    <input type="hidden" id="desc_" value="${role.desc_}" >
</div>

<div style="margin: 20px"  id="editRolePermission"></div>
<script>
    layui.use('transfer', function(){
        var transfer = layui.transfer;
        //获取角色信息
        var id=$("#id").val();
        var name=$("#name").val();
        var desc_=$("#desc_").val();
        var role={"id":id,"name":name,"desc_":desc_};

        var allPermission=new Array();
        var rolePermission=new Array();
        var title=new Array();
        title[0]="未拥有的权限";
        title[1]=name+"的权限";

        //一加载就发送ajax请求数据


        $(function () {
            $.ajax({
               type:"post",
               url:"/role/editRolePermission",
               data: JSON.stringify(role),
               dataType: "json",
               contentType: "application/json;charset=UTF-8",
               success:function (result) {
                   //所有权限
                   allPermission=result.ps;
                   //当前角色的所有权限id
                   rolePermission=result.rolePermIds;
                   // console.log(allPermission);

                   //穿梭时的回调
                   transfer.render({
                       elem: '#editRolePermission'
                       ,width: 300
                       ,height: 400
                       ,title: title
                       ,showSearch: true
                       ,data: allPermission
                       ,value: rolePermission
                       ,parseData: function(allPermission){
                           return {
                               "value": allPermission.id //数据值
                               ,"title": allPermission.name //数据标题
                               ,"disabled": allPermission.disabled  //是否禁用
                               ,"checked": allPermission.checked //是否选中
                           }
                       }
                       ,onchange: function(obj, index){
                          // var arr = ['左边', '右边'];  index=0,左边，1右边
                           var selectIds =[];
                           //获取选中行的id
                           for (var i = 0; i < obj.length; i++) {
                               selectIds.push(obj[i].value);
                           }
                           var ids={"ids":selectIds};
                           var data={"role":role,"ids":ids};
                           var method=null;
                           if (index==0){
                               //左边发出 进行角色权限添加 将method设为put
                               method="PUT";
                           }
                           if (index==1){
                               //右边发出 进行角色权限删除 将method设为delete
                               method="DELETE";
                           }
                           //添加角色权限
                           $.ajax({
                               type: method,
                               url:"/role/updataRolePermission",
                               data:JSON.stringify(data),
                               dataType: "json",
                               contentType: "application/json;charset=UTF-8",
                               success:function (res) {
                                   layer.msg(res.msg);
                               }
                           });

                       }
                       ,text: {
                           none: '无数据' //没有数据时的文案
                           ,searchNone: '无匹配数据' //搜索无匹配数据时的文案
                       }
                   });
                }
            });
        });



    });
</script>
</body>
</html>
