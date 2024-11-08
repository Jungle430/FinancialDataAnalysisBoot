package com.bupt.Jungle.FinancialDataAnalysis.service.impl;

import com.bupt.Jungle.FinancialDataAnalysis.dao.MetersTest;
import com.bupt.Jungle.FinancialDataAnalysis.mapper.MetersMapperTest;
import com.bupt.Jungle.FinancialDataAnalysis.service.MeterServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeterServiceImplTest implements MeterServiceTest {
    private final MetersMapperTest metersMapperTest;

    @Autowired
    public MeterServiceImplTest(MetersMapperTest metersMapperTest) {
        this.metersMapperTest = metersMapperTest;
    }

    @Override
    public List<MetersTest> find() {
        return metersMapperTest.find();
    }

    @Override
    public Integer create(MetersTest metersTest, String tableName) {
        return metersMapperTest.create(metersTest, tableName);
    }

    @Override
    public Integer save(MetersTest metersTest, String tableName) {
        return metersMapperTest.save(metersTest, tableName);
    }

    @Override
    public MetersTest lastRow() {
        return metersMapperTest.lastRow();
    }
}
