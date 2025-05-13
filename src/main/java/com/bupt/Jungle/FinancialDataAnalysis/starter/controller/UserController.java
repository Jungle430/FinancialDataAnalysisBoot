package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.service.UserService;
import com.bupt.Jungle.FinancialDataAnalysis.common.config.UserLogConfig;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.domain.service.UserDomainService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.UserAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.request.LoginRequest;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.LoginResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "用户")
@Slf4j
public class UserController {
    private final UserService userService;

    private final UserDomainService userDomainService;

    private final String HEADERS_TOKEN_KEY;

    @Autowired
    public UserController(UserService userService,
                          UserDomainService userDomainService,
                          UserLogConfig userLogConfig) {
        this.userService = userService;
        this.userDomainService = userDomainService;
        this.HEADERS_TOKEN_KEY = userLogConfig.getHead_token_key();
    }

    @PostMapping("/login")
    @Operation(summary = "登录")
    @Parameters({@Parameter(name = "loginRequest", description = "登录信息")})
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return UserAssembler.LoginBO2Response(userDomainService.login(loginRequest.getPhone(), loginRequest.getPassword()));
    }

    @GetMapping("/info")
    @Operation(summary = "实时获取用户信息")
    @Parameters({@Parameter(name = "X-Token", description = "本地存储的token", in = ParameterIn.HEADER)})
    public UserInfoResponse info() {
        return UserAssembler.UserInfoBO2Response(userService.getUserInfo());
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    @Parameters({@Parameter(name = "X-Token", description = "本地存储的token", in = ParameterIn.HEADER)})
    public void logout(@NonNull HttpServletRequest request) {
        String token = request.getHeader(HEADERS_TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            log.error("token is empty");
            throw new BusinessException("token为空!");
        }
        userService.logout(token);
    }
}
