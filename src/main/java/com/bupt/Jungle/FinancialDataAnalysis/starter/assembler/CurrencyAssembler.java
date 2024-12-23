package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CurrenciesResponse;

import java.util.List;

public class CurrencyAssembler {
    public static CurrenciesResponse buildCurrenciesResponseFromCurrencyBOs(List<CurrencyBO> currencies) {
        CurrenciesResponse currenciesResponse = new CurrenciesResponse();
        currenciesResponse.setCurrencies(currencies);
        return currenciesResponse;
    }
}
