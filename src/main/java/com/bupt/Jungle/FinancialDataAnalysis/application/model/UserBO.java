package com.bupt.Jungle.FinancialDataAnalysis.application.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBO {
    // 用户名
    private String username;

    // 用户电话号码
    private String phone;

    // 创建账号时间
    private Timestamp createTime;

    // 更新账号时间
    private Timestamp updateTime;
}
