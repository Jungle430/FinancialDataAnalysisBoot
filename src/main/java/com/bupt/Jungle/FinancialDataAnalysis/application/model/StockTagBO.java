package com.bupt.Jungle.FinancialDataAnalysis.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockTagBO {
    // 股票代码
    private String code;

    // 交易平台
    private String platform;

    // 公司所在地区
    private RegionBO region;

    // 交易货币
    private CurrencyBO currency;

    // 公司名称
    private String name;

    // 交易地区
    private RegionBO marketRegion;
}
