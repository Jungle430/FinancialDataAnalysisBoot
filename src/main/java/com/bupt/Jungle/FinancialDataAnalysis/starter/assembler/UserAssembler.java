package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.LoginBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.LoginResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.UserInfoResponse;

public class UserAssembler {
    public static UserInfoResponse UserInfoBO2Response(UserInfoBO userBO) {
        UserInfoResponse response = new UserInfoResponse();
        response.setUsername(userBO.getUsername());
        response.setPhone(userBO.getPhone());
        return response;
    }

    public static LoginResponse LoginBO2Response(LoginBO loginBO) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(loginBO.getToken());
        return loginResponse;
    }
}
