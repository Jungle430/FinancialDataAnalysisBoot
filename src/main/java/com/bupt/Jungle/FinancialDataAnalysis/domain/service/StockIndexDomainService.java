package com.bupt.Jungle.FinancialDataAnalysis.domain.service;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.StockIndexTagBO;
import com.bupt.Jungle.FinancialDataAnalysis.application.service.StockIndexService;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException;
import com.bupt.Jungle.FinancialDataAnalysis.common.exception.ServiceException;
import com.bupt.Jungle.FinancialDataAnalysis.domain.model.StockIndexRiseAndFallBO;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.cache.CacheService;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.mapper.StockIndexMapper;
import com.bupt.Jungle.FinancialDataAnalysis.infrastructure.gateway.RedisGateway;
import com.bupt.Jungle.FinancialDataAnalysis.starter.annotation.Performance;
import com.bupt.Jungle.FinancialDataAnalysis.util.GsonUtil;
import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;
import com.bupt.Jungle.FinancialDataAnalysis.util.type.FinancialCalculateData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class StockIndexDomainService {
    private final Executor financialAnalysisTaskThreadPool;

    private final StockIndexService stockIndexService;

    private final StockIndexMapper stockIndexMapper;

    private final CacheService cacheService;

    private final String analysisStockIndexRelevanceKey = String.format(
            "%s.%s", StockIndexDomainService.class.getName(), "analysisStockIndexRelevance"
    );

    @Autowired
    public StockIndexDomainService(
            StockIndexService stockIndexService,
            StockIndexMapper stockIndexMapper,
            RedisGateway redisGateway,
            Executor financialAnalysisTaskThreadPool
    ) {
        this.stockIndexService = stockIndexService;
        this.stockIndexMapper = stockIndexMapper;
        this.cacheService = redisGateway;
        this.financialAnalysisTaskThreadPool = financialAnalysisTaskThreadPool;
    }

    @Performance
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        financialAnalysisTaskThreadPool.execute(this::analysisStockIndexRelevanceTask);
    }

    @Performance
    @Async("financialAnalysisTaskThreadPool")
    @Scheduled(cron = "0 30 1 * * ?")
    public void analysisStockIndexRelevanceTask() {
        log.info("task:{} start", analysisStockIndexRelevanceKey);
        List<String> codes = stockIndexMapper.queryAllCode();
        int n = codes.size();
        List<StockIndexRiseAndFallBO> stockIndexRiseAndFallBOS = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                List<FinancialCalculateData> financialCalculateData1 = stockIndexService.getAllFinancialBranchData(codes.get(i));
                List<FinancialCalculateData> financialCalculateData2 = stockIndexService.getAllFinancialBranchData(codes.get(j));
                StockIndexTagBO stockIndexTagBO1 = stockIndexService.getStockIndexTag(codes.get(i));
                StockIndexTagBO stockIndexTagBO2 = stockIndexService.getStockIndexTag(codes.get(j));
                try {
                    double pearsonCorrelationCoefficient = StockCalculateUtil.calculatePearsonCorrelationCoefficient(financialCalculateData1, financialCalculateData2);
                    stockIndexRiseAndFallBOS.add(
                            StockIndexRiseAndFallBO.builder()
                                    .stockIndexTagBOX(stockIndexTagBO1)
                                    .stockIndexTagBOY(stockIndexTagBO2)
                                    .riseAndFallPearsonCorrelationCoefficient(pearsonCorrelationCoefficient)
                                    .build()
                    );
                } catch (Exception e) {
                    log.error("calculate error, params{code1:{}, code2:{}}", codes.get(i), codes.get(j), e);
                    throw new BusinessException("计算出错, 股票指数数据: {code1:%s,code2:%s}", codes.get(i), codes.get(j));
                }
            }
        }
        stockIndexRiseAndFallBOS.sort(Comparator.comparingDouble(StockIndexRiseAndFallBO::getRiseAndFallPearsonCorrelationCoefficient));
        log.info("cacheService.set start, key:{}", analysisStockIndexRelevanceKey);
        cacheService.set(
                analysisStockIndexRelevanceKey,
                GsonUtil.beanToJson(stockIndexRiseAndFallBOS),
                TimeUnit.DAYS.toSeconds(1) + TimeUnit.MINUTES.toSeconds(1),
                TimeUnit.SECONDS
        );
        log.info("cacheService.set end, key:{}", analysisStockIndexRelevanceKey);
    }

    public List<StockIndexRiseAndFallBO> analysisStockIndexRelevance() {
        List<StockIndexRiseAndFallBO> stockIndexRiseAndFallBOS;
        try {
            log.info("cacheService.get start, key: {}", analysisStockIndexRelevanceKey);
            stockIndexRiseAndFallBOS = Objects.requireNonNull(GsonUtil.jsonToList(
                            cacheService.get(analysisStockIndexRelevanceKey),
                            StockIndexRiseAndFallBO.class
                    ),
                    "数据为空");
            return stockIndexRiseAndFallBOS;
        } catch (Exception e) {
            log.error("cacheService.get fail, key:{}", analysisStockIndexRelevanceKey, e);
            throw new ServiceException("缓存/定时任务有问题,key:" + analysisStockIndexRelevanceKey);
        }
    }
}
