package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.StockIndexMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.StockAssembler;

import java.util.List;

@Service
public class StockIndexService {
    private final StockIndexMapper stockIndexMapper;

    @Autowired
    public StockIndexService(StockIndexMapper stockIndexMapper) {
        this.stockIndexMapper = stockIndexMapper;
    }

    public List<RegionBO> getAllRegions() {
        return stockIndexMapper.queryAllRegion()
                .stream()
                .map(StockAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<CurrencyBO> getAllCurrency() {
        return stockIndexMapper.queryAllCurrency()
                .stream()
                .map(StockAssembler::buildCurrencyBOFromCurrencyCode)
                .toList();
    }
}
