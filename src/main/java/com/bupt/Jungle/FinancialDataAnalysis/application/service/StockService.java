package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.StockAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
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

    public List<RegionBO> getAllMarketRegion() {
        return stockMapper.queryAllMarketRegion()
                .stream()
                .map(StockAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<RegionBO> getAllRegion() {
        return stockMapper.queryAllRegion()
                .stream()
                .map(StockAssembler::buildRegionBOFromISOCode)
                .toList();
    }


    public List<CurrencyBO> getAllCurrency() {
        return stockMapper.queryAllCurrency()
                .stream()
                .map(StockAssembler::buildCurrencyBOFromCurrencyCode)
                .toList();
    }

    public StockTagPageBO queryStockTagPage(String code,
                                            String platform,
                                            String region,
                                            String currency,
                                            String name,
                                            String marketRegion,
                                            Long current,
                                            Long pageSize) {
        Long total = stockMapper.queryStockTagTotalCount(
                code,
                platform,
                region,
                currency,
                name,
                marketRegion
        );

        long offSet = (current - 1) * pageSize;
        if (offSet > total) {
            throw new BusinessException("没有该页");
        }

        List<StockTagBO> stockPOS = stockMapper.queryStockTag(
                code,
                platform,
                region,
                currency,
                name,
                marketRegion,
                pageSize,
                offSet
        ).stream().map(StockAssembler::StockPO2StockTagBO).toList();

        return StockAssembler.buildStockTagPageBOFromStockTagBOs(stockPOS, total);
    }
}
