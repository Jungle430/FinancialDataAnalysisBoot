package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.service.OffshoreRMBService;
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
@RequestMapping("/offshoreRMB")
@Tag(name = "离岸人民币")
public class OffshoreRMBController {
    private final OffshoreRMBService offshoreRMBService;

    @Autowired
    public OffshoreRMBController(OffshoreRMBService offshoreRMBService) {
        this.offshoreRMBService = offshoreRMBService;
    }

    @GetMapping("/baseRegion/list")
    @Operation(summary = "获取所有基础货币所属国家|地区信息")
    public RegionsResponse getAllBaseRegion() {
        return RegionAssembler.buildRegionsResponseFromRegionBOs(offshoreRMBService.getAllBaseRegion());
    }

    @GetMapping("/baseCurrency/list")
    @Operation(summary = "获取所有基础货币信息")
    public CurrenciesResponse getAllBaseCurrency() {
        return CurrencyAssembler.buildCurrenciesResponseFromCurrencyBOs(offshoreRMBService.getAllBaseCurrency());
    }

    @GetMapping("/quoteRegion/list")
    @Operation(summary = "获取所有报价货币所属国家|地区信息")
    public RegionsResponse getAllQuoteRegion() {
        return RegionAssembler.buildRegionsResponseFromRegionBOs(offshoreRMBService.getAllQuoteRegion());
    }

    @GetMapping("/quoteCurrency/list")
    @Operation(summary = "获取所有报价基础货币信息")
    public CurrenciesResponse getAllQuoteCurrency() {
        return CurrencyAssembler.buildCurrenciesResponseFromCurrencyBOs(offshoreRMBService.getAllQuoteCurrency());
    }

    @PostMapping("/table/{current}/{pageSize}")
    @Operation(summary = "离岸人民币表格查询")
    @Parameters({
            @Parameter(name = "OffshoreRMBTableRequest", description = "查询条件"),
            @Parameter(name = "current", description = "当前页数", in = ParameterIn.PATH),
            @Parameter(name = "pageSize", description = "页大小", in = ParameterIn.PATH),
    })
    public ForexTagPageResponse getForexTableData(@RequestBody ForexTableRequest offshoreRMBTableRequest,
                                                  @PathVariable(name = "current") Long current,
                                                  @PathVariable(name = "pageSize") Long pageSize) {
        return ForexAssembler.ForexTagPageBO2ForexTagPageResponse(offshoreRMBService.getOffshoreRMBTagPage(
                offshoreRMBTableRequest.getBaseRegion(),
                offshoreRMBTableRequest.getBaseCurrency(),
                offshoreRMBTableRequest.getQuoteRegion(),
                offshoreRMBTableRequest.getQuoteCurrency(),
                current,
                pageSize
        ));
    }
}
