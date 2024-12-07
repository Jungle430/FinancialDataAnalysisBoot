package com.bupt.Jungle.FinancialDataAnalysis.domain.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class UserLogConfig {
    private final String prefix;

    private final Long expireTimeInDays;

    @Autowired
    public UserLogConfig(
            @Value("${personal.redis.login.prefix}") String loginPrefix,
            @Value("${personal.redis.login.expiration_time_in_days}") Long loginExpireTimeInDays) {
        this.prefix = loginPrefix;
        this.expireTimeInDays = loginExpireTimeInDays;
    }
}
