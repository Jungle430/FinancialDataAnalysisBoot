package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.BitCoinPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BitCoinMapper {
    List<String> queryAllRegion();

    List<String> queryAllCurrency();

    List<BitCoinPO> queryBitCoinDataByCode(@Param("code") String code);

    List<BitCoinPO> queryBitCoinTagByCode(@Param("code") String code);

    List<BitCoinPO> queryBitCoinTag(
            @Param("code") String code,
            @Param("platform") String platform,
            @Param("region") String region,
            @Param("currency") String currency,
            @Param("limitSize") Long limitSize,
            @Param("offSet") Long offSet
    );

    Long queryBitCoinTagTotalCount(
            @Param("code") String code,
            @Param("platform") String platform,
            @Param("region") String region,
            @Param("currency") String currency
    );
}
