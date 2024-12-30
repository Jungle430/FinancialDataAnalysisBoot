package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.BondsEchartsResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.BondsTagPageResponse;

public class BondsAssembler {
    public static BondsTagPageResponse BondsTagPageBO2BondsTagPageResponse(BondsTagPageBO bondsTagPageBO) {
        return BondsTagPageResponse.builder()
                .bondsTags(bondsTagPageBO.getBonds())
                .total(bondsTagPageBO.getTotal())
                .build();
    }

    public static BondsEchartsResponse buildBondsEchartsResponseFromBondsEchartsBO(BondsEchartsBO bondsEchartsBO) {
        return BondsEchartsResponse.builder()
                .bonds(bondsEchartsBO.getBondsBOS())
                .tags(bondsEchartsBO.getBondsTagBO())
                .MA5(bondsEchartsBO.getMA5())
                .MA10(bondsEchartsBO.getMA10())
                .MA20(bondsEchartsBO.getMA20())
                .MA30(bondsEchartsBO.getMA30())
                .build();
    }
}
