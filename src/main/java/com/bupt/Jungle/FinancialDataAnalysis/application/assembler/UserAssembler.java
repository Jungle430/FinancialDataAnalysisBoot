package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;

public class UserAssembler {
    public static UserInfoBO UserBO2UserInfoBO(UserBO userBO) {
        UserInfoBO userInfoBO = new UserInfoBO();
        userInfoBO.setUsername(userBO.getUsername());
        userInfoBO.setPhone(userBO.getPhone());
        return userInfoBO;
    }
}
