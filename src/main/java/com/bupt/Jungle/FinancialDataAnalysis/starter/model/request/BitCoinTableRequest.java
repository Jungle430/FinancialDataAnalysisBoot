package com.bupt.Jungle.FinancialDataAnalysis.starter.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BitCoinTableRequest {
    // 比特币代码
    private String code;

    // 交易平台
    private String platform;

    // 交易地区
    private String region;

    // 交易货币
    private String currency;
}
