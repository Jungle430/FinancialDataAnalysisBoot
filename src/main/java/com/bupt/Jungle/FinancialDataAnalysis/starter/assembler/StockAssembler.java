package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CurrenciesResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.RegionsResponse;

import java.util.List;

public class StockAssembler {
    public static RegionsResponse BuildRegionsResponseFromStrings(List<String> regions) {
        RegionsResponse regionsResponse = new RegionsResponse();
        regionsResponse.setRegions(regions);
        return regionsResponse;
    }

    public static CurrenciesResponse BuildCurrenciesResponseFromStrings(List<String> currencies) {
        CurrenciesResponse currenciesResponse = new CurrenciesResponse();
        currenciesResponse.setCurrencies(currencies);
        return currenciesResponse;
    }
}
