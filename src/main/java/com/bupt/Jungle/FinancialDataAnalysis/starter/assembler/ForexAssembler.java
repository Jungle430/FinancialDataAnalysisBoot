package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.ForexTagPageResponse;

public class ForexAssembler {
    public static ForexTagPageResponse ForexTagPageBO2ForexTagPageResponse(ForexTagPageBO forexTagPageBO) {
        return ForexTagPageResponse.builder()
                .forexTags(forexTagPageBO.getForexes())
                .total(forexTagPageBO.getTotal())
                .build();
    }
}
