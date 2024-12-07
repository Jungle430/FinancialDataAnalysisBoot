package com.bupt.Jungle.FinancialDataAnalysis.application.user;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.LoginBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.NoAuthException;
import com.bupt.Jungle.FinancialDataAnalysis.domain.service.UserService;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache.CacheService;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.gateway.RedisGateway;
import com.bupt.Jungle.FinancialDataAnalysis.starter.local.UserInfoHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class UserLogService {
    private final UserService userService;

    @Resource(type = RedisGateway.class)
    private CacheService cacheService;

    @Autowired
    public UserLogService(UserService userService) {
        this.userService = userService;
    }

    public LoginBO login(String phone, String password) {
        return userService.login(phone, password);
    }

    public void logout(String token) {
        UserInfoBO userInfoBO = UserInfoHolder.getUserInfoBO();
        if (Objects.isNull(userInfoBO)) {
            log.error("no user's information ,token:{}", token);
            throw new NoAuthException();
        }
        UserInfoHolder.removeUserInfoBO();
        userService.delUserInfoCache(token);
    }
}
