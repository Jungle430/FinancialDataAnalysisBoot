package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.NoAuthException;
import com.bupt.Jungle.FinancialDataAnalysis.domain.service.UserDomainService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.local.UserInfoHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class UserService {
    private final UserDomainService userDomainService;

    @Autowired
    public UserService(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    public void logout(String token) {
        UserInfoBO userInfoBO = UserInfoHolder.getUserInfoBO();
        if (Objects.isNull(userInfoBO)) {
            log.error("no user's information ,token:{}", token);
            throw new NoAuthException();
        }
        UserInfoHolder.removeUserInfoBO();
        userDomainService.delUserInfoCache(token);
    }

    public UserInfoBO getUserInfo() {
        UserInfoBO userInfoBO = UserInfoHolder.getUserInfoBO();
        if (Objects.isNull(userInfoBO)) {
            throw new NoAuthException();
        }
        return userInfoBO;
    }
}
