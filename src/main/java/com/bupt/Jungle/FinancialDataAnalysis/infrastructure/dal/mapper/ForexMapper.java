package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.ForexPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ForexMapper {
    List<String> queryAllBaseRegion();

    List<String> queryAllBaseCurrency();

    List<String> queryAllQuoteRegion();

    List<String> queryAllQuoteCurrency();

    List<ForexPO> queryForexRiseAndFallAVGWithOffshoreRMB();

    List<ForexPO> queryForexDataByBaseRegionAndQuoteRegion(
            @Param("baseCurrency") String baseCurrency,
            @Param("quoteCurrency") String quoteCurrency
    );

    /**
     * 为金融数据分析计算准备的SQL,这个里面不再区分基础外汇和离岸人民币
     */
    List<ForexPO> queryForexDataByBaseRegionAndQuoteRegionWithOffshoreRMB(
            @Param("baseCurrency") String baseCurrency,
            @Param("quoteCurrency") String quoteCurrency
    );

    List<ForexPO> queryForexTagByBaseRegionAndQuoteRegion(
            @Param("baseCurrency") String baseCurrency,
            @Param("quoteCurrency") String quoteCurrency
    );

    List<ForexPO> queryForexTag(
            @Param("baseRegion") String baseRegion,
            @Param("baseCurrency") String baseCurrency,
            @Param("quoteRegion") String quoteRegion,
            @Param("quoteCurrency") String quoteCurrency,
            @Param("limitSize") Long limitSize,
            @Param("offSet") Long offSet
    );

    Long queryForexTagTotalCount(
            @Param("baseRegion") String baseRegion,
            @Param("baseCurrency") String baseCurrency,
            @Param("quoteRegion") String quoteRegion,
            @Param("quoteCurrency") String quoteCurrency);

    List<ForexPO> queryAllTags();
}
