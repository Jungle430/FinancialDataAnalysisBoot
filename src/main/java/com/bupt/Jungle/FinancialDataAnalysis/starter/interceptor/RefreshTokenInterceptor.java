package com.bupt.Jungle.FinancialDataAnalysis.starter.interceptor;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.config.UserLogConfig;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache.CacheService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.local.UserInfoHolder;
import com.bupt.Jungle.FinancialDataAnalysis.util.GsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

public class AuthAndRefreshTokenInterceptor implements HandlerInterceptor {
    private final CacheService cacheService;

    private final UserLogConfig userLogConfig;

    private static final String HEADERS_TOKEN_KEY = "X-Token";

    public AuthAndRefreshTokenInterceptor(CacheService cacheService, UserLogConfig userLogConfig) {
        this.cacheService = cacheService;
        this.userLogConfig = userLogConfig;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String token = request.getHeader(HEADERS_TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            return true;
        }

        String key = userLogConfig.getPrefix() + token;
        String s = cacheService.get(token);
        UserInfoBO userInfoBo = GsonUtil.jsonToBean(s, UserInfoBO.class);
        if (Objects.isNull(userInfoBo)) {
            return true;
        }
        UserInfoHolder.saveUserInfoBO(userInfoBo);

        return true;
    }
}
