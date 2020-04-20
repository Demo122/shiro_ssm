package com.danqing.filter;

import com.alibaba.fastjson.JSON;
import com.danqing.pojo.ResponseJSON;
import com.danqing.pojo.ResponseStatusEnum;
import com.danqing.service.PermissionService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class URLPathMatchingFilter extends PathMatchingFilter {
    @Autowired
    PermissionService permissionService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        String requestURI = getPathWithinApplication(request);

        System.out.println("requestURI:" + requestURI);

        Subject subject = SecurityUtils.getSubject();
        // 如果没有登录，就跳转到登录页面
        if (!subject.isAuthenticated()) {
            WebUtils.issueRedirect(request, response, "/login");
            return false;
        }

        // 看看这个路径权限里有没有维护，如果没有维护，一律放行(也可以改为一律不放行)
        boolean needInterceptor = permissionService.needInterceptor(requestURI);
        if (!needInterceptor) {
            //不在权限表
            return true;
        } else {
            //在权限表
            boolean hasPermission = false;
            String userName = subject.getPrincipal().toString();
            Set<String> permissionUrls = permissionService.listPermissionURLs(userName);
            for (String url : permissionUrls) {
                // 这就表示当前用户有这个权限
                if (url.equals(requestURI)) {
                    hasPermission = true;
                    break;
                }
            }

            if (hasPermission)
                return true;
            else {
                UnauthorizedException ex = new UnauthorizedException("当前用户没有访问路径 " + requestURI + " 的权限");

                //获取请求头，判断是不是ajax请求
                HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                String header = httpServletRequest.getHeader("X-Requested-With");
                boolean isAjax = "XMLHttpRequest".equals(header) ? true:false;
//                System.out.println("是不是ajax："+isAjax);

                //ajax请求就返回json
                if (isAjax){
//                    Map<String,Object> resJSON=new HashMap<>();
//                    resJSON.put("msg",ex.getMessage());
//                    resJSON.put("code",-1);
                    ResponseJSON resJSON=new ResponseJSON();
                    resJSON.setCode(ResponseStatusEnum.NO_Authority.getStatus());
                    resJSON.setMsg(ex.getMessage());

                    response.setContentType("application/json; charset=utf-8");//返回json
                    response.getWriter().write(JSON.toJSONString(resJSON));
                }else{
                    //普通请求就重定向到 unauthorized.jsp
                    subject.getSession().setAttribute("ex", ex);
                    WebUtils.issueRedirect(request, response, "/unauthorized");
                }


                return false;
            }

        }

    }
}