package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.BitCoinTagPageResponse;

public class BitCoinAssembler {
    public static BitCoinTagPageResponse BitCoinTagPageBO2BitCoinTagPageResponse(BitCoinTagPageBO bitCoinTagPageBO) {
        return BitCoinTagPageResponse.builder()
                .bitCoinTags(bitCoinTagPageBO.getBitCoins())
                .total(bitCoinTagPageBO.getTotal())
                .build();
    }
}
