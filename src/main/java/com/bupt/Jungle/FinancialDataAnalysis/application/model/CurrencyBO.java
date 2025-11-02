package com.bupt.Jungle.FinancialDataAnalysis.application.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyBO {
    /**
     * 货币代码
     */
    private String currencyCode;

    private String simplifiedChineseName;

    private String englishName;
}
