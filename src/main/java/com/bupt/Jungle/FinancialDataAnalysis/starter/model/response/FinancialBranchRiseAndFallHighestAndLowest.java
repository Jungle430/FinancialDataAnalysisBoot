package com.bupt.Jungle.FinancialDataAnalysis.starter.model.response;


import com.bupt.Jungle.FinancialDataAnalysis.domain.model.FinancialBranchRiseAndFallBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialBranchRiseAndFallHighestAndLowest {
    private List<FinancialBranchRiseAndFallBO> financialBranchRiseAndFallHighest;

    private List<FinancialBranchRiseAndFallBO> financialBranchRiseAndFallLowest;
}
