# shiro_ssm
使用ssm+shiro+layui的权限管理系统 
[演示地址](http://47.115.62.234:8080)

# 讲解
1. 开发工具 ide为idea2019 数据库为mysql5.5 
2. 后端使用了spring+springmvc+mybatis框架 
3. 安全框架使用了shiro 自定义了两个登录验证的realm
    一个用来验证 邮箱|用户名 的密码登录
    一个用来验证邮箱验证码登录
    还有一个url过滤器用来过滤请求，进行权限校验
4. 前端界面使用了layui框架 和juquery
5. 本项目除了少数几个页面是直接请求jsp，大部分都是通过ajax进行json格式的数据交换
    稍微改动就可以改成前后端分离的项目

# 功能
## 登录模块
使用使用用户名或者邮箱进行账户密码登录
也可以使用使用邮箱进行验证码登录
## 用户管理 角色管理 权限管理
1. 可以对用户，权限，角色进行增删改查，在进行添加和修改的时候会对用户名，邮箱地址，权限名，权限url，角色名等进行后端校验，保证唯一
2. 可以对用户进行搜索，搜索后进行操作，这里前端主要都是写layui的操作

## 角色和权限授予
1. 对用户授予角色，对角色授予权限
2. 访问没有权限的请求会返回无权限提示



