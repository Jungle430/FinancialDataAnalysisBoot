package com.bupt.Jungle.FinancialDataAnalysis.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.dao.DO.MetersTestPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MetersTestMapper {
    List<MetersTestPO> find();

    MetersTestPO lastRow();
}
