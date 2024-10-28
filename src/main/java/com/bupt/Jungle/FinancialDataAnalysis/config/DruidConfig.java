package com.bupt.Jungle.FinancialDataAnalysis.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class DruidConfig {
    private final Boolean testWhileIdle;

    private final String validationQuery;

    @Autowired
    public DruidConfig(@Value("${spring.datasource.test-while-idle}")
                       Boolean testWhileIdle,
                       @Value("${spring.datasource.validation-query}")
                       String validationQuery
    ) {
        this.testWhileIdle = testWhileIdle;
        this.validationQuery = validationQuery;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }
}
