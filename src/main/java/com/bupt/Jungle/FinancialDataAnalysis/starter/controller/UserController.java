package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.user.UserLogService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.LoginAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.request.LoginRequest;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.LoginResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
@Tag(name = "用户")
public class UserController {
    private final UserLogService userService;

    @Autowired
    public UserController(UserLogService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "登录")
    @Parameters({@Parameter(name = "loginRequest", description = "登录信息")})
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return LoginAssembler.LoginBO2Response(userService.login(loginRequest.getPhone(), loginRequest.getPassword()));
    }
}
