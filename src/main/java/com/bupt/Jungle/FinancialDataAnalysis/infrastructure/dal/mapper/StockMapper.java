package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockMapper {
    List<String> queryAllRegion();

    List<String> queryAllCurrency();

    List<String> queryAllMarketRegion();
}
