package com.bupt.Jungle.FinancialDataAnalysis.application.user;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.LoginBO;
import com.bupt.Jungle.FinancialDataAnalysis.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLogService {
    private final UserService userService;

    @Autowired
    public UserLogService(UserService userService) {
        this.userService = userService;
    }

    public LoginBO login(String phone, String password) {
        return userService.login(phone, password);
    }

    public boolean logout(String token) {
        return userService.delUserInfoCache(token);
    }
}
