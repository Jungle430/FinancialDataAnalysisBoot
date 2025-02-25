package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.ServiceException;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.StockIndexMapper;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.StockIndexPO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.StockAssembler;

import java.util.List;

@Service(value = "stock_index")
public class StockIndexService implements AnalysisBaseService {
    private final StockIndexMapper stockIndexMapper;

    @Autowired
    public StockIndexService(StockIndexMapper stockIndexMapper) {
        this.stockIndexMapper = stockIndexMapper;
    }

    public List<RegionBO> getAllRegion() {
        return stockIndexMapper.queryAllRegion()
                .stream()
                .map(RegionAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<CurrencyBO> getAllCurrency() {
        return stockIndexMapper.queryAllCurrency()
                .stream()
                .map(CurrencyAssembler::buildCurrencyBOFromCurrencyCode)
                .toList();
    }

    public StockIndexTagPageBO getStockIndexTagPage(String code,
                                                    String platform,
                                                    String region,
                                                    String currency,
                                                    String name,
                                                    Long current,
                                                    Long pageSize) {
        Long total = stockIndexMapper.queryStockIndexTagTotalCount(
                code,
                platform,
                region,
                currency,
                name
        );

        long offset = (current - 1) * pageSize;
        if (offset > total) {
            offset = 0;
        }

        List<StockIndexTagBO> stockIndexTagBOS = stockIndexMapper.queryStockIndexTag(
                code,
                platform,
                region,
                currency,
                name,
                pageSize,
                offset
        ).stream().map(StockAssembler::StockIndexPO2StockIndexTagBO).toList();
        return StockAssembler.buildStockIndexTagPageBOFromStockIndexTagBOs(stockIndexTagBOS, total);
    }

    public StockIndexEchartsBO getStockIndexEchartsData(String code) {
        List<StockIndexPO> stockIndexTags = stockIndexMapper.queryStockIndexTagByCode(code);
        if (CollectionUtils.isEmpty(stockIndexTags)) {
            throw new BusinessException("没有该股票");
        }

        if (stockIndexTags.size() > 1) {
            throw new ServiceException(String.format("数据库数据有问题, 一个code查出来两组TAG, code:%s, tags:%s", code, stockIndexTags));
        }

        StockIndexTagBO stockIndexTagBO = StockAssembler.StockIndexPO2StockIndexTagBO(stockIndexTags.get(0));
        List<StockIndexBO> stockIndexBOS = stockIndexMapper.queryStockIndexDataByCode(code).stream().map(StockAssembler::StockIndexPO2StockIndexBO).toList();
        return StockAssembler.buildStockIndexEchartsBOFromStockIndexBOsAndStockIndexTagBO(stockIndexBOS, stockIndexTagBO);
    }

    @Override
    public List<ImmutablePair<String, String>> getAllBranchBaseData() {
        List<StockIndexPO> stockIndexTags = stockIndexMapper.queryAllTags();

        if (CollectionUtils.isEmpty(stockIndexTags)) {
            throw new BusinessException("股票指数没有分支数据");
        }

        return stockIndexTags
                .stream()
                .map(stockIndexPO -> ImmutablePair.of(
                        stockIndexPO.getCode(),
                        String.join(
                                "-",
                                stockIndexPO.getCode(),
                                stockIndexPO.getName(),
                                stockIndexPO.getPlatform(),
                                RegionAssembler.buildDetailRegionMessageFromISOCode(stockIndexPO.getRegion()),
                                CurrencyAssembler.buildCurrencyDetailMessageFromCurrencyCode(stockIndexPO.getCurrency())
                        )
                )).toList();
    }

    @Override
    public ImmutablePair<List<?>, Class<?>> getAllFinancialBranchData(String code) {
        return ImmutablePair.of(
                stockIndexMapper.queryStockIndexTagByCode(code)
                        .stream()
                        .map(StockAssembler::StockIndexPO2StockIndexBO)
                        .toList(),
                StockIndexBO.class
        );
    }
}
