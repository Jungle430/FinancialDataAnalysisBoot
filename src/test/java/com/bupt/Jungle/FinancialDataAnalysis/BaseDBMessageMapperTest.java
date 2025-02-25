package com.bupt.Jungle.FinancialDataAnalysis;


import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.BaseDBMessageMapper;
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
public class BaseDBMessageMapperTest {
    private final BaseDBMessageMapper baseTableMessageMapper;

    @Autowired
    public BaseDBMessageMapperTest(BaseDBMessageMapper baseTableMessageMapper) {
        this.baseTableMessageMapper = baseTableMessageMapper;
    }

    @Test
    public void testShowSTables() {
        log.info("{}", baseTableMessageMapper.showSTables());
    }
}
