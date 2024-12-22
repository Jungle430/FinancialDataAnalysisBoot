package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.CommoditiesPO;

import java.util.Collection;

public class CommoditiesAssembler {
    public static CommoditiesTagBO CommoditiesPO2CommoditiesTagBO(CommoditiesPO commoditiesPO) {
        return CommoditiesTagBO.builder()
                .code(commoditiesPO.getCode())
                .platform(commoditiesPO.getPlatform())
                .region(StockAssembler.buildRegionBOFromISOCode(commoditiesPO.getRegion()))
                .currency(StockAssembler.buildCurrencyBOFromCurrencyCode(commoditiesPO.getCurrency()))
                .name(commoditiesPO.getName())
                .build();
    }

    public static CommoditiesTagPageBO buildCommoditiesTagPageBOFromCommoditiesTagBOs(Collection<CommoditiesTagBO> commoditiesTagBOs, Long total) {
        return CommoditiesTagPageBO.builder()
                .commodities(commoditiesTagBOs.stream().toList())
                .total(total)
                .build();
    }
}
