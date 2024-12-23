package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ForexMapper {
    List<String> queryAllBaseRegion();

    List<String> queryAllBaseCurrency();

    List<String> queryAllQuoteRegion();

    List<String> queryAllQuoteCurrency();
}
