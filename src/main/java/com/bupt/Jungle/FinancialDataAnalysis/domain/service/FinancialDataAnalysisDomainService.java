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
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.Result;
import com.bupt.Jungle.FinancialDataAnalysis.util.GsonUtil;
import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;
import com.bupt.Jungle.FinancialDataAnalysis.util.model.PearsonMatrixWithAttr;
import com.bupt.Jungle.FinancialDataAnalysis.util.type.FinancialCalculateData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public final String analysisTwoFinancialDataKindHighestTaskKey;

    public final String analysisTwoFinancialDataBranchHighestAndLowestTaskKey;

    public final String analysisTwoFinancialDataBranchHighestAndLowestTaskHighestKey;

    public final String analysisTwoFinancialDataBranchHighestAndLowestTaskLowestKey;

    private final Integer DEFAULT_ANALYSIS_TWO_FINANCIAL_DATA_BRANCH_HIGHEST_AND_LOWEST_TASK_SHOW_SIZE;

    private final String transformerWebIp;

    private final Integer transformerWebPort;

    private final RestTemplate restTemplate;

    @Autowired
    public FinancialDataAnalysisDomainService(
            Map<String, AnalysisBaseService> analysisBaseServiceMap,
            BaseDBMessageService baseDBMessageService,
            RedisGateway redisGateway,
            Executor financialAnalysisTaskThreadPool,
            @Value("${spring.application.name}") String applicationName,
            @Value("${financial-data.analysisTwoFinancialDataBranchHighestAndLowestTaskLowest.show-size}") Integer DEFAULT_ANALYSIS_TWO_FINANCIAL_DATA_BRANCH_HIGHEST_AND_LOWEST_TASK_SHOW_SIZE,
            @Value("${personal.transformer_predict.ip}") String transformerWebIp,
            @Value("${personal.transformer_predict.port}") Integer transformerWebPort,
            RestTemplate restTemplate
    ) {
        this.analysisBaseServiceMap = new ConcurrentHashMap<>(analysisBaseServiceMap);
        this.baseDBMessageService = baseDBMessageService;
        this.cacheService = redisGateway;
        this.financialAnalysisTaskThreadPool = financialAnalysisTaskThreadPool;
        this.DEFAULT_ANALYSIS_TWO_FINANCIAL_DATA_BRANCH_HIGHEST_AND_LOWEST_TASK_SHOW_SIZE = DEFAULT_ANALYSIS_TWO_FINANCIAL_DATA_BRANCH_HIGHEST_AND_LOWEST_TASK_SHOW_SIZE;
        this.analysisTwoFinancialDataKindHighestTaskKey = String.join(
                ".",
                applicationName,
                FinancialDataAnalysisDomainService.class.getName(),
                "analysisTwoFinancialDataKindHighestTask"
        );
        this.analysisTwoFinancialDataBranchHighestAndLowestTaskKey = String.join(
                ".",
                applicationName,
                FinancialDataAnalysisDomainService.class.getName(),
                "analysisTwoFinancialDataBranchHighestAndLowestTask"
        );
        this.analysisTwoFinancialDataBranchHighestAndLowestTaskHighestKey = String.join(
                ".",
                analysisTwoFinancialDataBranchHighestAndLowestTaskKey,
                "Highest"
        );
        this.analysisTwoFinancialDataBranchHighestAndLowestTaskLowestKey = String.join(
                ".",
                analysisTwoFinancialDataBranchHighestAndLowestTaskKey,
                "Lowest"
        );
        this.transformerWebIp = transformerWebIp;
        this.transformerWebPort = transformerWebPort;
        this.restTemplate = restTemplate;
    }

    @Performance
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        financialAnalysisTaskThreadPool.execute(this::analysisTwoFinancialDataKindHighestTask);
        financialAnalysisTaskThreadPool.execute(this::analysisTwoFinancialDataBranchHighestAndLowestTask);
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
        List<FinancialBranchRiseAndFallBO> financialBranchRiseAndFallHighestBOS;
        try {
            log.info("cacheService.get start, key:{}", analysisTwoFinancialDataBranchHighestAndLowestTaskHighestKey);
            financialBranchRiseAndFallHighestBOS = Objects.requireNonNull(
                    GsonUtil.jsonToList(
                            cacheService.get(analysisTwoFinancialDataBranchHighestAndLowestTaskHighestKey),
                            FinancialBranchRiseAndFallBO.class
                    ),
                    "数据为空"
            );
            return financialBranchRiseAndFallHighestBOS;
        } catch (Exception e) {
            log.error("cacheService.get fail, key:{}", analysisTwoFinancialDataBranchHighestAndLowestTaskHighestKey, e);
            throw new ServiceException("缓存/定时任务有问题,key:" + analysisTwoFinancialDataBranchHighestAndLowestTaskHighestKey);
        }
    }

    public List<FinancialBranchRiseAndFallBO> analysisTwoFinancialDataBranchLowest() {
        List<FinancialBranchRiseAndFallBO> financialBranchRiseAndFallLowestBOS;
        try {
            log.info("cacheService.get start, key:{}", analysisTwoFinancialDataBranchHighestAndLowestTaskLowestKey);
            financialBranchRiseAndFallLowestBOS = Objects.requireNonNull(
                    GsonUtil.jsonToList(
                            cacheService.get(analysisTwoFinancialDataBranchHighestAndLowestTaskLowestKey),
                            FinancialBranchRiseAndFallBO.class
                    ),
                    "数据为空"
            );
            return financialBranchRiseAndFallLowestBOS;
        } catch (Exception e) {
            log.error("cacheService.get fail, key:{}", analysisTwoFinancialDataBranchHighestAndLowestTaskLowestKey, e);
            throw new ServiceException("缓存/定时任务有问题,key:" + analysisTwoFinancialDataBranchHighestAndLowestTaskLowestKey);
        }
    }

    @Performance
    @Async("financialAnalysisTaskThreadPool")
    @Scheduled(cron = "0 0 3 * * ?") // 凌晨执行
    public void analysisTwoFinancialDataBranchHighestAndLowestTask() {
        log.info("task:{} start", analysisTwoFinancialDataBranchHighestAndLowestTaskKey);
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
        int financialBranchRiseAndFallBOSSize = financialBranchRiseAndFallBOS.size();

        List<FinancialBranchRiseAndFallBO> financialBranchRiseAndFallBOSLowest = financialBranchRiseAndFallBOS
                .subList(0, Math.min(DEFAULT_ANALYSIS_TWO_FINANCIAL_DATA_BRANCH_HIGHEST_AND_LOWEST_TASK_SHOW_SIZE, financialBranchRiseAndFallBOSSize));
        log.info("cacheService.set begin, key:{}", analysisTwoFinancialDataBranchHighestAndLowestTaskLowestKey);
        cacheService.set(
                analysisTwoFinancialDataBranchHighestAndLowestTaskLowestKey,
                GsonUtil.beanToJson(financialBranchRiseAndFallBOSLowest),
                TimeUnit.DAYS.toSeconds(1) + TimeUnit.HOURS.toSeconds(1),
                TimeUnit.SECONDS
        );
        log.info("cacheService.set end, key:{}", analysisTwoFinancialDataBranchHighestAndLowestTaskLowestKey);

        List<FinancialBranchRiseAndFallBO> financialBranchRiseAndFallBOSHighest = financialBranchRiseAndFallBOS.subList(
                Math.max(0, financialBranchRiseAndFallBOSSize - DEFAULT_ANALYSIS_TWO_FINANCIAL_DATA_BRANCH_HIGHEST_AND_LOWEST_TASK_SHOW_SIZE), financialBranchRiseAndFallBOSSize);

        log.info("cacheService.set begin, key:{}", analysisTwoFinancialDataBranchHighestAndLowestTaskHighestKey);
        cacheService.set(
                analysisTwoFinancialDataBranchHighestAndLowestTaskHighestKey,
                GsonUtil.beanToJson(financialBranchRiseAndFallBOSHighest),
                TimeUnit.DAYS.toSeconds(1) + TimeUnit.HOURS.toSeconds(1),
                TimeUnit.SECONDS
        );
        log.info("cacheService.set end, key:{}", analysisTwoFinancialDataBranchHighestAndLowestTaskHighestKey);
    }

    public List<Double> predictStock(String code) {
        String url = String.format("http://%s:%d/predict/%s", transformerWebIp, transformerWebPort, code);
        log.info("start http call for {}", url);
        ParameterizedTypeReference<Result<List<Double>>> typeRef =
                new ParameterizedTypeReference<>() {
                };
        ResponseEntity<Result<List<Double>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,       // GET 请求没有 request body，就传 null
                typeRef     // 把 typeRef 传给 RestTemplate，让它知道要解析成 Result<List<Double>>
        );
        Result<List<Double>> res = response.getBody();
        if (Objects.isNull(res)) {
            log.error("call http for {} fail, res is null", url);
            throw new BusinessException("预测出现问题，请稍后再试");
        }
        if (!res.isSuccess()) {
            log.error("call http for {} tail, success is fail", url);
            throw new BusinessException("预测出现问题，请稍后再试");
        }
        log.info("end http call for {}", url);
        return res.getData().subList(1, res.getData().size());
    }
}
