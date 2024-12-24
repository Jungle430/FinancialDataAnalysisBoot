package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.service.OffshoreRMBService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CurrenciesResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.RegionsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
