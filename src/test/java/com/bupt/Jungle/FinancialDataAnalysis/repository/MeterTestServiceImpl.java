package com.bupt.Jungle.FinancialDataAnalysis.repository;

import com.bupt.Jungle.FinancialDataAnalysis.dal.model.MetersTestPO;
import com.bupt.Jungle.FinancialDataAnalysis.dal.mapper.MetersTestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MeterTestServiceImpl {
    private final MetersTestMapper metersTestMapper;

    @Autowired
    public MeterTestServiceImpl(MetersTestMapper metersTestMapper) {
        this.metersTestMapper = metersTestMapper;
    }

    public List<MetersTestPO> find() {
        return metersTestMapper.find();
    }

    public MetersTestPO lastRow() {
        return metersTestMapper.lastRow();
    }
}
