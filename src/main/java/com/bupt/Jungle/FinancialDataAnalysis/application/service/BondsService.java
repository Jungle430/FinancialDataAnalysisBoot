package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.BondsAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.ServiceException;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.BondsMapper;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.BondsPO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "bonds")
public class BondsService implements AnalysisBaseService {
    private final BondsMapper bondsMapper;

    @Autowired
    public BondsService(BondsMapper bondsMapper) {
        this.bondsMapper = bondsMapper;
    }

    public List<RegionBO> getAllRegion() {
        return bondsMapper.queryAllRegion()
                .stream()
                .map(RegionAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<CurrencyBO> getAllCurrency() {
        return bondsMapper.queryAllCurrency()
                .stream()
                .map(CurrencyAssembler::buildCurrencyBOFromCurrencyCode)
                .toList();
    }

    public BondsTagPageBO getBondsTagPage(String code,
                                          String platform,
                                          String region,
                                          String currency,
                                          String name,
                                          Long current,
                                          Long pageSize) {
        Long total = bondsMapper.queryBondsTagTotalCount(
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

        List<BondsTagBO> bondsTagBOS = bondsMapper.queryBondsTag(
                code,
                platform,
                region,
                currency,
                name,
                pageSize,
                offSet
        ).stream().map(BondsAssembler::BondsPO2BondsTagBO).toList();
        return BondsAssembler.buildBondsTagPageBOFromBondsTagBOs(bondsTagBOS, total);
    }

    public BondsEchartsBO getBondsEchartsData(String code) {
        List<BondsPO> bondsTags = bondsMapper.queryBondsTagByCode(code);
        if (CollectionUtils.isEmpty(bondsTags)) {
            throw new BusinessException("没有该债券");
        }

        if (bondsTags.size() > 1) {
            throw new ServiceException(String.format("数据库数据有问题, 一个code查出来两组TAG, code:%s, tags:%s", code, bondsTags));
        }

        BondsTagBO bondsTagBO = BondsAssembler.BondsPO2BondsTagBO(bondsTags.get(0));
        List<BondsBO> bondsBOS = bondsMapper.queryBondsDataByCode(code).stream().map(BondsAssembler::BondsPO2BondsBO).toList();
        return BondsAssembler.buildBondsEchartsBOFromBondsBOsAndBondsTagPageBO(bondsBOS, bondsTagBO);
    }

    @Override
    public List<ImmutablePair<String, String>> getAllBranchBaseData() {
        List<BondsPO> bondsTags = bondsMapper.queryAllTags();

        if (CollectionUtils.isEmpty(bondsTags)) {
            throw new BusinessException("美国国债没有分支数据");
        }

        return bondsTags.stream().map(
                bondsPO -> ImmutablePair.of(
                        bondsPO.getCode(),
                        String.join(
                                "-",
                                bondsPO.getCode(),
                                bondsPO.getName(),
                                bondsPO.getPlatform(),
                                RegionAssembler.buildDetailRegionMessageFromISOCode(bondsPO.getRegion()),
                                CurrencyAssembler.buildCurrencyDetailMessageFromCurrencyCode(bondsPO.getCurrency())))
        ).toList();
    }

    @Override
    public ImmutablePair<List<?>, Class<?>> getAllFinancialBranchData(String code) {
        return ImmutablePair.of(
                bondsMapper.queryBondsDataByCode(code)
                        .stream()
                        .map(BondsAssembler::BondsPO2BondsBO)
                        .toList(),
                BondsBO.class
        );
    }
}
