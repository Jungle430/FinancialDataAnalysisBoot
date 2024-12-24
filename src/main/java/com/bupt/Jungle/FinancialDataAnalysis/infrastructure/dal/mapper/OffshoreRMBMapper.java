package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// 离岸人民币
@Mapper
public interface OffshoreRMBMapper {
    List<String> queryAllBaseRegion();

    List<String> queryAllBaseCurrency();

    List<String> queryAllQuoteRegion();

    List<String> queryAllQuoteCurrency();
}
