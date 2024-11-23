package com.bupt.Jungle.FinancialDataAnalysis.starter.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    // 用户名
    private String username;

    // 用户手机号码
    private String phone;
}
