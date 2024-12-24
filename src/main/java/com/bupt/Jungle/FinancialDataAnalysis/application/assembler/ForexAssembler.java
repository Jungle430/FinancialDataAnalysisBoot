package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.ForexPO;

import java.util.Collection;

public class ForexAssembler {
    public static ForexTagBO ForexPO2ForexTagBO(ForexPO forexPO) {
        return ForexTagBO.builder()
                .baseRegion(RegionAssembler.buildRegionBOFromISOCode(forexPO.getBaseRegion()))
                .baseCurrency(CurrencyAssembler.buildCurrencyBOFromCurrencyCode(forexPO.getBaseCurrency()))
                .quoteRegion(RegionAssembler.buildRegionBOFromISOCode(forexPO.getQuoteRegion()))
                .quoteCurrency(CurrencyAssembler.buildCurrencyBOFromCurrencyCode(forexPO.getQuoteCurrency()))
                .build();
    }

    public static ForexTagPageBO buildForexTagPageBOFromForexTagBOs(Collection<ForexTagBO> forexTagBOs, Long total) {
        return ForexTagPageBO.builder()
                .forexes(forexTagBOs.stream().toList())
                .total(total)
                .build();
    }
}
