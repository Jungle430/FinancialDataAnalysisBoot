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
        String token = request.getHeader(HEADERS_TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            return true;
        }

        String key = userLogConfig.getPrefix() + token;
        UserInfoBO userInfoBo = GsonUtil.jsonToBean(cacheService.get(token), UserInfoBO.class);
        if (Objects.isNull(userInfoBo)) {
            return true;
        }
        UserInfoHolder.saveUserInfoBO(userInfoBo);
        cacheService.expire(key, userLogConfig.getExpireTimeInDays(), TimeUnit.DAYS);
        return true;
    }
}
