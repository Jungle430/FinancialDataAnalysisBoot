package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.BondsTagPageResponse;

public class BondsAssembler {
    public static BondsTagPageResponse BondsTagPageBO2BondsTagPageResponse(BondsTagPageBO bondsTagPageBO) {
        return BondsTagPageResponse.builder()
                .bondsTags(bondsTagPageBO.getBonds())
                .total(bondsTagPageBO.getTotal())
                .build();
    }
}
