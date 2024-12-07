package com.bupt.Jungle.FinancialDataAnalysis.starter.config;

import com.bupt.Jungle.FinancialDataAnalysis.common.config.UserLogConfig;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache.CacheService;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.gateway.RedisGateway;
import com.bupt.Jungle.FinancialDataAnalysis.starter.interceptor.RefreshTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource(type = RedisGateway.class)
    private CacheService cacheService;

    private final UserLogConfig userLogConfig;

    @Autowired
    public InterceptorConfig(UserLogConfig userLogConfig) {
        this.userLogConfig = userLogConfig;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(
                new RefreshTokenInterceptor(cacheService, userLogConfig)
        ).order(0);
    }
}
