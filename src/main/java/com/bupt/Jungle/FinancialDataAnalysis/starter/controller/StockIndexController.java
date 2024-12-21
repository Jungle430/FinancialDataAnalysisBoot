package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.service.StockIndexService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CurrenciesResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.RegionsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.StockAssembler;


@RestController
@RequestMapping("/stockIndex")
@Tag(name = "股票指数")
public class StockIndexController {
    private final StockIndexService stockIndexService;

    @Autowired
    public StockIndexController(StockIndexService stockIndexService) {
        this.stockIndexService = stockIndexService;
    }

    @GetMapping("/region/list")
    @Operation(summary = "获取所有交易国家|地区信息")
    public RegionsResponse getAllRegions() {
        return StockAssembler.buildRegionsResponseFromRegionBOs(stockIndexService.getAllRegions());
    }

    @GetMapping("/currency/list")
    @Operation(summary = "获取所有交易货币信息")
    public CurrenciesResponse getAllCurrency() {
        return StockAssembler.buildCurrenciesResponseFromCurrencyBOs(stockIndexService.getAllCurrency());
    }
}
