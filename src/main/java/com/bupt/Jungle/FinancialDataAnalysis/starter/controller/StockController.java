package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.service.StockService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.StockAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.request.StockTableRequest;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CurrenciesResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.RegionsResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.StockTagPageResponse;
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
        return RegionAssembler.buildRegionsResponseFromRegionBOs(stockService.getAllRegion());
    }

    @GetMapping("/marketRegion/list")
    @Operation(summary = "获取所有交易所在国家|地区信息")
    public RegionsResponse getAllMarketRegion() {
        return RegionAssembler.buildRegionsResponseFromRegionBOs(stockService.getAllMarketRegion());
    }

    @GetMapping("/currency/list")
    @Operation(summary = "获取所有交易货币信息")
    public CurrenciesResponse getAllCurrency() {
        return CurrencyAssembler.buildCurrenciesResponseFromCurrencyBOs(stockService.getAllCurrency());
    }

    @PostMapping("/table/{current}/{pageSize}")
    @Operation(summary = "股票表格查询")
    @Parameters({
            @Parameter(name = "stockTableRequest", description = "查询条件"),
            @Parameter(name = "current", description = "当前页数", in = ParameterIn.PATH),
            @Parameter(name = "pageSize", description = "页大小", in = ParameterIn.PATH),
    })
    public StockTagPageResponse getStockTableData(@RequestBody StockTableRequest stockTableRequest,
                                                  @PathVariable(name = "current") Long current,
                                                  @PathVariable(name = "pageSize") Long pageSize) {
        return StockAssembler.StockTagPageBO2StockTagsPageResponse(stockService.getStockTagPage(
                stockTableRequest.getCode(),
                stockTableRequest.getPlatform(),
                stockTableRequest.getRegion(),
                stockTableRequest.getCurrency(),
                stockTableRequest.getName(),
                stockTableRequest.getMarketRegion(),
                current,
                pageSize
        ));
    }
}
