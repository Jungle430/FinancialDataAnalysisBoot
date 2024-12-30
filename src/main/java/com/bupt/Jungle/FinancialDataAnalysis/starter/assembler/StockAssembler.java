package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.StockEchartsResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.StockIndexEchartsResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.StockIndexTagPageResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.StockTagPageResponse;

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
}
