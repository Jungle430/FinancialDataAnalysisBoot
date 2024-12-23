package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.RegionsResponse;

import java.util.List;

public class RegionAssembler {
    public static RegionsResponse buildRegionsResponseFromRegionBOs(List<RegionBO> regions) {
        RegionsResponse regionsResponse = new RegionsResponse();
        regionsResponse.setRegions(regions);
        return regionsResponse;
    }
}
