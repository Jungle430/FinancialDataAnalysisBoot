package com.bupt.Jungle.FinancialDataAnalysis.starter.threadLocal;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserInfoBO;

public class UserInfoHolder {
    private static final ThreadLocal<UserInfoBO> threadLocal = new ThreadLocal<>();

    public static void saveUserInfoBO(UserInfoBO userInfoBO) {
        threadLocal.set(userInfoBO);
    }

    public static UserInfoBO getUserInfoBO() {
        return threadLocal.get();
    }

    public static void removeUserInfoBO() {
        threadLocal.remove();
    }
}
