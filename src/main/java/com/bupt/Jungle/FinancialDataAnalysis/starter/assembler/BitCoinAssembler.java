package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.BitCoinEchartsResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.BitCoinTagPageResponse;

public class BitCoinAssembler {
    public static BitCoinTagPageResponse BitCoinTagPageBO2BitCoinTagPageResponse(BitCoinTagPageBO bitCoinTagPageBO) {
        return BitCoinTagPageResponse.builder()
                .bitCoinTags(bitCoinTagPageBO.getBitCoins())
                .total(bitCoinTagPageBO.getTotal())
                .build();
    }

    public static BitCoinEchartsResponse buildBitCoinEchartsResponseFromBitCoinEchartsBO(BitCoinEchartsBO bitCoinEchartsBO) {
        return BitCoinEchartsResponse.builder()
                .bitCoins(bitCoinEchartsBO.getBitCoinBOS())
                .tags(bitCoinEchartsBO.getBitCoinTagBO())
                .MA5(bitCoinEchartsBO.getMA5())
                .MA10(bitCoinEchartsBO.getMA10())
                .MA20(bitCoinEchartsBO.getMA20())
                .MA30(bitCoinEchartsBO.getMA30())
                .build();
    }
}
