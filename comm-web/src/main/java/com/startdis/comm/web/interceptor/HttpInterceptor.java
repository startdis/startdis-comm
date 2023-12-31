package com.startdis.comm.web.interceptor;

import com.startdis.comm.util.auth.RequestHolder;
import com.startdis.comm.util.auth.TenantHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Startdis
 * @email startdis@dianjiu.cc
 * @desc 主要用于请求结束  清除当前线程相关数据  清除所有 ThreadLocal
 */
@Component
public class HttpInterceptor implements HandlerInterceptor {
    /**
     * This implementation is empty.
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
        TenantHolder.remove();
    }
}
