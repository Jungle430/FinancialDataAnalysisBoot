package com.bupt.Jungle.FinancialDataAnalysis.service.impl;

import com.bupt.Jungle.FinancialDataAnalysis.dao.DO.MetersTestDO;
import com.bupt.Jungle.FinancialDataAnalysis.mapper.MetersTestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeterTestServiceImpl {
    private final MetersTestMapper metersTestMapper;

    @Autowired
    public MeterTestServiceImpl(MetersTestMapper metersTestMapper) {
        this.metersTestMapper = metersTestMapper;
    }

    public List<MetersTestDO> find() {
        return metersTestMapper.find();
    }

    public MetersTestDO lastRow() {
        return metersTestMapper.lastRow();
    }
}
