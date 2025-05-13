package com.bupt.Jungle.FinancialDataAnalysis.starter.model.response;

import com.bupt.Jungle.FinancialDataAnalysis.common.constant.ResultCodeConstant;
import com.bupt.Jungle.FinancialDataAnalysis.util.GsonUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.io.PrintWriter;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@Schema(name = "向前端返回包装类型")
public class Result<T> {
    @Schema(description = "是否成功")
    private boolean success;

    @Schema(description = "错误信息")
    private String errMessage;

    @Schema(description = "状态码")
    private int code;

    @Schema(description = "数据")
    private T data;

    @Schema(description = "时间戳")
    private long timestamp;

    public Result() {
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(true);
        result.setErrMessage(null);
        result.setCode(ResultCodeConstant.OK);
        result.setData(data);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    public static <T> Result<T> fail(String errMessage) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setErrMessage(errMessage);
        result.setCode(ResultCodeConstant.OK);
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    public static <T> Result<T> error(String errMessage) {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setErrMessage(errMessage);
        result.setCode(ResultCodeConstant.SERVER_ERROR);
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    public static <T> Result<T> notFound() {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setErrMessage(ResultCodeConstant.NOT_FOUND_MESSAGE);
        result.setCode(ResultCodeConstant.NOT_FOUND);
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    public static <T> Result<T> noAuth() {
        Result<T> result = new Result<>();
        result.setSuccess(false);
        result.setErrMessage(ResultCodeConstant.NO_AUTH_MESSAGE);
        result.setCode(ResultCodeConstant.NO_AUTH);
        result.setData(null);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    public static void writeErrorResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write(GsonUtil.beanToJson(noAuth()));
        }
    }
}
