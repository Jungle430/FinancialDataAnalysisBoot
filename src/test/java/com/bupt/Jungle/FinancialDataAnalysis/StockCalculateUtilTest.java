package com.bupt.Jungle.FinancialDataAnalysis;

import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil.calculatePearsonMatrix;

@SpringBootTest(classes = FinancialDataAnalysisBoot.class)
@Slf4j
@ActiveProfiles("test")
@MapperScan("com.bupt.Jungle.FinancialDataAnalysis.dal.mapper")
public class StockCalculateUtilTest {
    private List<Double> preciseCalculateMAOrigin(final List<Double> closingPrices, int dayCount, int scale, RoundingMode roundingMode) {
        int len = closingPrices.size();
        BigDecimal dayCountBigDecimal = new BigDecimal(dayCount);
        List<Double> result = new ArrayList<>(len);

        for (int i = 0; i < len; i++) {
            if (i < dayCount - 1) {
                result.add(null);
            } else {
                BigDecimal sum = new BigDecimal(0);
                for (int j = 0; j < dayCount; j++) {
                    sum = sum.add(BigDecimal.valueOf(closingPrices.get(i - j)));
                }
                result.add(sum.divide(dayCountBigDecimal, scale, roundingMode).doubleValue());
            }
        }

        return result;
    }

    @Test
    public void testPreciseCalculateMA() {
        List<Double> testData = List.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0);
        List<Double> testCalculateData = preciseCalculateMAOrigin(testData, 3, 10, RoundingMode.HALF_EVEN);
        List<Double> OriginCalculateData = StockCalculateUtil.preciseCalculateMA(testData, 3, 10, RoundingMode.HALF_EVEN);
        log.info("{}", testData);
        log.info("{}", testCalculateData);
        log.info("{}", OriginCalculateData);
        Assertions.assertEquals(
                testCalculateData,
                OriginCalculateData
        );
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class testCalculatePearsonMatrixClass1 {
        // 时间戳
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonFormat(pattern = "yyyy/MM/dd")
        private Timestamp ts;

        // 收盘价
        private Double closingPrice;

        // 开盘价
        private Double openingPrice;

        // 最高价
        private Double highestPrice;

        // 最低价
        private Double lowestPrice;

        // 交易量
        private Double tradeVolume;

        // 涨跌幅
        private Double riseAndFall;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class testCalculatePearsonMatrixClass2 {
        // 时间戳
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
        @JsonFormat(pattern = "yyyy/MM/dd")
        private Timestamp ts;

        // 收盘价
        private Double closingPrice;

        // 开盘价
        private Double openingPrice;

        // 最高价
        private Double highestPrice;

        // 最低价
        private Double lowestPrice;
    }

    private static List<testCalculatePearsonMatrixClass1> generateTestData1() {
        List<testCalculatePearsonMatrixClass1> list = new ArrayList<>();
        Random random = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] dates = {
                "2025-01-01 00:00:00", "2025-01-03 00:00:00", "2025-01-05 00:00:00", "2025-01-06 00:00:00"
        };
        try {
            // 生成 10 条测试数据
            for (String date : dates) {
                Timestamp ts = new Timestamp(sdf.parse(date).getTime());

                // 随机生成其他数据
                double closingPrice = random.nextDouble() * 100;
                double openingPrice = random.nextDouble() * 100;
                double highestPrice = Math.max(closingPrice, openingPrice) + random.nextDouble() * 10;
                double lowestPrice = Math.min(closingPrice, openingPrice) - random.nextDouble() * 10;
                double tradeVolume = random.nextDouble() * 1000;
                double riseAndFall = (random.nextDouble() - 0.5) * 20;

                testCalculatePearsonMatrixClass1 item = testCalculatePearsonMatrixClass1.builder()
                        .ts(ts)
                        .closingPrice(closingPrice)
                        .openingPrice(openingPrice)
                        .highestPrice(highestPrice)
                        .lowestPrice(lowestPrice)
                        .tradeVolume(tradeVolume)
                        .riseAndFall(riseAndFall)
                        .build();
                list.add(item);
            }
        } catch (ParseException e) {
            log.error(e.getMessage());
        }

        // 按照时间戳排序
        list.sort(Comparator.comparing(testCalculatePearsonMatrixClass1::getTs));

        return list;
    }

    private static List<testCalculatePearsonMatrixClass2> generateTestData2() {
        List<testCalculatePearsonMatrixClass2> list = new ArrayList<>();
        Random random = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] dates = {
                "2025-01-01 00:00:00", "2025-01-02 00:00:00", "2025-01-03 00:00:00", "2025-01-05 00:00:00",
        };
        try {
            // 生成 10 条测试数据
            for (String date : dates) {
                Timestamp ts = new Timestamp(sdf.parse(date).getTime());

                // 随机生成其他数据
                double closingPrice = random.nextDouble() * 100;
                double openingPrice = random.nextDouble() * 100;
                double highestPrice = Math.max(closingPrice, openingPrice) + random.nextDouble() * 10;
                double lowestPrice = Math.min(closingPrice, openingPrice) - random.nextDouble() * 10;

                testCalculatePearsonMatrixClass2 item = testCalculatePearsonMatrixClass2.builder()
                        .ts(ts)
                        .closingPrice(closingPrice)
                        .openingPrice(openingPrice)
                        .highestPrice(highestPrice)
                        .lowestPrice(lowestPrice)
                        .build();
                list.add(item);
            }
        } catch (ParseException e) {
            log.error(e.getMessage());
        }

        // 按照时间戳排序
        list.sort(Comparator.comparing(testCalculatePearsonMatrixClass2::getTs));

        return list;
    }

    @Test
    public void testCalculatePearsonMatrix() throws InvocationTargetException, IllegalAccessException {
        List<testCalculatePearsonMatrixClass1> testCalculatePearsonMatrixClass1s = generateTestData1();
        List<testCalculatePearsonMatrixClass2> testCalculatePearsonMatrixClass2s = generateTestData2();

        log.info("{}", calculatePearsonMatrix(
                testCalculatePearsonMatrixClass1s, testCalculatePearsonMatrixClass1.class,
                testCalculatePearsonMatrixClass2s, testCalculatePearsonMatrixClass2.class
        ));
    }
}
