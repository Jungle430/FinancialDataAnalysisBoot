package com.bupt.Jungle.FinancialDataAnalysis.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockIndexEchartsBO {
    private List<StockIndexBO> stockIndexBOs;

    private StockIndexTagBO stockIndexTagBO;

    private List<Double> MA5;

    private List<Double> MA10;

    private List<Double> MA20;

    private List<Double> MA30;
}