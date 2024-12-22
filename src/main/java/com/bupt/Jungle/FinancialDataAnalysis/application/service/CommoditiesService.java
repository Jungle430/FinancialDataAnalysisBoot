package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CommoditiesAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.StockAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CommoditiesTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.CommoditiesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommoditiesService {
    private final CommoditiesMapper commoditiesMapper;

    @Autowired
    public CommoditiesService(CommoditiesMapper commoditiesMapper) {
        this.commoditiesMapper = commoditiesMapper;
    }

    public List<RegionBO> getAllRegion() {
        return commoditiesMapper.queryAllRegion()
                .stream()
                .map(StockAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<CurrencyBO> getAllCurrency() {
        return commoditiesMapper.queryAllCurrency()
                .stream()
                .map(StockAssembler::buildCurrencyBOFromCurrencyCode)
                .toList();
    }

    public CommoditiesTagPageBO getCommoditiesTagPage(String code,
                                                      String platform,
                                                      String region,
                                                      String currency,
                                                      String name,
                                                      Long current,
                                                      Long pageSize) {
        Long total = commoditiesMapper.queryCommoditiesTagTotalCount(
                code,
                platform,
                region,
                currency,
                name
        );

        long offSet = (current - 1) * pageSize;
        if (offSet > total) {
            offSet = 0;
        }

        List<CommoditiesTagBO> commoditiesTagBOS = commoditiesMapper.queryCommoditiesTag(
                code,
                platform,
                region,
                currency,
                name,
                pageSize,
                offSet
        ).stream().map(CommoditiesAssembler::CommoditiesPO2CommoditiesTagBO).toList();
        return CommoditiesAssembler.buildCommoditiesTagPageBOFromCommoditiesTagBOs(commoditiesTagBOS, total);
    }
}
