package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.StockIndexPO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.StockPO;

import java.util.Collection;

public class StockAssembler {
    public static StockTagBO StockPO2StockTagBO(StockPO stockPO) {
        return StockTagBO.builder()
                .code(stockPO.getCode())
                .platform(stockPO.getPlatform())
                .region(RegionAssembler.buildRegionBOFromISOCode(stockPO.getRegion()))
                .currency(CurrencyAssembler.buildCurrencyBOFromCurrencyCode(stockPO.getCurrency()))
                .name(stockPO.getName())
                .marketRegion(RegionAssembler.buildRegionBOFromISOCode(stockPO.getMarketRegion()))
                .build();
    }

    public static StockIndexTagBO StockIndexPO2StockIndexTagBO(StockIndexPO stockIndexPO) {
        return StockIndexTagBO.builder()
                .code(stockIndexPO.getCode())
                .platform(stockIndexPO.getPlatform())
                .region(RegionAssembler.buildRegionBOFromISOCode(stockIndexPO.getRegion()))
                .currency(CurrencyAssembler.buildCurrencyBOFromCurrencyCode(stockIndexPO.getCurrency()))
                .name(stockIndexPO.getName())
                .build();
    }

    public static StockTagPageBO buildStockTagPageBOFromStockTagBOs(Collection<StockTagBO> stockTagBOs, Long total) {
        return StockTagPageBO.builder()
                .stockTags(stockTagBOs.stream().toList())
                .total(total)
                .build();
    }

    public static StockIndexTagPageBO buildStockIndexTagPageBOFromStockIndexTagBOs(Collection<StockIndexTagBO> stockIndexTagBOs, Long total) {
        return StockIndexTagPageBO.builder()
                .stockIndexs(stockIndexTagBOs.stream().toList())
                .total(total)
                .build();
    }
}
