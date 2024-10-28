package com.bupt.Jungle.FinancialDataAnalysis.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.dao.MetersTest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MetersMapperTest {
    List<MetersTest> find();
}
