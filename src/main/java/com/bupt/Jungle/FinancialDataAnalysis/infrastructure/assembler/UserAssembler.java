package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.UserPO;

public class UserAssembler {
    public static UserBO UserPO2BO(UserPO userPO) {
        UserBO userBO = new UserBO();
        userBO.setUsername(userPO.getUsername());
        userBO.setPhone(userPO.getPhone());
        userBO.setCreateTime(userPO.getCreateTime());
        userBO.setUpdateTime(userPO.getUpdateTime());
        return userBO;
    }
}
