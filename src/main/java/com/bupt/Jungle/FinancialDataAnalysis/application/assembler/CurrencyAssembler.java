package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.util.CurrencyUtil;

public class CurrencyAssembler {
    public static CurrencyBO buildCurrencyBOFromCurrencyCode(String currencyCode) {
        return CurrencyBO
                .builder()
                .currencyCode(currencyCode)
                .simplifiedChineseName(CurrencyUtil.getSimplifiedCurrencyChineseName(currencyCode))
                .englishName(CurrencyUtil.getCurrencyEnglishName(currencyCode))
                .build();
    }
}