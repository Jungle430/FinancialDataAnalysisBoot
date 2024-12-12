package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.user.StockService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.StockAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CurrenciesResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.RegionsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
@Tag(name = "股票")
public class StockController {
    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/region/list")
    @Operation(summary = "获取所有公司所在国家|地区信息")
    public RegionsResponse getAllRegion() {
        return StockAssembler.BuildRegionsResponseFromStrings(stockService.getAllRegion());
    }

    @GetMapping("/marketRegion/list")
    @Operation(summary = "获取所有交易所在国家|地区信息")
    public RegionsResponse getAllMarketRegion() {
        return StockAssembler.BuildRegionsResponseFromStrings(stockService.getAllMarketRegion());
    }

    @GetMapping("/currency/list")
    @Operation(summary = "获取所有交易货币信息")
    public CurrenciesResponse getAllCurrency() {
        return StockAssembler.BuildCurrenciesResponseFromStrings(stockService.getAllCurrency());
    }
}
