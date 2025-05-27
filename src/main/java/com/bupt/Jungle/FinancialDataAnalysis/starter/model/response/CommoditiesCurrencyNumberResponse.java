package com.bupt.Jungle.FinancialDataAnalysis.starter.model.response;

import com.bupt.Jungle.FinancialDataAnalysis.domain.model.CommoditiesCurrencyNumberBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommoditiesCurrencyNumberResponse {
    private List<CommoditiesCurrencyNumberBO> commoditiesCurrencyNumber;
}
