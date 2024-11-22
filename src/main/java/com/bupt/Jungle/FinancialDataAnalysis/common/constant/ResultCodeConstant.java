package com.bupt.Jungle.FinancialDataAnalysis.common.constant;

public final class ResultCodeConstant {
    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     */
    public static final int OK = 200;

    /**
     * {@code 404 Not Found} (HTTP/1.0 - RFC 1945)
     */
    public static final int NOT_FOUND = 404;

    public static final String NOT_FOUND_MESSAGE = "404 Not Found";

    /**
     * {@code 500 Server Error} (HTTP/1.0 - RFC 1945)
     */
    public static final int SERVER_ERROR = 500;

    /**
     * {@code 510 no auth}
     */
    public static final int NO_AUTH = 510;

    public static final String NO_AUTH_MESSAGE = "No authentication, need login";
}
