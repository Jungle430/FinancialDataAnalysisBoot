package com.bupt.Jungle.FinancialDataAnalysis.util;

import com.ibm.icu.util.ULocale;

public final class ISOUtil {
    private ISOUtil() {
    }

    public static String isoSimplifiedChineseName(String isoCode) {
        return isoName(isoCode, ULocale.SIMPLIFIED_CHINESE);
    }

    public static String isoEnglishName(String isoCode) {
        return isoName(isoCode, ULocale.ENGLISH);
    }

    private static String isoName(String isoCode, ULocale locale) {
        return new ULocale("und-" + isoCode.toUpperCase()).getDisplayCountry(locale);
    }
}
