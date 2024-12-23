package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.service.CommoditiesService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.CommoditiesAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.request.CommoditiesTableRequest;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CommoditiesTagPageResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.CurrenciesResponse;
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
@RequestMapping("/commodities")
@Tag(name = "大宗商品")
public class CommoditiesController {
    private final CommoditiesService commoditiesService;

    @Autowired
    public CommoditiesController(CommoditiesService commoditiesService) {
        this.commoditiesService = commoditiesService;
    }

    @GetMapping("/region/list")
    @Operation(summary = "获取所有交易国家|地区信息")
    public RegionsResponse getAllRegion() {
        return RegionAssembler.buildRegionsResponseFromRegionBOs(commoditiesService.getAllRegion());
    }

    @GetMapping("/currency/list")
    @Operation(summary = "获取所有交易货币信息")
    public CurrenciesResponse getAllCurrency() {
        return CurrencyAssembler.buildCurrenciesResponseFromCurrencyBOs(commoditiesService.getAllCurrency());
    }

    @PostMapping("/table/{current}/{pageSize}")
    @Operation(summary = "大宗商品表格查询")
    @Parameters({
            @Parameter(name = "commoditiesTableRequest", description = "查询条件"),
            @Parameter(name = "current", description = "当前页数", in = ParameterIn.PATH),
            @Parameter(name = "pageSize", description = "页大小", in = ParameterIn.PATH),
    })
    public CommoditiesTagPageResponse getCommoditiesTableData(@RequestBody CommoditiesTableRequest commoditiesTableRequest,
                                                              @PathVariable(name = "current") Long current,
                                                              @PathVariable(name = "pageSize") Long pageSize) {
        return CommoditiesAssembler.CommoditiesTagPageBO2CommoditiesTagPageResponse(commoditiesService.getCommoditiesTagPage(
                commoditiesTableRequest.getCode(),
                commoditiesTableRequest.getPlatform(),
                commoditiesTableRequest.getRegion(),
                commoditiesTableRequest.getCurrency(),
                commoditiesTableRequest.getName(),
                current,
                pageSize
        ));
    }
}
