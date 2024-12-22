package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CommoditiesTagPageResponse;

public class CommoditiesAssembler {
    public static CommoditiesTagPageResponse CommoditiesTagPageBO2CommoditiesTagPageResponse(CommoditiesTagPageBO commoditiesTagPageBO) {
        return CommoditiesTagPageResponse.builder()
                .commoditiesTags(commoditiesTagPageBO.getCommodities())
                .total(commoditiesTagPageBO.getTotal())
                .build();
    }
}
