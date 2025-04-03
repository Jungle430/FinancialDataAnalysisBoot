package com.bupt.Jungle.FinancialDataAnalysis.starter.model.response;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexTagBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockIndexRiseAndFallResponse {
    private StockIndexTagBO stockIndexX;

    private StockIndexTagBO stockIndexY;

    private double riseAndFallPearsonCorrelationCoefficient;
}
