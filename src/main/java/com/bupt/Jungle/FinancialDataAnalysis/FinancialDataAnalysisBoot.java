package com.bupt.Jungle.FinancialDataAnalysis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper")
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
public class FinancialDataAnalysisBoot {

    public static void main(String[] args) {
        SpringApplication.run(FinancialDataAnalysisBoot.class, args);
    }

}
