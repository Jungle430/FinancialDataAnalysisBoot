package com.bupt.Jungle.FinancialDataAnalysis.application.user;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;
import com.bupt.Jungle.FinancialDataAnalysis.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    private final UserService userService;

    @Autowired
    public UserInfoService(UserService userService) {
        this.userService = userService;
    }

    public UserInfoBO getUserInfo(String token) {
        return userService.getUserInfo(token);
    }
}
