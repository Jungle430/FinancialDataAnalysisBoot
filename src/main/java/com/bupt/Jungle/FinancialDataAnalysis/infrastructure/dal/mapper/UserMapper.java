package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper;

import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserPO> queryUserByPhoneAndPassword(@Param("phone") String phone, @Param("password") String password);
}
