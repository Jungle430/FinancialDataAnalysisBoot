package com.bupt.Jungle.FinancialDataAnalysis.starter.interceptor;

import com.bupt.Jungle.FinancialDataAnalysis.starter.local.UserInfoHolder;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        if (UserInfoHolder.getUserInfoBO() == null) {
            Result.writeErrorResponse(response);
            return false;
        }
        return true;
    }
}