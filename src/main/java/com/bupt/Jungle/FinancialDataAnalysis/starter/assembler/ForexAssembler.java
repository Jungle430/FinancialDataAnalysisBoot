package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.ForexEchartsResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.ForexTagPageResponse;

public class ForexAssembler {
    public static ForexTagPageResponse ForexTagPageBO2ForexTagPageResponse(ForexTagPageBO forexTagPageBO) {
        return ForexTagPageResponse.builder()
                .forexTags(forexTagPageBO.getForexes())
                .total(forexTagPageBO.getTotal())
                .build();
    }

    public static ForexEchartsResponse buildForexEchartsResponseFromForexEchartsBO(ForexEchartsBO forexEchartsBO) {
        return ForexEchartsResponse.builder()
                .forexes(forexEchartsBO.getForexBOs())
                .tags(forexEchartsBO.getForexTagBO())
                .MA5(forexEchartsBO.getMA5())
                .MA10(forexEchartsBO.getMA10())
                .MA20(forexEchartsBO.getMA20())
                .MA30(forexEchartsBO.getMA30())
                .build();
    }
}
