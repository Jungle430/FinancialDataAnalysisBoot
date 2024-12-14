package com.bupt.Jungle.FinancialDataAnalysis.util;

import com.ibm.icu.util.Currency;

import java.util.Locale;

public final class CurrencyUtil {
    private CurrencyUtil() {
    }

    public static String getSimplifiedCurrencyChineseName(String currencyCode) {
        return getCurrencyName(currencyCode, Locale.SIMPLIFIED_CHINESE);
    }

    public static String getCurrencyEnglishName(String currencyCode) {
        return getCurrencyName(currencyCode, Locale.ENGLISH);
    }

    private static String getCurrencyName(String currencyCode, Locale locale) {
        return Currency.getInstance(currencyCode.toUpperCase()).getDisplayName(locale);
    }
}