package com.bupt.Jungle.FinancialDataAnalysis.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoBO {
    // 用户名
    private String username;

    // 用户手机号码
    private String phone;
}
