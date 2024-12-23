package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.service.ForexService;
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
}
