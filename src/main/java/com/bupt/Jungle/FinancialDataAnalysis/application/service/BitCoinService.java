package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.BitCoinAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.StockAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.BitCoinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BitCoinService {
    private final BitCoinMapper bitCoinMapper;

    @Autowired
    public BitCoinService(BitCoinMapper bitCoinMapper) {
        this.bitCoinMapper = bitCoinMapper;
    }

    public List<RegionBO> getAllRegion() {
        return bitCoinMapper.queryAllRegion()
                .stream()
                .map(StockAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<CurrencyBO> getAllCurrency() {
        return bitCoinMapper.queryAllCurrency()
                .stream()
                .map(StockAssembler::buildCurrencyBOFromCurrencyCode)
                .toList();
    }

    public BitCoinTagPageBO getBitCoinTagPage(String code,
                                              String platform,
                                              String region,
                                              String currency,
                                              Long current,
                                              Long pageSize) {
        Long total = bitCoinMapper.queryBitCoinTagTotalCount(
                code,
                platform,
                region,
                currency
        );

        long offSet = (current - 1) * pageSize;
        if (offSet > total) {
            offSet = 0;
        }

        List<BitCoinTagBO> bitCoinPOS = bitCoinMapper.queryBitCoinTag(
                code,
                platform,
                region,
                currency,
                pageSize,
                offSet
        ).stream().map(BitCoinAssembler::BitCoinPO2BitCoinTagBO).toList();
        return BitCoinAssembler.buildBitCoinTagPageBOFromBitCoinTagBOs(bitCoinPOS, total);
    }
}
