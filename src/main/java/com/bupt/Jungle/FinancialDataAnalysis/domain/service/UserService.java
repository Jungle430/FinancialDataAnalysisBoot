package com.bupt.Jungle.FinancialDataAnalysis.domain.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.UserAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.LoginBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache.CacheService;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache.GuavaCacheServiceImpl;
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

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Resource(type = GuavaCacheServiceImpl.class)
    private CacheService cacheService;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginBO login(String phone, String password) {
        if (!ToolUtil.checkPhone(phone)) {
            log.warn("ToolUtil.checkPhone fail, phone:{}", phone);
            return LoginAssembler.buildLoginBOFromErrMessage("电话号码格式错误");
        }

        if (!ToolUtil.checkPassword(password)) {
            log.warn("ToolUtil.checkPassword fail");
            return LoginAssembler.buildLoginBOFromErrMessage("密码格式错误");
        }

        UserBO userBO = userRepository.queryUserByPhoneAndPassword(phone, password);
        if (Objects.isNull(userBO)) {
            return LoginAssembler.buildLoginBOFromErrMessage("没有该用户");
        }

        UserInfoBO userInfoBO = UserAssembler.UserBO2UserInfoBO(userBO);
        String userInfoBOJsonStr = GsonUtil.beanToJson(userInfoBO);
        String token = UUID.randomUUID().toString();
        log.info("cacheService.set, key:{}", token);
        cacheService.set(token, userInfoBOJsonStr);
        return LoginAssembler.buildLoginBOFromToken(token);
    }
}
