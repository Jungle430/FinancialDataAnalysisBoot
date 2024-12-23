package com.bupt.Jungle.FinancialDataAnalysis;

import com.bupt.Jungle.FinancialDataAnalysis.util.CurrencyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = FinancialDataAnalysisBoot.class)
@Slf4j
@ActiveProfiles("test")
@MapperScan("com.bupt.Jungle.FinancialDataAnalysis.dal.mapper")
public class CurrencyUtilTest {
    @Test
    public void testCurrencyUtil() {
        log.info("{}", CurrencyUtil.getSimplifiedCurrencyChineseName("CNH"));
        log.info("{}", CurrencyUtil.getCurrencyEnglishName("CNH"));
    }
}
