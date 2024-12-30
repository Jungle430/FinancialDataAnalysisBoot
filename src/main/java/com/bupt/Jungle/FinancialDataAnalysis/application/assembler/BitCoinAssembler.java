package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.BitCoinPO;
import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;

import java.util.Collection;
import java.util.List;

public class BitCoinAssembler {
    public static BitCoinTagBO BitCoinPO2BitCoinTagBO(BitCoinPO bitCoinPO) {
        return BitCoinTagBO.builder()
                .code(bitCoinPO.getCode())
                .platform(bitCoinPO.getPlatform())
                .region(RegionAssembler.buildRegionBOFromISOCode(bitCoinPO.getRegion()))
                .currency(CurrencyAssembler.buildCurrencyBOFromCurrencyCode(bitCoinPO.getCurrency()))
                .build();
    }

    public static BitCoinTagPageBO buildBitCoinTagPageBOFromBitCoinTagBOs(Collection<BitCoinTagBO> bitCoinTagBOs, Long total) {
        return BitCoinTagPageBO.builder()
                .bitCoins(bitCoinTagBOs.stream().toList())
                .total(total)
                .build();
    }

    public static BitCoinBO BitCoinPO2BitCoinBO(BitCoinPO bitCoinPO) {
        return BitCoinBO.builder()
                .ts(bitCoinPO.getTs())
                .openingPrice(bitCoinPO.getOpeningPrice())
                .closingPrice(bitCoinPO.getClosingPrice())
                .highestPrice(bitCoinPO.getHighestPrice())
                .lowestPrice(bitCoinPO.getLowestPrice())
                .tradeVolume(bitCoinPO.getTradeVolume())
                .riseAndFall(bitCoinPO.getRiseAndFall())
                .build();
    }

    public static BitCoinEchartsBO buildBitCoinEchartsBOFromBitCoinBOsAndBitCoinTagBO(List<BitCoinBO> bitCoinBOs, BitCoinTagBO bitCoinTagBO) {
        BitCoinEchartsBO bitCoinEchartsBO = new BitCoinEchartsBO();
        bitCoinEchartsBO.setBitCoinBOS(bitCoinBOs);
        bitCoinEchartsBO.setBitCoinTagBO(bitCoinTagBO);
        List<Double> closingPrices = bitCoinBOs.stream().map(BitCoinBO::getClosingPrice).toList();
        bitCoinEchartsBO.setMA5(StockCalculateUtil.preciseCalculateMA(closingPrices, 5));
        bitCoinEchartsBO.setMA10(StockCalculateUtil.preciseCalculateMA(closingPrices, 10));
        bitCoinEchartsBO.setMA20(StockCalculateUtil.preciseCalculateMA(closingPrices, 20));
        bitCoinEchartsBO.setMA30(StockCalculateUtil.preciseCalculateMA(closingPrices, 30));
        return bitCoinEchartsBO;
    }
}
