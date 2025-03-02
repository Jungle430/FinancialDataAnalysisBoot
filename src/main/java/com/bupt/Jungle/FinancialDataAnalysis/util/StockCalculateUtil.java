package com.bupt.Jungle.FinancialDataAnalysis.util;

import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.util.type.FinancialCalculateData;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StockCalculateUtil {
    public static final int DEFAULT_SCALE = 15;

    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

    /**
     * 皮尔逊矩阵返回包装
     */
    @Data
    @Builder
    public static class PearsonMatrixWithAttr {
        /**
         * x属性列表
         */
        private List<String> attributesX;

        /**
         * y属性列表
         */
        private List<String> attributesY;

        /**
         * 皮尔逊矩阵
         */
        private double[][] pearsonMatrix;
    }

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

    private static boolean filterQualifiedFinancialCalculateData(FinancialCalculateData financialCalculateData) {
        if (financialCalculateData == null) {
            return false;
        }

        Timestamp financialDataTimestamp = financialCalculateData.getFinancialDataTimestamp();
        if (financialDataTimestamp == null) {
            return false;
        }

        List<String> calculationAttributeEnglishNames = financialCalculateData.getCalculationAttributeEnglishNames();
        if (CollectionUtils.isEmpty(calculationAttributeEnglishNames)) {
            return false;
        }

        List<String> calculationAttributeSimplifiedChineseNames = financialCalculateData.getCalculationAttributeSimplifiedChineseNames();
        if (CollectionUtils.isEmpty(calculationAttributeSimplifiedChineseNames)) {
            return false;
        }

        List<Double> calculationAttributeData = financialCalculateData.getCalculationAttributeData();
        if (CollectionUtils.isEmpty(calculationAttributeData)) {
            return false;
        }

        return calculationAttributeEnglishNames.size() == calculationAttributeSimplifiedChineseNames.size()
                && calculationAttributeEnglishNames.size() == calculationAttributeData.size()
                && calculationAttributeData
                .stream()
                .allMatch(Objects::nonNull)
                && calculationAttributeEnglishNames
                .stream()
                .allMatch(StringUtils::isNotBlank)
                && calculationAttributeSimplifiedChineseNames
                .stream()
                .allMatch(StringUtils::isNotBlank);
    }

    @Data
    private static class CalculateDataWithoutFullData {
        /**
         * 属性字段名称(简体中文+英文)
         */
        private List<String> calculationAttributeNames;

        /**
         * 属性数量
         */
        private int attributeCount;

        /**
         * 过滤后的数据
         */
        private List<FinancialCalculateData> filterValues;

        /**
         * 后续用于填充数据的列表,填充完数据之后进行相关性系数的计算
         */
        private List<List<Double>> calculateValues;
    }

    /**
     * @param businessExceptionMsg 异常时本数据的标识信息
     */
    private static CalculateDataWithoutFullData getCalculateDataWithoutFullData(final List<FinancialCalculateData> value, String businessExceptionMsg) {
        Validate.notEmpty(value);

        List<FinancialCalculateData> filterValues = value
                .stream()
                .filter(StockCalculateUtil::filterQualifiedFinancialCalculateData)
                .toList();

        if (CollectionUtils.isEmpty(filterValues)) {
            throw new BusinessException("计算数据%s为空", businessExceptionMsg);
        }

        int attributeCount = filterValues.get(0).getCalculationAttributeData().size();

        List<List<Double>> calculateValues = new ArrayList<>(attributeCount);
        List<String> calculationAttributeNames = new ArrayList<>(attributeCount);
        for (int i = 0; i < attributeCount; i++) {
            calculateValues.add(new ArrayList<>(filterValues.size()));
            calculationAttributeNames.add(
                    String.join(
                            "-",
                            filterValues.get(0).getCalculationAttributeSimplifiedChineseNames().get(i),
                            filterValues.get(0).getCalculationAttributeEnglishNames().get(i)
                    )
            );
        }

        CalculateDataWithoutFullData calculateDataWithoutFullData = new CalculateDataWithoutFullData();
        calculateDataWithoutFullData.setCalculationAttributeNames(calculationAttributeNames);
        calculateDataWithoutFullData.setAttributeCount(attributeCount);
        calculateDataWithoutFullData.setFilterValues(filterValues);
        calculateDataWithoutFullData.setCalculateValues(calculateValues);
        return calculateDataWithoutFullData;
    }

    /**
     * 计算两种金融数据的相关性矩阵
     * <p>
     * <strong>
     * 由于数据库中带的排序功能通常会比Java本身的排序快很多,
     * 并且不会对后端的服务器造成更大的计算压力,
     * 本方法要求提供排序好的数据,
     * 请在数据库中或者使用其他方式排序好数据后再将数据传入该方法
     * </strong>
     *
     * @throws NullPointerException     参数为空
     * @throws IllegalArgumentException 参数为空数组
     * @throws BusinessException        计算数据经过{@link #filterQualifiedFinancialCalculateData}过滤后为空
     */
    public static PearsonMatrixWithAttr calculatePearsonMatrix(final List<FinancialCalculateData> values1, final List<FinancialCalculateData> values2) {
        CalculateDataWithoutFullData calculateDataWithoutFullData1 = getCalculateDataWithoutFullData(values1, "values1");
        CalculateDataWithoutFullData calculateDataWithoutFullData2 = getCalculateDataWithoutFullData(values2, "values2");

        int attributeCount1 = calculateDataWithoutFullData1.getAttributeCount();
        int attributeCount2 = calculateDataWithoutFullData2.getAttributeCount();

        int filterValuesSize1 = calculateDataWithoutFullData1.getFilterValues().size();
        int filterValuesSize2 = calculateDataWithoutFullData2.getFilterValues().size();

        List<FinancialCalculateData> filterValues1 = calculateDataWithoutFullData1.getFilterValues();
        List<FinancialCalculateData> filterValues2 = calculateDataWithoutFullData2.getFilterValues();

        int index1 = 0;
        int index2 = 0;
        while (index1 < filterValuesSize1 && index2 < filterValuesSize2) {
            Timestamp ts1 = filterValues1.get(index1).getFinancialDataTimestamp();
            Timestamp ts2 = filterValues2.get(index2).getFinancialDataTimestamp();
            if (ts1.equals(ts2)) {
                List<Double> calculationAttributeData1 = calculateDataWithoutFullData1.getFilterValues().get(index1).getCalculationAttributeData();
                for (int i = 0; i < attributeCount1; i++) {
                    calculateDataWithoutFullData1.getCalculateValues().get(i).add(calculationAttributeData1.get(i));
                }
                List<Double> calculationAttributeData2 = calculateDataWithoutFullData2.getFilterValues().get(index2).getCalculationAttributeData();
                for (int i = 0; i < attributeCount2; i++) {
                    calculateDataWithoutFullData2.getCalculateValues().get(i).add(calculationAttributeData2.get(i));
                }
                index1++;
                index2++;
            } else if (ts1.compareTo(ts2) < 0) {
                index1++;
            } else if (ts1.compareTo(ts2) > 0) {
                index2++;
            }
        }

        double[][] pearsonMatrix = calculatePearsonMatrixWithProcessedData(calculateDataWithoutFullData1.getCalculateValues(), calculateDataWithoutFullData2.getCalculateValues());
        return PearsonMatrixWithAttr.builder()
                .pearsonMatrix(pearsonMatrix)
                .attributesX(calculateDataWithoutFullData1.getCalculationAttributeNames())
                .attributesY(calculateDataWithoutFullData2.getCalculationAttributeNames())
                .build();
    }

    private static double[][] calculatePearsonMatrixWithProcessedData(final List<List<Double>> values1, final List<List<Double>> values2) {
        int numAttributes1 = values1.size();
        int numAttributes2 = values2.size();
        double[][] crossCorrelationMatrix = new double[numAttributes1][numAttributes2];
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        for (int i = 0; i < numAttributes1; i++) {
            double[] attr1 = values1.get(i).stream().mapToDouble(Double::doubleValue).toArray();
            for (int j = 0; j < numAttributes2; j++) {
                double[] attr2 = values2.get(j).stream().mapToDouble(Double::doubleValue).toArray();
                crossCorrelationMatrix[i][j] = pearsonsCorrelation.correlation(attr1, attr2);
            }
        }
        return crossCorrelationMatrix;
    }

}
