package com.bupt.Jungle.FinancialDataAnalysis.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialBranchRiseAndFallBO {
    private String financialKindBOX;

    private String financialKindBOY;

    private double riseAndFallPearsonCorrelationCoefficient;
}
