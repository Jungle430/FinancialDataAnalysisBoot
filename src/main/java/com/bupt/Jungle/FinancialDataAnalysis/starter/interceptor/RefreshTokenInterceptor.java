package com.bupt.Jungle.FinancialDataAnalysis.starter.interceptor;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.config.UserLogConfig;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache.CacheService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.local.UserInfoHolder;
import com.bupt.Jungle.FinancialDataAnalysis.util.GsonUtil;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final CacheService cacheService;

    private final UserLogConfig userLogConfig;

    private final String HEADERS_TOKEN_KEY;

    public RefreshTokenInterceptor(CacheService cacheService, UserLogConfig userLogConfig) {
        this.cacheService = cacheService;
        this.userLogConfig = userLogConfig;
        this.HEADERS_TOKEN_KEY = userLogConfig.getHead_token_key();
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(HEADERS_TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            return true;
        }

        String key = userLogConfig.getPrefix() + token;
        String value = cacheService.get(key);
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        UserInfoBO userInfoBO;
        try {
            userInfoBO = Objects.requireNonNull(
                    GsonUtil.jsonToBean(value, UserInfoBO.class)
            );
        } catch (NullPointerException | JsonSyntaxException ignore) {
            return true;
        }

        UserInfoHolder.saveUserInfoBO(userInfoBO);
        cacheService.expire(key, userLogConfig.getExpireTimeInDays(), TimeUnit.DAYS);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        UserInfoHolder.removeUserInfoBO();
    }
}
