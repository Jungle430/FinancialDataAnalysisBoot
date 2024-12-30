package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.CommoditiesPO;
import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;

import java.util.Collection;
import java.util.List;

public class CommoditiesAssembler {
    public static CommoditiesTagBO CommoditiesPO2CommoditiesTagBO(CommoditiesPO commoditiesPO) {
        return CommoditiesTagBO.builder()
                .code(commoditiesPO.getCode())
                .platform(commoditiesPO.getPlatform())
                .region(RegionAssembler.buildRegionBOFromISOCode(commoditiesPO.getRegion()))
                .currency(CurrencyAssembler.buildCurrencyBOFromCurrencyCode(commoditiesPO.getCurrency()))
                .name(commoditiesPO.getName())
                .build();
    }

    public static CommoditiesTagPageBO buildCommoditiesTagPageBOFromCommoditiesTagBOs(Collection<CommoditiesTagBO> commoditiesTagBOs, Long total) {
        return CommoditiesTagPageBO.builder()
                .commodities(commoditiesTagBOs.stream().toList())
                .total(total)
                .build();
    }

    public static CommoditiesEchartsBO buildCommoditiesEchartsBOFromCommoditiesBOsAndCommoditiesTagBO(List<CommoditiesBO> commoditiesBOS, CommoditiesTagBO commoditiesTagBO) {
        CommoditiesEchartsBO commoditiesEchartsBO = new CommoditiesEchartsBO();
        commoditiesEchartsBO.setCommoditiesBOs(commoditiesBOS);
        commoditiesEchartsBO.setCommoditiesTagBO(commoditiesTagBO);
        List<Double> closingPrices = commoditiesBOS.stream().map(CommoditiesBO::getClosingPrice).toList();
        commoditiesEchartsBO.setMA5(StockCalculateUtil.preciseCalculateMA(closingPrices, 5));
        commoditiesEchartsBO.setMA10(StockCalculateUtil.preciseCalculateMA(closingPrices, 10));
        commoditiesEchartsBO.setMA20(StockCalculateUtil.preciseCalculateMA(closingPrices, 20));
        commoditiesEchartsBO.setMA30(StockCalculateUtil.preciseCalculateMA(closingPrices, 30));
        return commoditiesEchartsBO;
    }

    public static CommoditiesBO CommoditiesPO2CommoditiesBO(CommoditiesPO commoditiesPO) {
        return CommoditiesBO.builder()
                .ts(commoditiesPO.getTs())
                .openingPrice(commoditiesPO.getOpeningPrice())
                .closingPrice(commoditiesPO.getClosingPrice())
                .highestPrice(commoditiesPO.getHighestPrice())
                .lowestPrice(commoditiesPO.getLowestPrice())
                .tradeVolume(commoditiesPO.getTradeVolume())
                .riseAndFall(commoditiesPO.getRiseAndFall())
                .build();
    }
}
