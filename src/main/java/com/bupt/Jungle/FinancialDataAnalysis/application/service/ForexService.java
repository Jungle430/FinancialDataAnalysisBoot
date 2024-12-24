package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.ForexAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexTagPageBO;
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

    public ForexTagPageBO getForexTagPage(String baseRegion,
                                          String baseCurrency,
                                          String quoteRegion,
                                          String quoteCurrency,
                                          Long current,
                                          Long pageSize) {
        Long total = forexMapper.queryForexTagTotalCount(
                baseRegion,
                baseCurrency,
                quoteRegion,
                quoteCurrency
        );

        long offSet = (current - 1) * pageSize;
        if (offSet > total) {
            offSet = 0;
        }

        List<ForexTagBO> forexTagBOS = forexMapper.queryForexTag(
                baseRegion,
                baseCurrency,
                quoteRegion,
                quoteCurrency,
                pageSize,
                offSet
        ).stream().map(ForexAssembler::ForexPO2ForexTagBO).toList();
        return ForexAssembler.buildForexTagPageBOFromForexTagBOs(forexTagBOS, total);
    }
}
