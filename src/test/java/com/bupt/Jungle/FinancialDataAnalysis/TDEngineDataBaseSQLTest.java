package com.bupt.Jungle.FinancialDataAnalysis;

import com.bupt.Jungle.FinancialDataAnalysis.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = FinancialDataAnalysisBoot.class)
@Slf4j
@MapperScan("com.bupt.Jungle.FinancialDataAnalysis.mapper")
public class TDEngineDataBaseSQLTest {
    private final StockMapper stockMapper;

    @Autowired
    public TDEngineDataBaseSQLTest(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    @Test
    public void TestStockSelect() {
        log.info("{}", stockMapper.queryByCompany("PDD", 5));
    }
}
