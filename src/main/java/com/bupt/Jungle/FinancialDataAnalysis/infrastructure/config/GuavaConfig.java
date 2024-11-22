package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuavaConfig {
    public final Long SIZE;

    @Autowired
    public GuavaConfig(@Value("${guava.size}") Long SIZE) {
        this.SIZE = SIZE;
    }
}
