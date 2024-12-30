package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.BondsPO;
import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;

import java.util.Collection;
import java.util.List;

public class BondsAssembler {
    public static BondsTagBO BondsPO2BondsTagBO(BondsPO bondsPO) {
        return BondsTagBO.builder()
                .code(bondsPO.getCode())
                .platform(bondsPO.getPlatform())
                .region(RegionAssembler.buildRegionBOFromISOCode(bondsPO.getRegion()))
                .currency(CurrencyAssembler.buildCurrencyBOFromCurrencyCode(bondsPO.getCurrency()))
                .name(bondsPO.getName())
                .build();
    }

    public static BondsTagPageBO buildBondsTagPageBOFromBondsTagBOs(Collection<BondsTagBO> bondsTagBOs, Long total) {
        return BondsTagPageBO.builder()
                .bonds(bondsTagBOs.stream().toList())
                .total(total)
                .build();
    }

    public static BondsBO BondsPO2BondsBO(BondsPO bondsPO) {
        return BondsBO.builder()
                .ts(bondsPO.getTs())
                .closingPrice(bondsPO.getClosingPrice())
                .openingPrice(bondsPO.getOpeningPrice())
                .highestPrice(bondsPO.getHighestPrice())
                .lowestPrice(bondsPO.getLowestPrice())
                .tradeVolume(bondsPO.getTradeVolume())
                .riseAndFall(bondsPO.getRiseAndFall())
                .build();
    }

    public static BondsEchartsBO buildBondsEchartsBOFromBondsBOsAndBondsTagPageBO(List<BondsBO> bondsBOs, BondsTagBO bondsTagBO) {
        BondsEchartsBO bondsEchartsBO = new BondsEchartsBO();
        bondsEchartsBO.setBondsBOS(bondsBOs);
        bondsEchartsBO.setBondsTagBO(bondsTagBO);
        List<Double> closingPrices = bondsBOs.stream().map(BondsBO::getClosingPrice).toList();
        bondsEchartsBO.setMA5(StockCalculateUtil.preciseCalculateMA(closingPrices, 5));
        bondsEchartsBO.setMA10(StockCalculateUtil.preciseCalculateMA(closingPrices, 10));
        bondsEchartsBO.setMA20(StockCalculateUtil.preciseCalculateMA(closingPrices, 20));
        bondsEchartsBO.setMA30(StockCalculateUtil.preciseCalculateMA(closingPrices, 30));
        return bondsEchartsBO;
    }
}
