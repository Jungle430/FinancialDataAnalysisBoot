package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.StockIndexPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StockIndexMapper {
    List<String> queryAllRegion();

    List<String> queryAllCurrency();

    List<StockIndexPO> queryStockIndexRiseAndFallAVG();

    List<String> queryAllCode();

    List<StockIndexPO> queryStockIndexDataByCode(@Param("code") String code);

    List<StockIndexPO> queryStockIndexTagByCode(@Param("code") String code);

    List<StockIndexPO> queryStockIndexTag(
            @Param("code") String code,
            @Param("platform") String platform,
            @Param("region") String region,
            @Param("currency") String currency,
            @Param("name") String name,
            @Param("limitSize") Long limitSize,
            @Param("offSet") Long offSet
    );

    Long queryStockIndexTagTotalCount(
            @Param("code") String code,
            @Param("platform") String platform,
            @Param("region") String region,
            @Param("currency") String currency,
            @Param("name") String name
    );

    List<StockIndexPO> queryAllTags();
}
