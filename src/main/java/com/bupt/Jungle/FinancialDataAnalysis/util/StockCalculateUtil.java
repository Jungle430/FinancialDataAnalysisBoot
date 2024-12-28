package com.bupt.Jungle.FinancialDataAnalysis.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StockCalculateUtil {
    public static final int DEFAULT_SCALE = 15;

    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

    private StockCalculateUtil() {
    }

    public static List<Double> preciseCalculateMA(final List<Double> closingPrices, int dayCount) {
        return preciseCalculateMA(closingPrices, dayCount, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    public static List<Double> preciseCalculateMA(final List<Double> closingPrices, int dayCount, int scale) {
        return preciseCalculateMA(closingPrices, dayCount, scale, DEFAULT_ROUNDING_MODE);
    }

    public static List<Double> preciseCalculateMA(final List<Double> closingPrices, int dayCount, RoundingMode roundingMode) {
        return preciseCalculateMA(closingPrices, dayCount, DEFAULT_SCALE, roundingMode);
    }

    public static List<Double> preciseCalculateMA(final List<Double> closingPrices, int dayCount, int scale, RoundingMode roundingMode) {
        Objects.requireNonNull(closingPrices);

        if (dayCount < 1) {
            throw new IllegalArgumentException(String.format("dayCount:%d, dayCount is less than 1", dayCount));
        }

        BigDecimal sum = new BigDecimal(0);
        BigDecimal dayCountBigDecimal = new BigDecimal(dayCount);

        int len = closingPrices.size();
        List<Double> precisePrices = new ArrayList<>(len);

        for (int i = 0; i < len; i++) {
            sum = sum.add(BigDecimal.valueOf(closingPrices.get(i)));

            if (i < dayCount - 1) {
                precisePrices.add(null);
                continue;
            }

            if (i == dayCount - 1) {
                precisePrices.add(sum.divide(dayCountBigDecimal, scale, roundingMode).doubleValue());
                continue;
            }

            sum = sum.subtract(BigDecimal.valueOf(closingPrices.get(i - dayCount)));
            precisePrices.add(sum.divide(dayCountBigDecimal, scale, roundingMode).doubleValue());
        }

        return precisePrices;
    }

    public static List<Double> roughCalculateMA(final List<Double> closingPrices, int dayCount) {
        Objects.requireNonNull(closingPrices);

        if (dayCount < 1) {
            throw new IllegalArgumentException(String.format("dayCount:%d, dayCount is less than 1", dayCount));
        }

        double sum = 0;

        int len = closingPrices.size();
        List<Double> result = new ArrayList<>(len);

        for (int i = 0; i < len; i++) {
            sum = sum + closingPrices.get(i);

            if (i < dayCount - 1) {
                result.add(null);
                continue;
            }

            if (i == dayCount - 1) {
                result.add(sum / (double) dayCount);
                continue;
            }

            sum -= closingPrices.get(i - dayCount);
            result.add(sum / (double) dayCount);
        }

        return result;
    }
}
