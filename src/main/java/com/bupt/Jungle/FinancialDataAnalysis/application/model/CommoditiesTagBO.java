package com.bupt.Jungle.FinancialDataAnalysis.application.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommoditiesTagBO {
    // 大宗商品代码
    private String code;

    // 交易平台
    private String platform;

    // 交易地区
    private RegionBO region;

    // 交易货币
    private CurrencyBO currency;

    // 大宗商品名称
    private String name;
}
