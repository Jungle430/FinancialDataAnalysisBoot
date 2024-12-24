package com.bupt.Jungle.FinancialDataAnalysis.starter.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForexTableRequest {
    // 基础货币所属的地区
    private String baseRegion;

    // 基础货币名称
    private String baseCurrency;

    // 报价货币所属地区
    private String quoteRegion;

    // 报价货币名称
    private String quoteCurrency;
}
