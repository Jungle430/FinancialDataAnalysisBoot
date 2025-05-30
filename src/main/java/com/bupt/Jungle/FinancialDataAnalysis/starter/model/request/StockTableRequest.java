package com.bupt.Jungle.FinancialDataAnalysis.starter.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTableRequest {
    // 股票代码
    private String code;

    // 交易平台
    private String platform;

    // 公司所在地区
    private String region;

    // 交易货币
    private String currency;

    // 公司名称
    private String name;

    // 交易地区
    private String marketRegion;
}
