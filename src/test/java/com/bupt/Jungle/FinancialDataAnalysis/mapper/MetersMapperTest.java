package com.bupt.Jungle.FinancialDataAnalysis.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.dao.MetersTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MetersMapperTest {
    List<MetersTest> find();

    Integer create(@Param("meter") MetersTest metersTest, @Param("tableName") String tableName);

    Integer save(@Param("meter") MetersTest metersTest, @Param("tableName") String tableName);

    MetersTest lastRow();
}
