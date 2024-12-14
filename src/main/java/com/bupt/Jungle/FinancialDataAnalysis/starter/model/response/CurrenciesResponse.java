package com.bupt.Jungle.FinancialDataAnalysis.starter.model.response;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrenciesResponse {
    private List<CurrencyBO> currencies;
}
