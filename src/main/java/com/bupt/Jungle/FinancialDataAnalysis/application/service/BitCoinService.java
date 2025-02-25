package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.BitCoinAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinEchartsBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.BitCoinTagPageBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.CurrencyBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.model.RegionBO;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.ServiceException;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.BitCoinMapper;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.BitCoinPO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "bit_coin")
public class BitCoinService implements AnalysisBaseService {
    private final BitCoinMapper bitCoinMapper;

    @Autowired
    public BitCoinService(BitCoinMapper bitCoinMapper) {
        this.bitCoinMapper = bitCoinMapper;
    }

    public List<RegionBO> getAllRegion() {
        return bitCoinMapper.queryAllRegion()
                .stream()
                .map(RegionAssembler::buildRegionBOFromISOCode)
                .toList();
    }

    public List<CurrencyBO> getAllCurrency() {
        return bitCoinMapper.queryAllCurrency()
                .stream()
                .map(CurrencyAssembler::buildCurrencyBOFromCurrencyCode)
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

    public BitCoinEchartsBO getBitCoinEchartsData(String code) {
        List<BitCoinPO> bitCoinTags = bitCoinMapper.queryBitCoinTagByCode(code);
        if (CollectionUtils.isEmpty(bitCoinTags)) {
            throw new BusinessException("没有该比特币");
        }

        if (bitCoinTags.size() > 1) {
            throw new ServiceException(String.format("数据库数据有问题, 一个code查出来两组TAG, code:%s, tags:%s", code, bitCoinTags));
        }

        BitCoinTagBO bitCoinTagBO = BitCoinAssembler.BitCoinPO2BitCoinTagBO(bitCoinTags.get(0));
        List<BitCoinBO> bitCoinBOS = bitCoinMapper.queryBitCoinDataByCode(code).stream().map(BitCoinAssembler::BitCoinPO2BitCoinBO).toList();
        return BitCoinAssembler.buildBitCoinEchartsBOFromBitCoinBOsAndBitCoinTagBO(bitCoinBOS, bitCoinTagBO);
    }

    @Override
    public List<ImmutablePair<String, String>> getAllBranchBaseData() {
        List<BitCoinPO> bitCoinTags = bitCoinMapper.queryAllTags();

        if (CollectionUtils.isEmpty(bitCoinTags)) {
            throw new BusinessException("比特币没有分支数据");
        }

        return bitCoinTags
                .stream()
                .map(bitCoinPO ->
                        ImmutablePair.of(
                                bitCoinPO.getCode(),
                                String.join(
                                        "-",
                                        bitCoinPO.getCode(),
                                        bitCoinPO.getPlatform(),
                                        RegionAssembler.buildDetailRegionMessageFromISOCode(bitCoinPO.getRegion()),
                                        CurrencyAssembler.buildCurrencyDetailMessageFromCurrencyCode(bitCoinPO.getCurrency())
                                )
                        )
                ).toList();
    }

    @Override
    public ImmutablePair<List<?>, Class<?>> getAllFinancialBranchData(String code) {
        return ImmutablePair.of(
                bitCoinMapper.queryBitCoinDataByCode(code)
                        .stream()
                        .map(BitCoinAssembler::BitCoinPO2BitCoinBO)
                        .toList(),
                BitCoinBO.class
        );
    }
}
