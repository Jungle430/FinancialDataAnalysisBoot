package com.bupt.Jungle.FinancialDataAnalysis.exception;

public class CacheServiceException extends RuntimeException {
    public CacheServiceException(String message) {
        super(message);
    }

    public CacheServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
