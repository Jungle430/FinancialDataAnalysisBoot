package com.bupt.Jungle.FinancialDataAnalysis.application.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommoditiesTagPageBO {
    private List<CommoditiesTagBO> commodities;

    private Long total;
}
