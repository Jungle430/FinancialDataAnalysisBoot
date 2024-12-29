package com.bupt.Jungle.FinancialDataAnalysis.starter.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

@Aspect
@Component
@Order(3)
public class PerformanceAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceAspect.class);

    @Around(value = "@annotation(com.bupt.Jungle.FinancialDataAnalysis.starter.annotation.Performance)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsageBefore = memoryMXBean.getHeapMemoryUsage();
        long heapMemoryUsageBeforeBytes = heapMemoryUsageBefore.getUsed();
        double heapMemoryUsageBeforeMb = (double) heapMemoryUsageBeforeBytes / (1024 * 1024);
        double heapMemoryUsageBeforeGb = heapMemoryUsageBeforeMb / 1024;
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            MemoryUsage heapMemoryUsageAfter = memoryMXBean.getHeapMemoryUsage();
            long duration = endTime - startTime;
            long heapMemoryUsageAfterBytes = heapMemoryUsageAfter.getUsed();
            double heapMemoryUsageAfterMb = (double) heapMemoryUsageAfterBytes / (1024 * 1024);
            double heapMemoryUsageAfterGb = heapMemoryUsageAfterMb / 1024;
            LOGGER.info("Method {} execution time: {}ms,Heap memory usage before:{}bytes/{}mb/{}GB,Heap memory usage after:{}bytes/{}mb/{}GB",
                    joinPoint.getSignature().getName(), duration,
                    heapMemoryUsageBeforeBytes,
                    heapMemoryUsageBeforeMb,
                    heapMemoryUsageBeforeGb,
                    heapMemoryUsageAfterBytes,
                    heapMemoryUsageAfterMb,
                    heapMemoryUsageAfterGb
            );
        }
    }
}
