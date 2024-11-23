package com.bupt.Jungle.FinancialDataAnalysis.util;


import org.apache.commons.lang3.StringUtils;

public final class ToolUtil {
    public static final String PHONE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";

    /**
     * 检验电话号码是否无效
     */
    public static boolean checkPhone(String phone) {
        return StringUtils.isNotBlank(phone) && phone.matches(PHONE_REGEX);
    }

    /*
     * 检查密码长度是否在5-255之间
     */
    public static boolean checkPassword(String password) {
        return StringUtils.isNotBlank(password)
                && password.length() >= 5 && password.length() <= 255;
    }
}
