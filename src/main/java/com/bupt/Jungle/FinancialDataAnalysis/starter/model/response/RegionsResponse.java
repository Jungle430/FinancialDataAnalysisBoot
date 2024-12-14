package com.bupt.Jungle.FinancialDataAnalysis.starter.model.response;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionsResponse {
    private List<RegionBO> regions;
}
