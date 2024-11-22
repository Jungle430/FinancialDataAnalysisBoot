package com.bupt.Jungle.FinancialDataAnalysis.domain.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.LoginBO;

public class LoginAssembler {
    public static LoginBO buildLoginBOFromToken(String token) {
        LoginBO loginBO = new LoginBO();
        loginBO.setSuccess(true);
        loginBO.setToken(token);
        loginBO.setErrMessage(null);
        return loginBO;
    }

    public static LoginBO buildLoginBOFromErrMessage(String errMessage) {
        LoginBO loginBO = new LoginBO();
        loginBO.setSuccess(false);
        loginBO.setToken(null);
        loginBO.setErrMessage(errMessage);
        return loginBO;
    }
}
