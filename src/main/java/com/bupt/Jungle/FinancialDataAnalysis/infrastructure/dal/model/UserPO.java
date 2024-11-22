package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPO {
    // 用户名
    private String username;

    // 用户电话号码
    private String phone;

    // 用户密码
    private String password;

    // 创建账号时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private Timestamp createTime;

    // 更新账号时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private Timestamp updateTime;
}
