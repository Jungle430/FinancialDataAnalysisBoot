package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.ForexPO;
import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;

import java.util.Collection;
import java.util.List;

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

    public static ForexBO ForexPO2ForexBO(ForexPO forexPO) {
        return ForexBO.builder()
                .ts(forexPO.getTs())
                .closingPrice(forexPO.getClosingPrice())
                .openingPrice(forexPO.getOpeningPrice())
                .highestPrice(forexPO.getHighestPrice())
                .lowestPrice(forexPO.getLowestPrice())
                .riseAndFall(forexPO.getRiseAndFall())
                .build();
    }

    public static ForexEchartsBO buildForexEchartsBOFromForexBOsAndForexTagBO(List<ForexBO> forexBOs, ForexTagBO forexTagBO) {
        ForexEchartsBO forexEchartsBO = new ForexEchartsBO();
        forexEchartsBO.setForexBOs(forexBOs);
        forexEchartsBO.setForexTagBO(forexTagBO);
        List<Double> closingPrices = forexBOs.stream().map(ForexBO::getClosingPrice).toList();
        forexEchartsBO.setMA5(StockCalculateUtil.preciseCalculateMA(closingPrices, 5));
        forexEchartsBO.setMA10(StockCalculateUtil.preciseCalculateMA(closingPrices, 10));
        forexEchartsBO.setMA20(StockCalculateUtil.preciseCalculateMA(closingPrices, 20));
        forexEchartsBO.setMA30(StockCalculateUtil.preciseCalculateMA(closingPrices, 30));
        return forexEchartsBO;
    }
}
