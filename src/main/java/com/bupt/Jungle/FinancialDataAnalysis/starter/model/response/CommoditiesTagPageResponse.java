package com.bupt.Jungle.FinancialDataAnalysis.starter.model.response;


import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesTagBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommoditiesTagPageResponse {
    private List<CommoditiesTagBO> commoditiesTags;

    private Long total;
}
