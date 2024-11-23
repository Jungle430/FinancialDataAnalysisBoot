package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.Result;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.UserInfoResponse;

import java.util.Objects;

public class UserAssembler {
    public static Result<UserInfoResponse> UserInfoBO2Response(UserInfoBO userBO) {
        if (Objects.isNull(userBO)) {
            return Result.fail("没有该用户");
        }
        UserInfoResponse response = new UserInfoResponse();
        response.setUsername(userBO.getUsername());
        response.setPhone(userBO.getPhone());
        return Result.ok(response);
    }
}
