package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;


import com.bupt.Jungle.FinancialDataAnalysis.application.service.AnalysisBaseService;
import com.bupt.Jungle.FinancialDataAnalysis.application.service.BaseDBMessageService;
import com.bupt.Jungle.FinancialDataAnalysis.domain.service.FinancialDataAnalysisDomainService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.annotation.Performance;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.FinancialDataAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.request.AnalysisTwoFinancialDataRequest;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.FinancialBranchItemResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.FinancialKindResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.TwoFinancialPearsonMatrixAnalysisResponse;
import com.bupt.Jungle.FinancialDataAnalysis.util.StockCalculateUtil;
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

    @Autowired
    public FinancialDataAnalysisController(
            BaseDBMessageService baseDBMessageService,
            Map<String, AnalysisBaseService> analysisBaseServiceMap,
            FinancialDataAnalysisDomainService financialDataAnalysisDomainService
    ) {
        this.baseDBMessageService = baseDBMessageService;
        this.analysisBaseServiceMap = new ConcurrentHashMap<>(analysisBaseServiceMap);
        this.financialDataAnalysisDomainService = financialDataAnalysisDomainService;
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
    @PostMapping("/analysisTwoFinancialData")
    @Operation(summary = "两种金融分支数据的相关性系数的计算和分析")
    @Parameters({
            @Parameter(name = "analysisTwoFinancialDataRequest", description = "查询参数")
    })
    public TwoFinancialPearsonMatrixAnalysisResponse analysisTwoFinancialData(
            @RequestBody AnalysisTwoFinancialDataRequest analysisTwoFinancialDataRequest
    ) {
        StockCalculateUtil.PearsonMatrixWithAttr pearsonMatrixWithAttr = financialDataAnalysisDomainService.analysisTwoFinancialData(
                analysisTwoFinancialDataRequest.getKindX(),
                analysisTwoFinancialDataRequest.getCodeX(),
                analysisTwoFinancialDataRequest.getKindY(),
                analysisTwoFinancialDataRequest.getCodeY()
        );
        return TwoFinancialPearsonMatrixAnalysisResponse.builder()
                .attributesX(pearsonMatrixWithAttr.getAttributesX())
                .attributesY(pearsonMatrixWithAttr.getAttributesY())
                .pearsonMatrix(pearsonMatrixWithAttr.getPearsonMatrix())
                .build();
    }
}
