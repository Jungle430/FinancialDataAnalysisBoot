package com.bupt.Jungle.FinancialDataAnalysis.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class UserLogConfig {
    private final String prefix;

    private final Long expireTimeInDays;

    private final String head_token_key;

    @Autowired
    public UserLogConfig(
            @Value("${personal.front.token.head_key}") String head_token_key,
            @Value("${spring.application.name}") String applicationName,
            @Value("${personal.front.token.prefix}") String loginPrefix,
            @Value("${personal.front.token.expiration_time_in_days}") Long loginExpireTimeInDays) {
        this.head_token_key = head_token_key;
        this.prefix = String.format("%s:%s", applicationName, loginPrefix);
        this.expireTimeInDays = loginExpireTimeInDays;
    }
}
