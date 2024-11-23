package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.user.UserInfoService;
import com.bupt.Jungle.FinancialDataAnalysis.application.user.UserLogService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.LoginAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.UserAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.request.LoginRequest;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.LoginResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.Result;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
@Tag(name = "用户")
public class UserController {
    private final UserLogService userLogService;

    private final UserInfoService userInfoService;

    @Autowired
    public UserController(UserLogService userService, UserInfoService userInfoService) {
        this.userLogService = userService;
        this.userInfoService = userInfoService;
    }

    @PostMapping("/login")
    @Operation(summary = "登录")
    @Parameters({@Parameter(name = "loginRequest", description = "登录信息")})
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return LoginAssembler.LoginBO2Response(userLogService.login(loginRequest.getPhone(), loginRequest.getPassword()));
    }

    @GetMapping("/info/{token}")
    @Operation(summary = "实时获取用户信息")
    @Parameters({@Parameter(name = "token", description = "本地存储的token")})
    public Result<UserInfoResponse> info(@PathVariable(name = "token") @NonNull String token) {
        return UserAssembler.UserInfoBO2Response(userInfoService.getUserInfo(token));
    }
}
