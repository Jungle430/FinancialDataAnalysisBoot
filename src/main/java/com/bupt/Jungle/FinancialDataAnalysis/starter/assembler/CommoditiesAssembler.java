package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.domain.model.CommoditiesCurrencyNumberBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CommoditiesCurrencyNumberResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CommoditiesEchartsResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CommoditiesTagPageResponse;

import java.util.List;

public class CommoditiesAssembler {
    public static CommoditiesTagPageResponse CommoditiesTagPageBO2CommoditiesTagPageResponse(CommoditiesTagPageBO commoditiesTagPageBO) {
        return CommoditiesTagPageResponse.builder()
                .commoditiesTags(commoditiesTagPageBO.getCommodities())
                .total(commoditiesTagPageBO.getTotal())
                .build();
    }

    public static CommoditiesEchartsResponse buildCommoditiesEchartsResponseFromCommoditiesEchartsBO(CommoditiesEchartsBO commoditiesEchartsBO) {
        return CommoditiesEchartsResponse.builder()
                .commodities(commoditiesEchartsBO.getCommoditiesBOs())
                .tags(commoditiesEchartsBO.getCommoditiesTagBO())
                .MA5(commoditiesEchartsBO.getMA5())
                .MA10(commoditiesEchartsBO.getMA10())
                .MA20(commoditiesEchartsBO.getMA20())
                .MA30(commoditiesEchartsBO.getMA30())
                .build();
    }

    public static CommoditiesCurrencyNumberResponse buildCommoditiesCurrencyNumberResponseFromBOs(List<CommoditiesCurrencyNumberBO> commoditiesCurrencyNumberBOs) {
        CommoditiesCurrencyNumberResponse commoditiesCurrencyNumberResponse = new CommoditiesCurrencyNumberResponse();
        commoditiesCurrencyNumberResponse.setCommoditiesCurrencyNumber(commoditiesCurrencyNumberBOs);
        return commoditiesCurrencyNumberResponse;
    }
}
