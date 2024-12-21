package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.BitCoinPO;

import java.util.Collection;

public class BitCoinAssembler {
    public static BitCoinTagBO BitCoinPO2BitCoinTagBO(BitCoinPO bitCoinPO) {
        return BitCoinTagBO.builder()
                .code(bitCoinPO.getCode())
                .platform(bitCoinPO.getPlatform())
                .region(StockAssembler.buildRegionBOFromISOCode(bitCoinPO.getRegion()))
                .currency(StockAssembler.buildCurrencyBOFromCurrencyCode(bitCoinPO.getCurrency()))
                .build();
    }

    public static BitCoinTagPageBO buildBitCoinTagPageBOFromBitCoinTagBOs(Collection<BitCoinTagBO> bitCoinTagBOs, Long total) {
        return BitCoinTagPageBO.builder()
                .bitCoins(bitCoinTagBOs.stream().toList())
                .total(total)
                .build();
    }
}
