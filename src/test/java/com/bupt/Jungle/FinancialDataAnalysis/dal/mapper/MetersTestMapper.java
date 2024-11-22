package com.bupt.Jungle.FinancialDataAnalysis.dal.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.dal.model.MetersTestPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MetersTestMapper {
    List<MetersTestPO> find();

    MetersTestPO lastRow();
}
