package com.bupt.Jungle.FinancialDataAnalysis.starter.aop;

import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.NoAuthException;
import com.bupt.Jungle.FinancialDataAnalysis.util.GsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class ExceptionHandlerLoggerAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerLoggerAspect.class);

    @Around(value = "execution(* com.bupt.Jungle.FinancialDataAnalysis.starter.controller.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (NoAuthException noAuthException) {
            LOGGER.error("NoAuthException caught in method: {}， Method arguments: {}, Exception message: {}",
                    joinPoint.getSignature().getName(),
                    GsonUtil.beanToJson(joinPoint.getArgs()),
                    noAuthException.getMessage(),
                    noAuthException);
            throw noAuthException;
        } catch (BusinessException businessException) {
            LOGGER.error("BusinessException caught in method: {}， Method arguments: {}, Exception message: {}",
                    joinPoint.getSignature().getName(),
                    GsonUtil.beanToJson(joinPoint.getArgs()),
                    businessException.getMessage(),
                    businessException);
            throw businessException;
        } catch (Exception exception) {
            LOGGER.error("Exception caught in method: {}， Method arguments: {}, Exception Type: {}, Exception message: {}",
                    joinPoint.getSignature().getName(),
                    GsonUtil.beanToJson(joinPoint.getArgs()),
                    exception.getClass().getName(),
                    exception.getMessage(),
                    exception);
            throw exception;
        }
    }
}

