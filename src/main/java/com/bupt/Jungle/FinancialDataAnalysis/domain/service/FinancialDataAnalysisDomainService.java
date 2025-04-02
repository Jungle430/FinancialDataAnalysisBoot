package com.bupt.Jungle.FinancialDataAnalysis.domain.service;


import com.bupt.Jungle.FinancialDataAnalysis.application.model.FinancialKindBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.service.AnalysisBaseService;
import com.bupt.Jungle.FinancialDataAnalysis.application.service.BaseDBMessageService;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.domain.model.FinancialKindRiseAndFallBO;
import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;
import com.bupt.Jungle.FinancialDataAnalysis.util.model.PearsonMatrixWithAttr;
import com.bupt.Jungle.FinancialDataAnalysis.util.type.FinancialCalculateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException.NO_FINANCIAL_BRANCH_EXCEPTION;

@Service
@Slf4j
public class FinancialDataAnalysisDomainService {
    private final BaseDBMessageService baseDBMessageService;

    private final Map<String, AnalysisBaseService> analysisBaseServiceMap;

    @Autowired
    public FinancialDataAnalysisDomainService(
            Map<String, AnalysisBaseService> analysisBaseServiceMap,
            BaseDBMessageService baseDBMessageService
    ) {
        this.analysisBaseServiceMap = new ConcurrentHashMap<>(analysisBaseServiceMap);
        this.baseDBMessageService = baseDBMessageService;
    }

    public PearsonMatrixWithAttr analysisTwoFinancialDataBranch(
            String kindX,
            String codeX,
            String kindY,
            String codeY
    ) {
        List<FinancialCalculateData> analysisFinancialDataX = Optional
                .ofNullable(analysisBaseServiceMap.get(kindX))
                .orElseThrow(NO_FINANCIAL_BRANCH_EXCEPTION)
                .getAllFinancialBranchData(codeX);
        if (CollectionUtils.isEmpty(analysisFinancialDataX)) {
            throw new BusinessException(
                    "金融数据:{kind:%s,code:%s}为空",
                    kindX,
                    codeX
            );
        }

        List<FinancialCalculateData> analysisFinancialDataY = Optional
                .ofNullable(analysisBaseServiceMap.get(kindY))
                .orElseThrow(NO_FINANCIAL_BRANCH_EXCEPTION)
                .getAllFinancialBranchData(codeY);
        if (CollectionUtils.isEmpty(analysisFinancialDataY)) {
            throw new BusinessException(
                    "金融数据:{kind:%s,code:%s}为空",
                    kindY,
                    codeY
            );
        }

        try {
            return StockCalculateUtil.calculatePearsonMatrix(
                    analysisFinancialDataX,
                    analysisFinancialDataY
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

    private double analysisTwoFinancialDataKindRiseAndFall(
            String kindX,
            String kindY
    ) {
        List<FinancialCalculateData> analysisFinancialKindXData = Optional
                .ofNullable(analysisBaseServiceMap.get(kindX))
                .orElseThrow(NO_FINANCIAL_BRANCH_EXCEPTION)
                .getAllFinancialKindRiseAndFallAVG();
        if (CollectionUtils.isEmpty(analysisFinancialKindXData)) {
            throw new BusinessException(
                    "金融数据:{kind:%s}为空",
                    kindX
            );
        }

        List<FinancialCalculateData> analysisFinancialKindYData = Optional
                .ofNullable(analysisBaseServiceMap.get(kindY))
                .orElseThrow(NO_FINANCIAL_BRANCH_EXCEPTION)
                .getAllFinancialKindRiseAndFallAVG();
        if (CollectionUtils.isEmpty(analysisFinancialKindYData)) {
            throw new BusinessException(
                    "金融数据:{kind:%s}为空",
                    kindY
            );
        }

        try {
            return StockCalculateUtil.calculatePearsonCorrelationCoefficient(
                    analysisFinancialKindXData,
                    analysisFinancialKindYData
            );
        } catch (Exception e) {
            log.error("calculate error: {kindX:{},kindY:{}}",
                    kindX,
                    kindY,
                    e);
            throw new BusinessException("计算出错, 金融数据: {kindX:%s,kindY:%s}", kindX, kindY);
        }
    }

    public List<FinancialKindRiseAndFallBO> analysisTwoFinancialDataKindHighest() {
        List<FinancialKindBO> financialKindBOS = baseDBMessageService.getAllFinancialKind();
        int n = financialKindBOS.size();
        List<FinancialKindRiseAndFallBO> financialKindRiseAndFallBOS = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double pearsonCorrelationCoefficient = analysisTwoFinancialDataKindRiseAndFall(
                        financialKindBOS.get(i).getEn(),
                        financialKindBOS.get(j).getEn()
                );
                FinancialKindRiseAndFallBO financialKindRiseAndFallBO = new FinancialKindRiseAndFallBO();
                financialKindRiseAndFallBO.setFinancialKindBOX(financialKindBOS.get(i));
                financialKindRiseAndFallBO.setFinancialKindBOY(financialKindBOS.get(j));
                financialKindRiseAndFallBO.setRiseAndFallPearsonCorrelationCoefficient(pearsonCorrelationCoefficient);
                financialKindRiseAndFallBOS.add(financialKindRiseAndFallBO);
            }
        }
        return financialKindRiseAndFallBOS
                .stream()
                .sorted(Comparator.comparingDouble(FinancialKindRiseAndFallBO::getRiseAndFallPearsonCorrelationCoefficient))
                .toList();
    }
}
