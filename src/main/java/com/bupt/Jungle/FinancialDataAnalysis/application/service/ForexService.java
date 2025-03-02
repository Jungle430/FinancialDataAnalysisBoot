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
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.ForexMapper;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.ForexPO;
import com.bupt.Jungle.FinancialDataAnalysis.util.type.FinancialCalculateData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "forex")
public class ForexService implements AnalysisBaseService {
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

    public ForexEchartsBO getForexEchartsData(String baseCurrency, String quoteCurrency) {
        List<ForexPO> forexTags = forexMapper.queryForexTagByBaseRegionAndQuoteRegion(baseCurrency, quoteCurrency);
        if (CollectionUtils.isEmpty(forexTags)) {
            throw new BusinessException("没有该股票");
        }

        if (forexTags.size() > 1) {
            throw new ServiceException(String.format("数据库数据有问题, 一个code查出来两组TAG, baseCurrency:%s, quoteCurrency:%s, tags:%s", baseCurrency, quoteCurrency, forexTags));
        }

        ForexTagBO forexTagBO = ForexAssembler.ForexPO2ForexTagBO(forexTags.get(0));
        List<ForexBO> forexBOS = forexMapper.queryForexDataByBaseRegionAndQuoteRegion(baseCurrency, quoteCurrency).stream().map(ForexAssembler::ForexPO2ForexBO).toList();
        return ForexAssembler.buildForexEchartsBOFromForexBOsAndForexTagBO(forexBOS, forexTagBO);
    }

    @Override
    public List<ImmutablePair<String, String>> getAllBranchBaseData() {
        List<ForexPO> forexTags = forexMapper.queryAllTags();

        if (CollectionUtils.isEmpty(forexTags)) {
            throw new BusinessException("外汇没有分支数据");
        }

        return forexTags
                .stream()
                .map(forexPO -> ImmutablePair.of(
                        String.join("-", forexPO.getBaseCurrency(), forexPO.getQuoteCurrency()),
                        String.join("-",
                                CurrencyAssembler.buildCurrencyDetailMessageFromCurrencyCode(forexPO.getBaseCurrency()),
                                RegionAssembler.buildDetailRegionMessageFromISOCode(forexPO.getBaseRegion()),
                                CurrencyAssembler.buildCurrencyDetailMessageFromCurrencyCode(forexPO.getQuoteCurrency()),
                                RegionAssembler.buildDetailRegionMessageFromISOCode(forexPO.getQuoteRegion())
                        )
                ))
                .toList();
    }

    @Override
    public List<FinancialCalculateData> getAllFinancialBranchData(String code) {
        String[] codes = code.split("-");
        if (codes.length != 2) {
            throw new BusinessException("数据格式不对, code:" + code);
        }
        String baseCurrency = codes[0];
        String quoteCurrency = codes[1];
        return forexMapper.queryForexDataByBaseRegionAndQuoteRegionWithOffshoreRMB(
                        baseCurrency,
                        quoteCurrency)
                .stream()
                .map(ForexAssembler::ForexPO2ForexBO)
                .map(FinancialCalculateData.class::cast)
                .toList();
    }
}
