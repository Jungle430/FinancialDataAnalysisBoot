package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.BondsPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BondsMapper {
    List<String> queryAllRegion();

    List<String> queryAllCurrency();

    List<BondsPO> queryBondsDataByCode(@Param("code") String code);

    List<BondsPO> queryBondsTagByCode(@Param("code") String code);

    List<BondsPO> queryBondsTag(
            @Param("code") String code,
            @Param("platform") String platform,
            @Param("region") String region,
            @Param("currency") String currency,
            @Param("name") String name,
            @Param("limitSize") Long limitSize,
            @Param("offSet") Long offSet
    );

    Long queryBondsTagTotalCount(
            @Param("code") String code,
            @Param("platform") String platform,
            @Param("region") String region,
            @Param("currency") String currency,
            @Param("name") String name
    );
}
