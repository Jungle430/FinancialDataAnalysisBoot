package com.bupt.Jungle.FinancialDataAnalysis.starter.aop;

import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.NoAuthException;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.Result;
import com.bupt.Jungle.FinancialDataAnalysis.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(1)
public class ExceptionHandlerAspect {
    public static final String DEFAULT_EXCEPTION_ERROR_MESSAGE = "服务器内部错误,请稍后重试!";

    @Around(value = "execution(* com.bupt.Jungle.FinancialDataAnalysis.starter.controller.*.*(..))")
    public Result<?> around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object proceed = joinPoint.proceed();
            return proceed instanceof Result<?> result ? result : Result.ok(proceed);
        } catch (NoAuthException noAuthException) {
            log.error("NoAuthException caught in method: {}， Method arguments: {}, Exception message: {}",
                    joinPoint.getSignature().getName(), GsonUtil.beanToJson(joinPoint.getArgs()), noAuthException.getMessage());
            return Result.noAuth();
        } catch (BusinessException businessException) {
            log.error("BusinessException caught in method: {}， Method arguments: {}, Exception message: {}",
                    joinPoint.getSignature().getName(), GsonUtil.beanToJson(joinPoint.getArgs()), businessException.getMessage());
            return Result.fail(businessException.getMessage());
        } catch (Exception exception) {
            log.error("Exception caught in method: {}， Method arguments: {}, Exception message: {}",
                    joinPoint.getSignature().getName(), GsonUtil.beanToJson(joinPoint.getArgs()), exception.getMessage());
            return Result.error(DEFAULT_EXCEPTION_ERROR_MESSAGE);
        }
    }
}

