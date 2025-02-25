package com.bupt.Jungle.FinancialDataAnalysis.application.service;


import com.bupt.Jungle.FinancialDataAnalysis.application.model.FinancialKindBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.util.FinancialDataUtil;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.BaseDBMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseDBMessageService {
    private final BaseDBMessageMapper baseDBMessageMapper;

    private final FinancialDataUtil financialDataUtil;

    @Autowired
    public BaseDBMessageService(
            BaseDBMessageMapper baseDBMessageMapper,
            FinancialDataUtil financialDataUtil
    ) {
        this.baseDBMessageMapper = baseDBMessageMapper;
        this.financialDataUtil = financialDataUtil;
    }

    public List<FinancialKindBO> getAllFinancialKind() {
        return baseDBMessageMapper.showSTables()
                .stream()
                .sorted(String::compareTo)
                .map(en ->
                        FinancialKindBO.builder()
                                .en(en)
                                .cn(financialDataUtil.getEnToCn(en))
                                .build())
                .toList();
    }
}
