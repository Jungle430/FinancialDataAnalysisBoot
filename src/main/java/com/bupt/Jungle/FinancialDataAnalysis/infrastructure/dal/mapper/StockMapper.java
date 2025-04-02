package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.StockPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StockMapper {
    List<String> queryAllRegion();

    List<String> queryAllCurrency();

    List<String> queryAllMarketRegion();

    List<StockPO> queryStockRiseAndFallAVG();

    List<StockPO> queryStockDataByCode(@Param("code") String code);

    List<StockPO> queryStockTagByCode(@Param("code") String code);

    List<StockPO> queryStockTag(
            @Param("code") String code,
            @Param("platform") String platform,
            @Param("region") String region,
            @Param("currency") String currency,
            @Param("name") String name,
            @Param("marketRegion") String marketRegion,
            @Param("limitSize") Long limitSize,
            @Param("offSet") Long offSet
    );

    Long queryStockTagTotalCount(
            @Param("code") String code,
            @Param("platform") String platform,
            @Param("region") String region,
            @Param("currency") String currency,
            @Param("name") String name,
            @Param("marketRegion") String marketRegion
    );

    List<StockPO> queryAllTags();
}
