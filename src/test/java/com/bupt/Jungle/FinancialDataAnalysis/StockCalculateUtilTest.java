package com.bupt.Jungle.FinancialDataAnalysis;

import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

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

}
