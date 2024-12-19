package com.bupt.Jungle.FinancialDataAnalysis;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = FinancialDataAnalysisBoot.class)
@Slf4j
@ActiveProfiles("test")
@MapperScan("com.bupt.Jungle.FinancialDataAnalysis.dal.mapper")
public class StockTest {
    private final StockMapper stockMapper;

    @Autowired
    public StockTest(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    @Test
    public void testQueryStockTagPage() {
        log.info("{}", stockMapper.queryStockTag(null, null, null, null, null, null, 10L, 0L).size());
    }

    @Test
    public void testQueryStockTotal() {
        log.info("{}", stockMapper.queryStockTagTotalCount(null, null, null, null, null, null));
    }
}
