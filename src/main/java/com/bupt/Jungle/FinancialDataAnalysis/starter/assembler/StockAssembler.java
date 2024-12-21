package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CurrenciesResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.RegionsResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.StockTagPageResponse;

import java.util.List;

public class StockAssembler {
    public static RegionsResponse buildRegionsResponseFromRegionBOs(List<RegionBO> regions) {
        RegionsResponse regionsResponse = new RegionsResponse();
        regionsResponse.setRegions(regions);
        return regionsResponse;
    }

    public static CurrenciesResponse buildCurrenciesResponseFromCurrencyBOs(List<CurrencyBO> currencies) {
        CurrenciesResponse currenciesResponse = new CurrenciesResponse();
        currenciesResponse.setCurrencies(currencies);
        return currenciesResponse;
    }

    public static StockTagPageResponse StockTagPageBO2StockTagsPageResponse(StockTagPageBO stockTagPageBO) {
        return StockTagPageResponse.builder()
                .stockTags(stockTagPageBO.getStockTags())
                .total(stockTagPageBO.getTotal())
                .build();
    }
}
