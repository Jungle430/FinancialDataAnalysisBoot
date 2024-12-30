package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.service.StockIndexService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.annotation.Performance;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.request.StockIndexTableRequest;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CurrenciesResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.RegionsResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.StockIndexEchartsResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.StockIndexTagPageResponse;
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
    public RegionsResponse getAllRegion() {
        return RegionAssembler.buildRegionsResponseFromRegionBOs(stockIndexService.getAllRegion());
    }

    @GetMapping("/currency/list")
    @Operation(summary = "获取所有交易货币信息")
    public CurrenciesResponse getAllCurrency() {
        return CurrencyAssembler.buildCurrenciesResponseFromCurrencyBOs(stockIndexService.getAllCurrency());
    }

    @PostMapping("/table/{current}/{pageSize}")
    @Operation(summary = "股票指数表格查询")
    @Parameters({
            @Parameter(name = "stockIndexTableRequest", description = "查询条件"),
            @Parameter(name = "current", description = "当前页数", in = ParameterIn.PATH),
            @Parameter(name = "pageSize", description = "页大小", in = ParameterIn.PATH),
    })
    public StockIndexTagPageResponse getStockIndexTableData(@RequestBody StockIndexTableRequest stockIndexTableRequest,
                                                            @PathVariable(name = "current") Long current,
                                                            @PathVariable(name = "pageSize") Long pageSize) {
        return StockAssembler.StockIndexTagPageBO2StockIndexTagPageResponse(stockIndexService.getStockIndexTagPage(
                stockIndexTableRequest.getCode(),
                stockIndexTableRequest.getPlatform(),
                stockIndexTableRequest.getRegion(),
                stockIndexTableRequest.getCurrency(),
                stockIndexTableRequest.getName(),
                current,
                pageSize
        ));
    }

    @Performance
    @GetMapping("/echarts/{code}")
    @Operation(summary = "股票指数图像查询")
    @Parameters({
            @Parameter(name = "code", description = "股票代码", in = ParameterIn.PATH)
    })
    public StockIndexEchartsResponse getStockIndexEchartsData(@PathVariable(name = "code") String code) {
        return StockAssembler.buildStockIndexEchartsResponseFromStockIndexEchartsBO(stockIndexService.getStockIndexEchartsData(code));
    }
}
