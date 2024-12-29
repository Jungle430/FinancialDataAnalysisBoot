package com.bupt.Jungle.FinancialDataAnalysis.starter.aop;


import com.bupt.Jungle.FinancialDataAnalysis.starter.annotation.Performance;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Method;
import java.util.Arrays;

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
            Method method = extractActualMethod(joinPoint);
            Performance performance = method.getAnnotation(Performance.class);
            boolean timePerformance = performance.timePerformance();
            boolean memoryPerformance = performance.memoryPerformance();
            if (!timePerformance && memoryPerformance) {
                LOGGER.info("Method:{}, Method arguments:{}, Heap memory usage before:{}bytes/{}mb/{}GB,Heap memory usage after:{}bytes/{}mb/{}GB",
                        joinPoint.getSignature().getName(),
                        Arrays.toString(joinPoint.getArgs()),
                        heapMemoryUsageBeforeBytes,
                        String.format("%.5f", heapMemoryUsageBeforeMb),
                        String.format("%.2f", heapMemoryUsageBeforeGb),
                        heapMemoryUsageAfterBytes,
                        String.format("%.5f", heapMemoryUsageAfterMb),
                        String.format("%.2f", heapMemoryUsageAfterGb)
                );
            }

            if (timePerformance && !memoryPerformance) {
                LOGGER.info("Method:{}, Method arguments:{}, execution time: {}ms",
                        joinPoint.getSignature().getName(),
                        Arrays.toString(joinPoint.getArgs()),
                        duration);
            }

            if (timePerformance && memoryPerformance) {
                LOGGER.info("Method {}, Method arguments:{}, execution time: {}ms,Heap memory usage before:{}bytes/{}mb/{}GB,Heap memory usage after:{}bytes/{}mb/{}GB",
                        joinPoint.getSignature().getName(),
                        Arrays.toString(joinPoint.getArgs()),
                        duration,
                        heapMemoryUsageBeforeBytes,
                        String.format("%.5f", heapMemoryUsageBeforeMb),
                        String.format("%.2f", heapMemoryUsageBeforeGb),
                        heapMemoryUsageAfterBytes,
                        String.format("%.5f", heapMemoryUsageAfterMb),
                        String.format("%.2f", heapMemoryUsageAfterGb)
                );
            }
        }

    }

    private Method extractActualMethod(ProceedingJoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        try {
            Method method = ((MethodSignature) signature).getMethod();
            Class<?>[] parameterTypes = method.getParameterTypes();
            return joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), parameterTypes);
        } catch (Exception e) {
            LOGGER.error("can't find method:{}", signature.getName(), e);
            throw e;
        }
    }
}
