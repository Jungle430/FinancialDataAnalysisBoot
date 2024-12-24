package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.service.ForexService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.ForexAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.request.ForexTableRequest;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CurrenciesResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.ForexTagPageResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.RegionsResponse;
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

@RestController
@RequestMapping("/forex")
@Tag(name = "外汇")
public class ForexController {
    private final ForexService forexService;

    @Autowired
    public ForexController(ForexService forexService) {
        this.forexService = forexService;
    }

    @GetMapping("/baseRegion/list")
    @Operation(summary = "获取所有基础货币所属国家|地区信息")
    public RegionsResponse getAllBaseRegion() {
        return RegionAssembler.buildRegionsResponseFromRegionBOs(forexService.getAllBaseRegion());
    }

    @GetMapping("/baseCurrency/list")
    @Operation(summary = "获取所有基础货币信息")
    public CurrenciesResponse getAllBaseCurrency() {
        return CurrencyAssembler.buildCurrenciesResponseFromCurrencyBOs(forexService.getAllBaseCurrency());
    }

    @GetMapping("/quoteRegion/list")
    @Operation(summary = "获取所有报价货币所属国家|地区信息")
    public RegionsResponse getAllQuoteRegion() {
        return RegionAssembler.buildRegionsResponseFromRegionBOs(forexService.getAllQuoteRegion());
    }

    @GetMapping("/quoteCurrency/list")
    @Operation(summary = "获取所有报价基础货币信息")
    public CurrenciesResponse getAllQuoteCurrency() {
        return CurrencyAssembler.buildCurrenciesResponseFromCurrencyBOs(forexService.getAllQuoteCurrency());
    }

    @PostMapping("/table/{current}/{pageSize}")
    @Operation(summary = "外汇表格查询")
    @Parameters({
            @Parameter(name = "ForexTableRequest", description = "查询条件"),
            @Parameter(name = "current", description = "当前页数", in = ParameterIn.PATH),
            @Parameter(name = "pageSize", description = "页大小", in = ParameterIn.PATH),
    })
    public ForexTagPageResponse getForexTableData(@RequestBody ForexTableRequest forexTableRequest,
                                                  @PathVariable(name = "current") Long current,
                                                  @PathVariable(name = "pageSize") Long pageSize) {
        return ForexAssembler.ForexTagPageBO2ForexTagPageResponse(forexService.getForexTagPage(
                forexTableRequest.getBaseRegion(),
                forexTableRequest.getBaseCurrency(),
                forexTableRequest.getQuoteRegion(),
                forexTableRequest.getQuoteCurrency(),
                current,
                pageSize
        ));
    }
}
