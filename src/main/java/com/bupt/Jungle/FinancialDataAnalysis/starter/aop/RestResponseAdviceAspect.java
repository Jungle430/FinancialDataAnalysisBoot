package com.bupt.Jungle.FinancialDataAnalysis.starter.aop;

import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.NoAuthException;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.Result;
import com.bupt.Jungle.FinancialDataAnalysis.util.GsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = {
        "com.bupt.Jungle.FinancialDataAnalysis.starter.controller"
})
@Order(2)
public class RestResponseAdviceAspect implements ResponseBodyAdvice<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseAdviceAspect.class);

    private static final String DEFAULT_ERROR_MESSAGE = "服务器错误，请稍后再试!";

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        if (body instanceof String string) {
            return GsonUtil.beanToJson(Result.ok(string));
        }
        if (body instanceof Result<?> result) {
            return result;
        }
        return Result.ok(body);
    }

    @ExceptionHandler(value = BusinessException.class)
    @Order(1)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(value = NoAuthException.class)
    @Order(1)
    public Result<?> handleNoAuthException(NoAuthException ignored) {
        return Result.noAuth();
    }

    @ExceptionHandler(value = Exception.class)
    @Order(2)
    public Result<?> handleException(Exception exception) {
        LOGGER.error("Exception, {}", exception.getMessage(), exception);
        return Result.error(DEFAULT_ERROR_MESSAGE);
    }
}
