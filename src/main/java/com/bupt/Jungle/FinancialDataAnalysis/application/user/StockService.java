package com.bupt.Jungle.FinancialDataAnalysis.application.user;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private final StockMapper stockMapper;

    @Autowired
    public StockService(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    public List<String> getAllRegion() {
        return stockMapper.queryAllRegion();
    }

    public List<String> getAllCurrency() {
        return stockMapper.queryAllCurrency();
    }

    public List<String> getAllMarketRegion() {
        return stockMapper.queryAllMarketRegion();
    }
}
