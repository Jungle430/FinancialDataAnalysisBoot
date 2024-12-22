package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.BondsPO;

import java.util.Collection;

public class BondsAssembler {
    public static BondsTagBO BondsPO2BondsTagBO(BondsPO bondsPO) {
        return BondsTagBO.builder()
                .code(bondsPO.getCode())
                .platform(bondsPO.getPlatform())
                .region(StockAssembler.buildRegionBOFromISOCode(bondsPO.getRegion()))
                .currency(StockAssembler.buildCurrencyBOFromCurrencyCode(bondsPO.getCurrency()))
                .name(bondsPO.getName())
                .build();
    }

    public static BondsTagPageBO buildBondsTagPageBOFromBondsTagBOs(Collection<BondsTagBO> bondsTagBOs, Long total) {
        return BondsTagPageBO.builder()
                .bonds(bondsTagBOs.stream().toList())
                .total(total)
                .build();
    }
}
