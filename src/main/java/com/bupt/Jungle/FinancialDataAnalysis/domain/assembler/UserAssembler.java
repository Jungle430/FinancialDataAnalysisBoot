package com.bupt.Jungle.FinancialDataAnalysis.domain.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.LoginBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.UserPO;

public class UserAssembler {
    public static UserInfoBO UserBO2UserInfoBO(UserBO userBO) {
        UserInfoBO userInfoBO = new UserInfoBO();
        userInfoBO.setUsername(userBO.getUsername());
        userInfoBO.setPhone(userBO.getPhone());
        return userInfoBO;
    }

    public static UserBO UserPO2BO(UserPO userPO) {
        UserBO userBO = new UserBO();
        userBO.setUsername(userPO.getUsername());
        userBO.setPhone(userPO.getPhone());
        userBO.setCreateTime(userPO.getCreateTime());
        userBO.setUpdateTime(userPO.getUpdateTime());
        return userBO;
    }

    public static LoginBO buildLoginBOFromToken(String token) {
        LoginBO loginBO = new LoginBO();
        loginBO.setToken(token);
        return loginBO;
    }
}
