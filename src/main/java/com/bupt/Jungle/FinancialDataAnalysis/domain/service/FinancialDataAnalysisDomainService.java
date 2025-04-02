package com.bupt.Jungle.FinancialDataAnalysis.domain.service;


import com.bupt.Jungle.FinancialDataAnalysis.application.model.FinancialKindBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.service.AnalysisBaseService;
import com.bupt.Jungle.FinancialDataAnalysis.application.service.BaseDBMessageService;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.ServiceException;
import com.bupt.Jungle.FinancialDataAnalysis.domain.model.FinancialKindRiseAndFallBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache.CacheService;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.gateway.RedisGateway;
import com.bupt.Jungle.FinancialDataAnalysis.starter.annotation.Performance;
import com.bupt.Jungle.FinancialDataAnalysis.util.GsonUtil;
import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;
import com.bupt.Jungle.FinancialDataAnalysis.util.model.PearsonMatrixWithAttr;
import com.bupt.Jungle.FinancialDataAnalysis.util.type.FinancialCalculateData;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException.NO_FINANCIAL_BRANCH_EXCEPTION;

@Service
@Slf4j
public class FinancialDataAnalysisDomainService {
    private final Executor financialAnalysisTaskThreadPool;

    private final BaseDBMessageService baseDBMessageService;

    private final Map<String, AnalysisBaseService> analysisBaseServiceMap;

    private final CacheService cacheService;

    private final String analysisTwoFinancialDataKindHighestTaskKey = "FinancialDataAnalysisDomainService.analysisTwoFinancialDataKindHighestTask";

    @Autowired
    public FinancialDataAnalysisDomainService(
            Map<String, AnalysisBaseService> analysisBaseServiceMap,
            BaseDBMessageService baseDBMessageService,
            RedisGateway redisGateway,
            Executor financialAnalysisTaskThreadPool
    ) {
        this.analysisBaseServiceMap = new ConcurrentHashMap<>(analysisBaseServiceMap);
        this.baseDBMessageService = baseDBMessageService;
        this.cacheService = redisGateway;
        this.financialAnalysisTaskThreadPool = financialAnalysisTaskThreadPool;
    }

    @PostConstruct
    public void init() {
        financialAnalysisTaskThreadPool.execute(this::analysisTwoFinancialDataKindHighestTask);
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

    @Performance
    @Async("financialAnalysisTaskThreadPool")
    @Scheduled(cron = "0 0 1 * * ?") // 凌晨执行
    public void analysisTwoFinancialDataKindHighestTask() {
        log.info("task:{} start", analysisTwoFinancialDataKindHighestTaskKey);
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
        financialKindRiseAndFallBOS.sort(Comparator.comparingDouble(FinancialKindRiseAndFallBO::getRiseAndFallPearsonCorrelationCoefficient));
        log.info("cacheService.set start, key:{}",
                analysisTwoFinancialDataKindHighestTaskKey);
        cacheService.set(
                analysisTwoFinancialDataKindHighestTaskKey,
                GsonUtil.beanToJson(financialKindRiseAndFallBOS),
                TimeUnit.DAYS.toSeconds(1) + TimeUnit.MINUTES.toSeconds(1),
                TimeUnit.SECONDS
        );
        log.info("cacheService.set end, key:{}", analysisTwoFinancialDataKindHighestTaskKey);
    }

    public List<FinancialKindRiseAndFallBO> analysisTwoFinancialDataKindHighest() {
        List<FinancialKindRiseAndFallBO> financialKindRiseAndFallBOS;
        try {
            log.info("cacheService.get start, key:{}", analysisTwoFinancialDataKindHighestTaskKey);
            financialKindRiseAndFallBOS = GsonUtil.jsonToList(cacheService.get(analysisTwoFinancialDataKindHighestTaskKey), FinancialKindRiseAndFallBO.class);
        } catch (Exception exception) {
            log.error("cacheService.get fail, key:{}", analysisTwoFinancialDataKindHighestTaskKey, exception);
            throw new ServiceException("缓存/定时任务有问题,key:" + analysisTwoFinancialDataKindHighestTaskKey);
        }
        if (financialKindRiseAndFallBOS == null) {
            throw new ServiceException("缓存/定时任务有问题,无对应数据,key:" + analysisTwoFinancialDataKindHighestTaskKey);
        }
        return financialKindRiseAndFallBOS;
    }
}
