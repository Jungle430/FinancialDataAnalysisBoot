package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.LoginBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.LoginResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.Result;

public class LoginAssembler {
    public static Result<LoginResponse> LoginBO2Response(LoginBO loginBO) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setSuccess(loginBO.getSuccess());
        loginResponse.setToken(loginBO.getToken());
        loginResponse.setErrMessage(loginBO.getErrMessage());
        return loginResponse.getSuccess() ?
                Result.ok(loginResponse) : Result.fail(loginBO.getErrMessage());
    }
}
