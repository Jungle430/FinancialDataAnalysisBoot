package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.LoginBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.LoginResponse;

public class LoginAssembler {
    public static LoginResponse LoginBO2Response(LoginBO loginBO) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(loginBO.getToken());
        return loginResponse;
    }
}
