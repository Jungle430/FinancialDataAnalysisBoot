package com.bupt.Jungle.FinancialDataAnalysis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bupt.Jungle.FinancialDataAnalysis.mapper")
public class FinancialDataAnalysisBoot {

    public static void main(String[] args) {
        SpringApplication.run(FinancialDataAnalysisBoot.class, args);
    }

}
