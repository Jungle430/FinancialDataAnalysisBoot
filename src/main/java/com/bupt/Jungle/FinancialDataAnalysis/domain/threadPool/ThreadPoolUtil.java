package com.bupt.Jungle.FinancialDataAnalysis.domain.threadPool;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class ThreadPoolUtil {
    @Bean(name = "financialAnalysisTaskThreadPool")
    public Executor financialAnalysisTaskThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(2);
        executor.setCorePoolSize(1);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("AnalysisTask-");
        executor.setKeepAliveSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }
}
