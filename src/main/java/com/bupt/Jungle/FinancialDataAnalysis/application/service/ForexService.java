package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.ForexMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForexService {
    private final ForexMapper forexMapper;

    @Autowired
    public ForexService(ForexMapper forexMapper) {
        this.forexMapper = forexMapper;
    }

    public List<RegionBO> getAllBaseRegion() {
        return forexMapper.queryAllBaseRegion()
                .stream()
                .map(RegionAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<CurrencyBO> getAllBaseCurrency() {
        return forexMapper.queryAllBaseCurrency()
                .stream()
                .map(CurrencyAssembler::buildCurrencyBOFromCurrencyCode)
                .toList();
    }

    public List<RegionBO> getAllQuoteRegion() {
        return forexMapper.queryAllQuoteRegion()
                .stream()
                .map(RegionAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<CurrencyBO> getAllQuoteCurrency() {
        return forexMapper.queryAllQuoteCurrency()
                .stream()
                .map(CurrencyAssembler::buildCurrencyBOFromCurrencyCode)
                .toList();
    }
}
