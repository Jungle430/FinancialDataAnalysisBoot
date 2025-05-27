package com.bupt.Jungle.FinancialDataAnalysis.domain.model;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommoditiesCurrencyNumberBO {
    private CurrencyBO currency;

    private Long number;
}
