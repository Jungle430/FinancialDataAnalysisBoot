package com.bupt.Jungle.FinancialDataAnalysis.starter.model.response;


import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinTagBO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BitCoinEchartsResponse {
    private List<BitCoinBO> bitCoins;

    private BitCoinTagBO tags;

    @JsonProperty("MA5")
    private List<Double> MA5;

    @JsonProperty("MA10")
    private List<Double> MA10;

    @JsonProperty("MA20")
    private List<Double> MA20;

    @JsonProperty("MA30")
    private List<Double> MA30;
}