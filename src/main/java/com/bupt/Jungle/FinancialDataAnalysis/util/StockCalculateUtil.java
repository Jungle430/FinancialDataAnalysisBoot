package com.bupt.Jungle.FinancialDataAnalysis.util;

import com.bupt.Jungle.FinancialDataAnalysis.util.annotation.Attribute;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.Validate;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class StockCalculateUtil {
    public static final int DEFAULT_SCALE = 15;

    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

    private static final Predicate<Method> IS_CALCULATE_GETTER = method -> {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        return (methodName.startsWith("get") && methodName.length() > 3)
                && parameterTypes.length == 0
                &&
                (
                        returnType.equals(double.class)
                                || returnType.equals(float.class)
                                || returnType.equals(int.class)
                                || returnType.equals(long.class)
                                || returnType.equals(short.class)
                                || returnType.equals(byte.class)
                                || returnType.equals(Double.class)
                                || returnType.equals(Float.class)
                                || returnType.equals(Integer.class)
                                || returnType.equals(Long.class)
                                || returnType.equals(Short.class)
                                || returnType.equals(Byte.class)
                );
    };

    private static double convertToDouble(Object result) {
        Objects.requireNonNull(result);
        if (result instanceof Double doubleResult) {
            return doubleResult;
        }
        if (result instanceof Float floatResult) {
            return floatResult.doubleValue();
        }
        if (result instanceof Integer integerResult) {
            return integerResult.doubleValue();
        }
        if (result instanceof Long longResult) {
            return longResult.doubleValue();
        }
        if (result instanceof Short shortResult) {
            return shortResult.doubleValue();
        }
        if (result instanceof Byte byteResult) {
            return byteResult.doubleValue();
        }
        throw new IllegalStateException("Unexpected return type: " + result.getClass());
    }

    private static final Predicate<Method> IS_GET_TS = method -> method.getName().equalsIgnoreCase("getTs");

    private static String getAttrFromGetterMethod(Method method) {
        Attribute attribute = method.getAnnotation(Attribute.class);
        return attribute != null ?
                String.format("%s(%s)", attribute.simplifiedChinese(), attribute.english())
                : method.getName().substring(3);
    }

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

    /**
     * @param values1 calculate values1, has method like <code>public Timestamp getTs() {...}</code> and ordered by this method
     * @param class1  values1's class
     * @param values2 calculate values2, has method like <code>public Timestamp getTs() {...}</code> and ordered by this method
     * @param class2  values2's class
     */
    public static PearsonMatrixWithAttr calculatePearsonMatrix(final List<?> values1, Class<?> class1, final List<?> values2, Class<?> class2) throws InvocationTargetException, IllegalAccessException {
        Validate.notEmpty(values1);
        Validate.notEmpty(values2);
        Objects.requireNonNull(class1);
        Objects.requireNonNull(class2);

        List<Method> methods1 = Arrays.stream(class1.getMethods()).toList();
        List<Method> methods2 = Arrays.stream(class2.getMethods()).toList();

        Method getTsMethod1 = checkAndGetGetTsMethod(methods1, "values1");
        Method getTsMethod2 = checkAndGetGetTsMethod(methods2, "values2");

        List<Method> calculatedMethods1 = checkAndGetCalculatedMethods(
                methods1,
                Comparator.comparing(Method::getName),
                "values1"
        );
        List<Method> calculatedMethods2 = checkAndGetCalculatedMethods(
                methods2,
                Comparator.comparing(Method::getName),
                "values2"
        );


        List<List<Double>> calculatedValues1 = new ArrayList<>(calculatedMethods1.size());
        for (int i = 0; i < calculatedMethods1.size(); i++) {
            calculatedValues1.add(new ArrayList<>(values1.size()));
        }
        List<List<Double>> calculatedValues2 = new ArrayList<>(calculatedMethods2.size());
        for (int i = 0; i < calculatedMethods2.size(); i++) {
            calculatedValues2.add(new ArrayList<>(values2.size()));
        }

        int n1 = values1.size();
        int n2 = values2.size();
        int index1 = 0, index2 = 0;
        while (index1 < n1 && index2 < n2) {
            Timestamp ts1 = (Timestamp) getTsMethod1.invoke(values1.get(index1));
            Timestamp ts2 = (Timestamp) getTsMethod2.invoke(values2.get(index2));
            // assert ts1 != null && ts2 != null
            if (ts1.equals(ts2)) {
                for (int i = 0; i < calculatedMethods1.size(); i++) {
                    calculatedValues1.get(i).add(convertToDouble(calculatedMethods1.get(i).invoke(values1.get(index1))));
                }
                for (int i = 0; i < calculatedMethods2.size(); i++) {
                    calculatedValues2.get(i).add(convertToDouble(calculatedMethods2.get(i).invoke(values2.get(index2))));
                }
                index1++;
                index2++;
            } else if (ts1.compareTo(ts2) < 0) {
                index1++;
            } else if (ts1.compareTo(ts2) > 0) {
                index2++;
            }
        }

        double[][] pearsonMatrix = calculatePearsonMatrix(calculatedValues1, calculatedValues2);
        return PearsonMatrixWithAttr.builder()
                .attributesX(calculatedMethods1.stream().map(StockCalculateUtil::getAttrFromGetterMethod).toList())
                .attributesY(calculatedMethods2.stream().map(StockCalculateUtil::getAttrFromGetterMethod).toList())
                .pearsonMatrix(pearsonMatrix)
                .build();
    }

    private static double[][] calculatePearsonMatrix(final List<List<Double>> values1, final List<List<Double>> values2) {
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

    private static Method checkAndGetGetTsMethod(final List<Method> methods, final String paramsName) {
        Validate.isTrue(methods.stream()
                        .filter(IS_GET_TS)
                        .count() == 1,
                "the number of %s's methods contains method like 'getTs' != 1",
                paramsName
        );
        Method getTsMethod = methods.stream()
                .filter(IS_GET_TS)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalStateException(String.format("%s's methods not contains method like 'getTs'", paramsName))
                );
        Validate.isTrue(
                getTsMethod.getReturnType().equals(Timestamp.class),
                "%s's method 'getTs' returnType is not timestamp",
                paramsName);
        Validate.isTrue(getTsMethod.getParameterTypes().length == 0,
                "%s: method 'getTs' parameterTypes is not '()'",
                paramsName);
        return getTsMethod;
    }

    private static List<Method> checkAndGetCalculatedMethods(final List<Method> methods, Comparator<Method> comparator, String paramsName) {
        return Validate.notEmpty(
                methods.stream()
                        .filter(IS_CALCULATE_GETTER)
                        .sorted(comparator)
                        .toList(),
                "%s's method ",
                paramsName
        );
    }
}
