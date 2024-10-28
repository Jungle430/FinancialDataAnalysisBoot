package com.bupt.Jungle.FinancialDataAnalysis.service;

import com.bupt.Jungle.FinancialDataAnalysis.dao.MetersTest;

import java.util.List;

public interface MeterServiceTest {
    List<MetersTest> find();

    Integer create(MetersTest metersTest, String tableName);

    Integer save(MetersTest metersTest, String tableName);

    MetersTest lastRow();
}
