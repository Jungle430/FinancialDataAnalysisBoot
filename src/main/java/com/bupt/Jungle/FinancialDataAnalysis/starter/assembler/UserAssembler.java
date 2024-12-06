package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.UserInfoResponse;

import java.util.Objects;

public class UserAssembler {
    public static UserInfoResponse UserInfoBO2Response(UserInfoBO userBO) {
        if (Objects.isNull(userBO)) {
            throw new BusinessException("没有该用户!请重新登录!");
        }
        UserInfoResponse response = new UserInfoResponse();
        response.setUsername(userBO.getUsername());
        response.setPhone(userBO.getPhone());
        return response;
    }
}
