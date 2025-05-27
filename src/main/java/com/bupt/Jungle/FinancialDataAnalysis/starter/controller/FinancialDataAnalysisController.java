package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;


import com.bupt.Jungle.FinancialDataAnalysis.application.service.AnalysisBaseService;
import com.bupt.Jungle.FinancialDataAnalysis.application.service.BaseDBMessageService;
import com.bupt.Jungle.FinancialDataAnalysis.domain.service.CommoditiesDomainService;
import com.bupt.Jungle.FinancialDataAnalysis.domain.service.FinancialDataAnalysisDomainService;
import com.bupt.Jungle.FinancialDataAnalysis.domain.service.StockIndexDomainService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.annotation.Performance;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.CommoditiesAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.FinancialDataAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.StockAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.request.AnalysisTwoFinancialBranchDataRequest;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.*;
import com.bupt.Jungle.FinancialDataAnalysis.util.model.PearsonMatrixWithAttr;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.bupt.Jungle.FinancialDataAnalysis.common.exception.BusinessException.NO_FINANCIAL_BRANCH_EXCEPTION;

@RestController
@RequestMapping("/financialDataAnalysis")
@Tag(name = "金融数据分析")
public class FinancialDataAnalysisController {
    private final BaseDBMessageService baseDBMessageService;

    private final Map<String, AnalysisBaseService> analysisBaseServiceMap;

    private final FinancialDataAnalysisDomainService financialDataAnalysisDomainService;

    private final StockIndexDomainService stockIndexDomainService;

    private final CommoditiesDomainService commoditiesDomainService;

    @Autowired
    public FinancialDataAnalysisController(
            BaseDBMessageService baseDBMessageService,
            Map<String, AnalysisBaseService> analysisBaseServiceMap,
            FinancialDataAnalysisDomainService financialDataAnalysisDomainService,
            StockIndexDomainService stockIndexDomainService,
            CommoditiesDomainService commoditiesDomainService
    ) {
        this.baseDBMessageService = baseDBMessageService;
        this.analysisBaseServiceMap = new ConcurrentHashMap<>(analysisBaseServiceMap);
        this.financialDataAnalysisDomainService = financialDataAnalysisDomainService;
        this.stockIndexDomainService = stockIndexDomainService;
        this.commoditiesDomainService = commoditiesDomainService;
    }

    @GetMapping("/kind/list")
    @Operation(summary = "获取所有金融数据种类信息")
    public FinancialKindResponse getAllFinancialKind() {
        return FinancialDataAssembler.FinancialKindBOs2Response(baseDBMessageService.getAllFinancialKind());
    }

    @GetMapping("/branch/{kind}")
    @Operation(summary = "获取某金融种类中所有分支的信息")
    @Parameters({
            @Parameter(name = "kind", description = "金融数据种类", in = ParameterIn.PATH)
    })
    public List<FinancialBranchItemResponse> getFinancialBranch(@PathVariable(name = "kind") String kind) {
        return FinancialDataAssembler.buildFinancialBranchItemResponsesFromList(
                Optional.ofNullable(analysisBaseServiceMap.get(kind))
                        .orElseThrow(NO_FINANCIAL_BRANCH_EXCEPTION)
                        .getAllBranchBaseData()
        );
    }

    @Performance
    @PostMapping("/analysisTwoFinancialDataBranch")
    @Operation(summary = "两种金融分支数据的相关性系数的计算和分析")
    @Parameters({
            @Parameter(name = "analysisTwoFinancialBranchDataRequest", description = "查询参数")
    })
    public TwoFinancialPearsonMatrixAnalysisResponse analysisTwoFinancialBranchData(
            @RequestBody AnalysisTwoFinancialBranchDataRequest analysisTwoFinancialBranchDataRequest
    ) {
        PearsonMatrixWithAttr pearsonMatrixWithAttr = financialDataAnalysisDomainService.analysisTwoFinancialDataBranch(
                analysisTwoFinancialBranchDataRequest.getKindX(),
                analysisTwoFinancialBranchDataRequest.getCodeX(),
                analysisTwoFinancialBranchDataRequest.getKindY(),
                analysisTwoFinancialBranchDataRequest.getCodeY()
        );
        return FinancialDataAssembler.buildTwoFinancialPearsonMatrixAnalysisResponseFromPearsonMatrixWithAttr(pearsonMatrixWithAttr);
    }

    @Performance
    @GetMapping("/analysisFinancialDataKinds")
    @Operation(summary = "金融数据的相关性系数的计算(由低到高排序)")
    public List<FinancialKindRiseAndFallResponse> analysisFinancialDataKinds() {
        return financialDataAnalysisDomainService.analysisTwoFinancialDataKindHighest()
                .stream()
                .map(FinancialDataAssembler::FinancialKindRiseAndFallBO2Response)
                .toList();
    }

    @Performance
    @GetMapping("/analysisRegionAndMarket")
    @Operation(summary = "分析市场与区域相关性(股票指数分析,由高到低排序)")
    public List<StockIndexRiseAndFallResponse> analysisRegionAndMarket() {
        return stockIndexDomainService.analysisStockIndexRelevance()
                .stream()
                .map(StockAssembler::StockIndexRiseAndFallBO2Response)
                .toList();
    }

    @Performance
    @GetMapping("/analysisTwoFinancialDataBranchHighestAndLowest")
    @Operation(summary = "金融数据相关性排行(由低到高)")
    public FinancialBranchRiseAndFallHighestAndLowest analysisTwoFinancialDataBranchHighestAndLowest() {
        FinancialBranchRiseAndFallHighestAndLowest financialBranchRiseAndFallHighestAndLowest = new FinancialBranchRiseAndFallHighestAndLowest();
        financialBranchRiseAndFallHighestAndLowest.setFinancialBranchRiseAndFallHighest(financialDataAnalysisDomainService.analysisTwoFinancialDataBranchHighest());
        financialBranchRiseAndFallHighestAndLowest.setFinancialBranchRiseAndFallLowest(financialDataAnalysisDomainService.analysisTwoFinancialDataBranchLowest());
        return financialBranchRiseAndFallHighestAndLowest;
    }

    @GetMapping("/commoditiesCurrencyNumber")
    @Operation(summary = "商品和货币的关系")
    public CommoditiesCurrencyNumberResponse getCommoditiesCurrencyNumber() {
        return CommoditiesAssembler.buildCommoditiesCurrencyNumberResponseFromBOs(commoditiesDomainService.getCommoditiesCurrencyNumber());
    }
}
