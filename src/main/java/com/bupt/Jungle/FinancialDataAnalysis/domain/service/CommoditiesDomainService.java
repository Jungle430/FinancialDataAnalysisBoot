package com.bupt.Jungle.FinancialDataAnalysis.domain.service;


import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.CommoditiesMapper;
import com.bupt.Jungle.FinancialDataAnalysis.domain.model.CommoditiesCurrencyNumberBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CommoditiesDomainService {
    private final CommoditiesMapper commoditiesMapper;

    @Autowired
    public CommoditiesDomainService(CommoditiesMapper commoditiesMapper) {
        this.commoditiesMapper = commoditiesMapper;
    }

    public List<CommoditiesCurrencyNumberBO> getCommoditiesCurrencyNumber() {
        return commoditiesMapper.queryCurrencyNumber()
                .stream()
                .map(currencyNumber -> {
                    CommoditiesCurrencyNumberBO commoditiesCurrencyNumber = new CommoditiesCurrencyNumberBO();
                    CurrencyBO currencyBO = CurrencyAssembler.buildCurrencyBOFromCurrencyCode(currencyNumber.getRight());
                    commoditiesCurrencyNumber.setCurrency(currencyBO);
                    commoditiesCurrencyNumber.setNumber(currencyNumber.getLeft());
                    return commoditiesCurrencyNumber;
                }).toList();
    }
}
