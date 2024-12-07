package com.bupt.Jungle.FinancialDataAnalysis.domain.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.UserAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.LoginBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.config.UserLogConfig;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.NoAuthException;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache.CacheService;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.gateway.RedisGateway;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.repository.UserRepository;
import com.bupt.Jungle.FinancialDataAnalysis.domain.assembler.LoginAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.util.GsonUtil;
import com.bupt.Jungle.FinancialDataAnalysis.util.ToolUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Resource(type = RedisGateway.class)
    private CacheService cacheService;

    private final UserLogConfig userLogConfig;

    @Autowired
    public UserService(UserRepository userRepository, UserLogConfig userLogConfig) {
        this.userRepository = userRepository;
        this.userLogConfig = userLogConfig;
    }

    public LoginBO login(String phone, String password) {
        if (!ToolUtil.checkPhone(phone)) {
            log.warn("ToolUtil.checkPhone fail, phone:{}", phone);
            throw new BusinessException("电话号码格式错误");
        }

        if (!ToolUtil.checkPassword(password)) {
            log.warn("ToolUtil.checkPassword fail");
            throw new BusinessException("密码格式错误");
        }

        UserBO userBO = userRepository.queryUserByPhone(phone);
        if (Objects.isNull(userBO)) {
            throw new BusinessException("没有该用户");
        }

        userBO = userRepository.queryUserByPhoneAndPassword(phone, password);
        if (Objects.isNull(userBO)) {
            throw new BusinessException("密码错误");
        }

        UserInfoBO userInfoBO = UserAssembler.UserBO2UserInfoBO(userBO);
        String userInfoBOJsonStr = GsonUtil.beanToJson(userInfoBO);
        String token = UUID.randomUUID().toString();
        log.info("cacheService.set start, key:{}", token);
        cacheService.set(userLogConfig.getPrefix() + token, userInfoBOJsonStr, userLogConfig.getExpireTimeInDays(), TimeUnit.DAYS);
        log.info("cacheService.set end, key:{}", token);
        return LoginAssembler.buildLoginBOFromToken(token);
    }

    public void delUserInfoCache(String token) {
        log.info("cacheService.del start, key:{}", token);
        boolean success = cacheService.del(userLogConfig.getPrefix() + token);
        if (!success) {
            log.error("no user's information in cache, token: {}", token);
            throw new NoAuthException();
        }
        log.info("cacheService.del end, key:{}", token);
    }
}
