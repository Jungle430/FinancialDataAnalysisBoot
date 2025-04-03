package com.bupt.Jungle.FinancialDataAnalysis.domain.service;


import com.bupt.Jungle.FinancialDataAnalysis.application.model.FinancialKindBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.service.AnalysisBaseService;
import com.bupt.Jungle.FinancialDataAnalysis.application.service.BaseDBMessageService;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.ServiceException;
import com.bupt.Jungle.FinancialDataAnalysis.domain.model.FinancialBranchRiseAndFallBO;
import com.bupt.Jungle.FinancialDataAnalysis.domain.model.FinancialKindRiseAndFallBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache.CacheService;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.gateway.RedisGateway;
import com.bupt.Jungle.FinancialDataAnalysis.starter.annotation.Performance;
import com.bupt.Jungle.FinancialDataAnalysis.util.GsonUtil;
import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;
import com.bupt.Jungle.FinancialDataAnalysis.util.model.PearsonMatrixWithAttr;
import com.bupt.Jungle.FinancialDataAnalysis.util.type.FinancialCalculateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException.NO_FINANCIAL_BRANCH_EXCEPTION;
import static com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil.calculatePearsonCorrelationCoefficient;

@Service
@Slf4j
public class FinancialDataAnalysisDomainService {
    private final Executor financialAnalysisTaskThreadPool;

    private final BaseDBMessageService baseDBMessageService;

    private final Map<String, AnalysisBaseService> analysisBaseServiceMap;

    private final CacheService cacheService;

    private final String analysisTwoFinancialDataKindHighestTaskKey = String.format(
            "%s.%s",
            FinancialDataAnalysisDomainService.class.getName(),
            "analysisTwoFinancialDataKindHighestTask"
    );

    private final String analysisTwoFinancialDataBranchHighestTaskKey = String.format(
            "%s.%s",
            FinancialDataAnalysisDomainService.class.getName(),
            "analysisTwoFinancialDataBranchHighestTask"
    );

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

    @Performance
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        financialAnalysisTaskThreadPool.execute(this::analysisTwoFinancialDataKindHighestTask);
        financialAnalysisTaskThreadPool.execute(this::analysisTwoFinancialDataBranchHighestTask);
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
            return calculatePearsonCorrelationCoefficient(
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
            financialKindRiseAndFallBOS = Objects.requireNonNull(
                    GsonUtil.jsonToList(
                            cacheService.get(analysisTwoFinancialDataKindHighestTaskKey),
                            FinancialKindRiseAndFallBO.class
                    ),
                    "数据为空"
            );
            return financialKindRiseAndFallBOS;
        } catch (Exception exception) {
            log.error("cacheService.get fail, key:{}", analysisTwoFinancialDataKindHighestTaskKey, exception);
            throw new ServiceException("缓存/定时任务有问题,key:" + analysisTwoFinancialDataKindHighestTaskKey);
        }
    }

    public List<FinancialBranchRiseAndFallBO> analysisTwoFinancialDataBranchHighest() {
        List<FinancialBranchRiseAndFallBO> financialBranchRiseAndFallBOS;
        try {
            log.info("cacheService.get start, key:{}", analysisTwoFinancialDataBranchHighestTaskKey);
            financialBranchRiseAndFallBOS = Objects.requireNonNull(
                    GsonUtil.jsonToList(
                            cacheService.get(analysisTwoFinancialDataBranchHighestTaskKey),
                            FinancialBranchRiseAndFallBO.class
                    ),
                    "数据为空"
            );
            return financialBranchRiseAndFallBOS;
        } catch (Exception e) {
            log.error("cacheService.get fail, key:{}", analysisTwoFinancialDataBranchHighestTaskKey, e);
            throw new ServiceException("缓存/定时任务有问题,key:" + analysisTwoFinancialDataBranchHighestTaskKey);
        }
    }

    @Performance
    @Async("financialAnalysisTaskThreadPool")
    @Scheduled(cron = "0 0 3 * * ?") // 凌晨执行
    public void analysisTwoFinancialDataBranchHighestTask() {
        log.info("task:{} start", analysisTwoFinancialDataBranchHighestTaskKey);
        List<String> kinds = analysisBaseServiceMap.keySet()
                .stream()
                .sorted(String::compareTo)
                .toList();
        int n = kinds.size();
        List<FinancialBranchRiseAndFallBO> financialBranchRiseAndFallBOS = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                String kind1 = kinds.get(i);
                String kind2 = kinds.get(j);
                List<String> codes1 = analysisBaseServiceMap.get(kind1).getAllFinancialBranchCode();
                List<String> codes2 = analysisBaseServiceMap.get(kind2).getAllFinancialBranchCode();
                for (String code1 : codes1) {
                    for (String code2 : codes2) {
                        if (!(kind1.equals(kind2) && code1.equals(code2))) {
                            double pearsonCorrelationCoefficient = calculatePearsonCorrelationCoefficient(
                                    analysisBaseServiceMap.get(kind1).getAllFinancialBranchData(code1),
                                    analysisBaseServiceMap.get(kind2).getAllFinancialBranchData(code2)
                            );
                            financialBranchRiseAndFallBOS.add(FinancialBranchRiseAndFallBO.builder()
                                    .financialKindBOX(kind1 + "-" + code1)
                                    .financialKindBOY(kind2 + "-" + code2)
                                    .riseAndFallPearsonCorrelationCoefficient(pearsonCorrelationCoefficient)
                                    .build());
                        }
                    }
                }
            }
        }
        financialBranchRiseAndFallBOS.sort(Comparator.comparingDouble(FinancialBranchRiseAndFallBO::getRiseAndFallPearsonCorrelationCoefficient));
        log.info("cacheService.set begin, key:{}", analysisTwoFinancialDataBranchHighestTaskKey);
        cacheService.set(
                analysisTwoFinancialDataBranchHighestTaskKey,
                GsonUtil.beanToJson(financialBranchRiseAndFallBOS),
                TimeUnit.DAYS.toSeconds(1) + TimeUnit.HOURS.toSeconds(1),
                TimeUnit.SECONDS
        );
        log.info("cacheService.set end, key:{}", analysisTwoFinancialDataBranchHighestTaskKey);
    }
}
