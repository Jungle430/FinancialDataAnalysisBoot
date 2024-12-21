package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.StockIndexPO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.StockPO;
import com.bupt.Jungle.FinancialDataAnalysis.util.CurrencyUtil;
import com.bupt.Jungle.FinancialDataAnalysis.util.ISOUtil;

import java.util.Collection;

public class StockAssembler {
    public static StockTagBO StockPO2StockTagBO(StockPO stockPO) {
        return StockTagBO.builder()
                .code(stockPO.getCode())
                .platform(stockPO.getPlatform())
                .region(buildRegionBOFromISOCode(stockPO.getRegion()))
                .currency(buildCurrencyBOFromCurrencyCode(stockPO.getCurrency()))
                .name(stockPO.getName())
                .marketRegion(buildRegionBOFromISOCode(stockPO.getMarketRegion()))
                .build();
    }

    public static StockIndexTagBO StockIndexPO2StockIndexTagBO(StockIndexPO stockIndexPO) {
        return StockIndexTagBO.builder()
                .code(stockIndexPO.getCode())
                .platform(stockIndexPO.getPlatform())
                .region(buildRegionBOFromISOCode(stockIndexPO.getRegion()))
                .currency(buildCurrencyBOFromCurrencyCode(stockIndexPO.getCurrency()))
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

    public static RegionBO buildRegionBOFromISOCode(String isoCode) {
        return RegionBO
                .builder()
                .isoCode(isoCode)
                .simplifiedChineseName(ISOUtil.isoSimplifiedChineseName(isoCode))
                .englishName(ISOUtil.isoEnglishName(isoCode))
                .build();
    }

    public static CurrencyBO buildCurrencyBOFromCurrencyCode(String currencyCode) {
        return CurrencyBO
                .builder()
                .currencyCode(currencyCode)
                .simplifiedChineseName(CurrencyUtil.getSimplifiedCurrencyChineseName(currencyCode))
                .englishName(CurrencyUtil.getCurrencyEnglishName(currencyCode))
                .build();
    }


}
