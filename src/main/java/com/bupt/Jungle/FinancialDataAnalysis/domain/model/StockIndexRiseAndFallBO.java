package com.bupt.Jungle.FinancialDataAnalysis.domain.model;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexTagBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockIndexRiseAndFallBO {
    private StockIndexTagBO stockIndexTagBOX;

    private StockIndexTagBO stockIndexTagBOY;

    private double riseAndFallPearsonCorrelationCoefficient;
}
