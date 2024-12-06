package com.bupt.Jungle.FinancialDataAnalysis.domain.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.LoginBO;

public class LoginAssembler {
    public static LoginBO buildLoginBOFromToken(String token) {
        LoginBO loginBO = new LoginBO();
        loginBO.setToken(token);
        return loginBO;
    }
}
