package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.ForexAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.ForexTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.ServiceException;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.OffshoreRMBMapper;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.ForexPO;
import org.apache.commons.collections4.CollectionUtils;
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

    public ForexEchartsBO getOffshoreRMBEchartsData(String baseCurrency, String quoteCurrency) {
        List<ForexPO> offshoreRMBTags = offshoreRMBMapper.queryOffshoreRMBTagByBaseRegionAndQuoteRegion(baseCurrency, quoteCurrency);
        if (CollectionUtils.isEmpty(offshoreRMBTags)) {
            throw new BusinessException("没有该股票");
        }

        if (offshoreRMBTags.size() > 1) {
            throw new ServiceException(String.format("数据库数据有问题, 一个code查出来两组TAG, baseCurrency:%s, quoteCurrency:%s, tags:%s", baseCurrency, quoteCurrency, offshoreRMBTags));
        }

        ForexTagBO offshoreRMBTagBO = ForexAssembler.ForexPO2ForexTagBO(offshoreRMBTags.get(0));
        List<ForexBO> offshoreRMBBOS = offshoreRMBMapper.queryOffshoreRMBDataByBaseRegionAndQuoteRegion(baseCurrency, quoteCurrency).stream().map(ForexAssembler::ForexPO2ForexBO).toList();
        return ForexAssembler.buildForexEchartsBOFromForexBOsAndForexTagBO(offshoreRMBBOS, offshoreRMBTagBO);
    }
}
