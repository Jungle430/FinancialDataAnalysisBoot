package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.StockAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.ServiceException;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.StockMapper;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.StockPO;
import com.bupt.Jungle.FinancialDataAnalysis.util.type.FinancialCalculateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "stock")
@Slf4j
public class StockService implements AnalysisBaseService {
    private final StockMapper stockMapper;

    @Autowired
    public StockService(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    public List<RegionBO> getAllMarketRegion() {
        return stockMapper.queryAllMarketRegion()
                .stream()
                .map(RegionAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<RegionBO> getAllRegion() {
        return stockMapper.queryAllRegion()
                .stream()
                .map(RegionAssembler::buildRegionBOFromISOCode)
                .toList();
    }


    public List<CurrencyBO> getAllCurrency() {
        return stockMapper.queryAllCurrency()
                .stream()
                .map(CurrencyAssembler::buildCurrencyBOFromCurrencyCode)
                .toList();
    }

    public StockTagPageBO getStockTagPage(String code,
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
            offSet = 0;
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

    public StockEchartsBO getStockEchartsData(String code) {
        List<StockPO> stockTags = stockMapper.queryStockTagByCode(code);
        if (CollectionUtils.isEmpty(stockTags)) {
            throw new BusinessException("没有该股票");
        }

        if (stockTags.size() > 1) {
            throw new ServiceException(String.format("数据库数据有问题, 一个code查出来两组TAG, code:%s, tags:%s", code, stockTags));
        }

        StockTagBO stockTagBO = StockAssembler.StockPO2StockTagBO(stockTags.get(0));
        List<StockBO> stockBOS = stockMapper.queryStockDataByCode(code).stream().map(StockAssembler::StockPO2StockBO).toList();
        return StockAssembler.buildStockEchartsBOFromStockBOsAndStockTagBO(stockBOS, stockTagBO);
    }

    @Override
    public List<ImmutablePair<String, String>> getAllBranchBaseData() {
        List<StockPO> stockTags = stockMapper.queryAllTags();

        if (CollectionUtils.isEmpty(stockTags)) {
            throw new BusinessException("股票没有分支数据");
        }

        return stockTags.stream()
                .map(stockPO ->
                        ImmutablePair.of(
                                stockPO.getCode(),
                                String.join("-",
                                        stockPO.getCode(),
                                        stockPO.getName(),
                                        stockPO.getPlatform(),
                                        CurrencyAssembler.buildCurrencyDetailMessageFromCurrencyCode(stockPO.getCurrency()),
                                        RegionAssembler.buildDetailRegionMessageFromISOCode(stockPO.getRegion()),
                                        RegionAssembler.buildDetailRegionMessageFromISOCode(stockPO.getMarketRegion())
                                )
                        )
                )
                .toList();
    }

    @Override
    public List<String> getAllFinancialBranchCode() {
        return stockMapper.queryAllTags()
                .stream()
                .map(StockPO::getCode)
                .toList();
    }

    @Override
    public List<FinancialCalculateData> getAllFinancialBranchData(String code) {
        return stockMapper.queryStockDataByCode(code)
                .stream()
                .map(StockAssembler::StockPO2StockBO)
                .map(FinancialCalculateData.class::cast)
                .toList();
    }

    @Override
    public List<FinancialCalculateData> getAllFinancialKindRiseAndFallAVG() {
        return stockMapper.queryStockRiseAndFallAVG()
                .stream()
                .map(StockAssembler::StockPO2StockBO)
                .map(FinancialCalculateData.class::cast)
                .toList();
    }
}
