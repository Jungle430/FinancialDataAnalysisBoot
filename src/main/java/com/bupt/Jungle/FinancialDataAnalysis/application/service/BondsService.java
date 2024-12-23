package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.BondsAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BondsTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.BondsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BondsService {
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
}
