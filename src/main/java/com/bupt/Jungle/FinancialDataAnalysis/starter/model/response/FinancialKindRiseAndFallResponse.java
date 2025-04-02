package com.bupt.Jungle.FinancialDataAnalysis.starter.model.response;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.FinancialKindBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialKindRiseAndFallResponse {
    private FinancialKindBO financialKindX;

    private FinancialKindBO financialKindY;

    private double riseAndFallPearsonCorrelationCoefficient;
}
