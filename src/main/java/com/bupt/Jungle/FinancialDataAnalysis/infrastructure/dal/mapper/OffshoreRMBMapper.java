package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.ForexPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// 离岸人民币
@Mapper
public interface OffshoreRMBMapper {
    List<String> queryAllBaseRegion();

    List<String> queryAllBaseCurrency();

    List<String> queryAllQuoteRegion();

    List<String> queryAllQuoteCurrency();

    List<ForexPO> queryOffshoreRMBDataByBaseRegionAndQuoteRegion(
            @Param("baseCurrency") String baseCurrency,
            @Param("quoteCurrency") String quoteCurrency
    );

    List<ForexPO> queryOffshoreRMBTagByBaseRegionAndQuoteRegion(
            @Param("baseCurrency") String baseCurrency,
            @Param("quoteCurrency") String quoteCurrency
    );

    List<ForexPO> queryOffshoreRMBTag(
            @Param("baseRegion") String baseRegion,
            @Param("baseCurrency") String baseCurrency,
            @Param("quoteRegion") String quoteRegion,
            @Param("quoteCurrency") String quoteCurrency,
            @Param("limitSize") Long limitSize,
            @Param("offSet") Long offSet
    );

    Long queryOffshoreTotalCount(
            @Param("baseRegion") String baseRegion,
            @Param("baseCurrency") String baseCurrency,
            @Param("quoteRegion") String quoteRegion,
            @Param("quoteCurrency") String quoteCurrency);
}
