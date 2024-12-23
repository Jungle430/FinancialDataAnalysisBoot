package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockTagPageBO;
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
}
