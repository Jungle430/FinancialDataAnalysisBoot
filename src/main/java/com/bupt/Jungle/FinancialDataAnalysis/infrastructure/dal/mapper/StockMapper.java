package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.StockPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockMapper {
    List<String> queryAllRegion();

    List<String> queryAllCurrency();

    List<String> queryAllMarketRegion();

    List<StockPO> queryStockTag(
            String code,
            String platform,
            String region,
            String currency,
            String name,
            String marketRegion,
            Long limitSize,
            Long offSet
    );

    Long queryStockTagTotalCount(
            String code,
            String platform,
            String region,
            String currency,
            String name,
            String marketRegion
    );
}
