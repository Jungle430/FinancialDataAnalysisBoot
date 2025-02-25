package com.bupt.Jungle.FinancialDataAnalysis.common.exception;

import java.util.function.Supplier;

public class BusinessException extends RuntimeException {
    public static final Supplier<BusinessException> NO_FINANCIAL_BRANCH_EXCEPTION = () -> new BusinessException("没有该金融分支");

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Object... args) {
        super(String.format(message, args));
    }
}
