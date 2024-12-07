package com.bupt.Jungle.FinancialDataAnalysis.application.user;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.NoAuthException;
import com.bupt.Jungle.FinancialDataAnalysis.starter.local.UserInfoHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserInfoService {

    public UserInfoBO getUserInfo() {
        UserInfoBO userInfoBO = UserInfoHolder.getUserInfoBO();
        if (Objects.isNull(userInfoBO)) {
            throw new NoAuthException();
        }
        return userInfoBO;
    }
}
