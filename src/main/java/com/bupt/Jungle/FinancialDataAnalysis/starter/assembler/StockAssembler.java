package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.domain.model.StockIndexRiseAndFallBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.*;

public class StockAssembler {
    public static StockTagPageResponse StockTagPageBO2StockTagsPageResponse(StockTagPageBO stockTagPageBO) {
        return StockTagPageResponse.builder()
                .stockTags(stockTagPageBO.getStockTags())
                .total(stockTagPageBO.getTotal())
                .build();
    }

    public static StockIndexTagPageResponse StockIndexTagPageBO2StockIndexTagPageResponse(StockIndexTagPageBO stockIndexTagPageBO) {
        return StockIndexTagPageResponse.builder()
                .stockIndexTags(stockIndexTagPageBO.getStockIndexs())
                .total(stockIndexTagPageBO.getTotal())
                .build();
    }

    public static StockEchartsResponse buildStockEchartsResponseFromStockEchartsBO(StockEchartsBO stockEchartsBO) {
        return StockEchartsResponse.builder()
                .stocks(stockEchartsBO.getStockBOs())
                .tags(stockEchartsBO.getStockTagBO())
                .MA5(stockEchartsBO.getMA5())
                .MA10(stockEchartsBO.getMA10())
                .MA20(stockEchartsBO.getMA20())
                .MA30(stockEchartsBO.getMA30())
                .build();
    }

    public static StockIndexEchartsResponse buildStockIndexEchartsResponseFromStockIndexEchartsBO(StockIndexEchartsBO stockIndexEchartsBO) {
        return StockIndexEchartsResponse.builder()
                .stockIndexes(stockIndexEchartsBO.getStockIndexBOs())
                .tags(stockIndexEchartsBO.getStockIndexTagBO())
                .MA5(stockIndexEchartsBO.getMA5())
                .MA10(stockIndexEchartsBO.getMA10())
                .MA20(stockIndexEchartsBO.getMA20())
                .MA30(stockIndexEchartsBO.getMA30())
                .build();
    }

    public static StockIndexRiseAndFallResponse StockIndexRiseAndFallBO2Response(StockIndexRiseAndFallBO stockIndexRiseAndFallBO) {
        StockIndexRiseAndFallResponse stockIndexRiseAndFallResponse = new StockIndexRiseAndFallResponse();
        stockIndexRiseAndFallResponse.setStockIndexX(stockIndexRiseAndFallBO.getStockIndexTagBOX());
        stockIndexRiseAndFallResponse.setStockIndexY(stockIndexRiseAndFallBO.getStockIndexTagBOY());
        stockIndexRiseAndFallResponse.setRiseAndFallPearsonCorrelationCoefficient(stockIndexRiseAndFallBO.getRiseAndFallPearsonCorrelationCoefficient());
        return stockIndexRiseAndFallResponse;
    }
}
