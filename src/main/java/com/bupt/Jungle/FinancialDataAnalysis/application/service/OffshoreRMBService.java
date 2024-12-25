package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.ForexAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.OffshoreRMBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OffshoreRMBService {
    private final OffshoreRMBMapper offshoreRMBMapper;

    @Autowired
    public OffshoreRMBService(OffshoreRMBMapper offshoreRMBMapper) {
        this.offshoreRMBMapper = offshoreRMBMapper;
    }

    public List<RegionBO> getAllBaseRegion() {
        return offshoreRMBMapper.queryAllBaseRegion()
                .stream()
                .map(RegionAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<CurrencyBO> getAllBaseCurrency() {
        return offshoreRMBMapper.queryAllBaseCurrency()
                .stream()
                .map(CurrencyAssembler::buildCurrencyBOFromCurrencyCode)
                .toList();
    }

    public List<RegionBO> getAllQuoteRegion() {
        return offshoreRMBMapper.queryAllQuoteRegion()
                .stream()
                .map(RegionAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<CurrencyBO> getAllQuoteCurrency() {
        return offshoreRMBMapper.queryAllQuoteCurrency()
                .stream()
                .map(CurrencyAssembler::buildCurrencyBOFromCurrencyCode)
                .toList();
    }

    public ForexTagPageBO getOffshoreRMBTagPage(String baseRegion,
                                                String baseCurrency,
                                                String quoteRegion,
                                                String quoteCurrency,
                                                Long current,
                                                Long pageSize) {
        Long total = offshoreRMBMapper.queryOffshoreTotalCount(
                baseRegion,
                baseCurrency,
                quoteRegion,
                quoteCurrency
        );

        long offSet = (current - 1) * pageSize;
        if (offSet > total) {
            offSet = 0;
        }

        List<ForexTagBO> offshoreRMBTags = offshoreRMBMapper.queryOffshoreRMBTag(
                baseRegion,
                baseCurrency,
                quoteRegion,
                quoteCurrency,
                pageSize,
                offSet
        ).stream().map(ForexAssembler::ForexPO2ForexTagBO).toList();
        return ForexAssembler.buildForexTagPageBOFromForexTagBOs(offshoreRMBTags, total);
    }
}
