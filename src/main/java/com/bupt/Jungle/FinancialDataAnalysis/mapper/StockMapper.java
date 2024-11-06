package com.bupt.Jungle.FinancialDataAnalysis.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.dao.Stock;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockMapper {
    List<Stock> queryByCompany(String company, Integer size);
}
