// 创建新文件: src/main/java/com/itmk/web/interceptor/AuthenticationInterceptor.java
package com.itmk.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itmk.status.StatusCode;
import com.itmk.utils.ResultUtils;
import com.itmk.utils.ResultVo;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 跳过OPTIONS请求（CORS预检）
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 获取userId参数
        String userId = request.getParameter("userId");

        // 对于需要验证的API
        if (isProtectedEndpoint(request) && (userId == null || userId.isEmpty())) {
            returnUnauthorized(response);
            return false;
        }

        return true;
    }

    private boolean isProtectedEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();

        // 定义需要身份验证的路径
        return path.contains("/api/collect/") ||
                path.contains("/api/goods/getMyUnusedList") ||
                path.contains("/api/goodsOrder/") ||
                path.contains("/api/wxUser/wxupdatePassword") ||
                path.contains("/api/wxUser/editInfo") ||
                path.contains("/api/wxUser/getInfo");
    }

    private void returnUnauthorized(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ResultVo result = ResultUtils.error("请先登录", StatusCode.NO_LOGIN);
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(result));
        out.flush();
        out.close();
    }
}