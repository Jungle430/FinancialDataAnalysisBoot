package com.bupt.Jungle.FinancialDataAnalysis.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForexTagBO {
    // 基础货币所属的地区
    private RegionBO baseRegion;

    // 基础货币信息
    private CurrencyBO baseCurrency;

    // 报价货币所属地区
    private RegionBO quoteRegion;

    // 报价货币信息
    private CurrencyBO quoteCurrency;
}
