package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.repository;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.UserBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.assembler.UserAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.UserMapper;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model.UserPO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final UserMapper userMapper;

    @Autowired
    public UserRepository(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserBO queryUserByPhoneAndPassword(String phone, String password) {
        List<UserPO> userPOList = userMapper.queryUserByPhoneAndPassword(phone, password);
        if (CollectionUtils.isEmpty(userPOList)) {
            return null;
        }
        return UserAssembler.UserPO2BO(userPOList.get(0));
    }
}
