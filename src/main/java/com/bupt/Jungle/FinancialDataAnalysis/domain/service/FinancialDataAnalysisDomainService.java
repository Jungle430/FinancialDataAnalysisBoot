package com.bupt.Jungle.FinancialDataAnalysis.domain.service;


import com.bupt.Jungle.FinancialDataAnalysis.application.service.AnalysisBaseService;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException.NO_FINANCIAL_BRANCH_EXCEPTION;

@Service
@Slf4j
public class FinancialDataAnalysisDomainService {
    private final Map<String, AnalysisBaseService> analysisBaseServiceMap;

    @Autowired
    public FinancialDataAnalysisDomainService(Map<String, AnalysisBaseService> analysisBaseServiceMap) {
        this.analysisBaseServiceMap = new ConcurrentHashMap<>(analysisBaseServiceMap);
    }

    public StockCalculateUtil.PearsonMatrixWithAttr analysisTwoFinancialData(
            String kindX,
            String codeX,
            String kindY,
            String codeY
    ) {
        ImmutablePair<List<?>, Class<?>> analysisFinancialDataX = Optional
                .ofNullable(analysisBaseServiceMap.get(kindX))
                .orElseThrow(NO_FINANCIAL_BRANCH_EXCEPTION)
                .getAllFinancialBranchData(codeX);
        if (CollectionUtils.isEmpty(analysisFinancialDataX.getLeft())) {
            throw new BusinessException(
                    "金融数据:{kind:%s,code:%s}为空",
                    kindX,
                    codeX
            );
        }

        ImmutablePair<List<?>, Class<?>> analysisFinancialDataY = Optional
                .ofNullable(analysisBaseServiceMap.get(kindY))
                .orElseThrow(NO_FINANCIAL_BRANCH_EXCEPTION)
                .getAllFinancialBranchData(codeY);
        if (CollectionUtils.isEmpty(analysisFinancialDataY.getLeft())) {
            throw new BusinessException(
                    "金融数据:{kind:%s,code:%s}为空",
                    kindY,
                    codeY
            );
        }

        try {
            return StockCalculateUtil.calculatePearsonMatrix(
                    analysisFinancialDataX.getLeft(), analysisFinancialDataX.getRight(),
                    analysisFinancialDataY.getLeft(), analysisFinancialDataY.getRight()
            );
        } catch (Exception e) {
            log.error("calculate error: {kindX:{},codeX:{},kindY:{},codeY:{}}",
                    kindX,
                    codeX,
                    kindY,
                    codeY,
                    e);
            throw new BusinessException("计算出错, 金融数据: {kindX:%s,codeX:%s,kindY:%s,codeY:%s}", kindX, codeX, kindY, codeY);
        }
    }
}
