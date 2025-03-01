package com.bupt.Jungle.FinancialDataAnalysis.application.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.util.ISOUtil;

public class RegionAssembler {
    public static RegionBO buildRegionBOFromISOCode(String isoCode) {
        return RegionBO
                .builder()
                .isoCode(isoCode)
                .simplifiedChineseName(ISOUtil.isoSimplifiedChineseName(isoCode))
                .englishName(ISOUtil.isoEnglishName(isoCode))
                .build();
    }

    public static String buildDetailRegionMessageFromISOCode(String isoCode) {
        return String.format("%s(%s)", ISOUtil.isoSimplifiedChineseName(isoCode), ISOUtil.isoEnglishName(isoCode));
    }
}
