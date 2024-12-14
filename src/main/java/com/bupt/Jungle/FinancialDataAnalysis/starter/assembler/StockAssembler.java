package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CurrenciesResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.RegionsResponse;

import java.util.List;

public class StockAssembler {
    public static RegionsResponse BuildRegionsResponseFromStrings(List<RegionBO> regions) {
        RegionsResponse regionsResponse = new RegionsResponse();
        regionsResponse.setRegions(regions);
        return regionsResponse;
    }

    public static CurrenciesResponse BuildCurrenciesResponseFromStrings(List<CurrencyBO> currencies) {
        CurrenciesResponse currenciesResponse = new CurrenciesResponse();
        currenciesResponse.setCurrencies(currencies);
        return currenciesResponse;
    }
}
