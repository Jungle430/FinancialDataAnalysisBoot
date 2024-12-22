package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.service.BondsService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.BondsAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.StockAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.request.BondsTableRequest;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.BondsTagPageResponse;
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
@RequestMapping("/bonds")
@Tag(name = "债券")
public class BondsController {
    private final BondsService bondsService;

    @Autowired
    public BondsController(BondsService bondsService) {
        this.bondsService = bondsService;
    }

    @GetMapping("/region/list")
    @Operation(summary = "获取所有交易国家|地区信息")
    public RegionsResponse getAllRegion() {
        return StockAssembler.buildRegionsResponseFromRegionBOs(bondsService.getAllRegion());
    }

    @GetMapping("/currency/list")
    @Operation(summary = "获取所有交易货币信息")
    public CurrenciesResponse getAllCurrency() {
        return StockAssembler.buildCurrenciesResponseFromCurrencyBOs(bondsService.getAllCurrency());
    }

    @PostMapping("/table/{current}/{pageSize}")
    @Operation(summary = "债券表格查询")
    @Parameters({
            @Parameter(name = "bondsTableRequest", description = "查询条件"),
            @Parameter(name = "current", description = "当前页数", in = ParameterIn.PATH),
            @Parameter(name = "pageSize", description = "页大小", in = ParameterIn.PATH),
    })
    public BondsTagPageResponse getBitCoinTableData(@RequestBody BondsTableRequest bondsTableRequest,
                                                    @PathVariable(name = "current") Long current,
                                                    @PathVariable(name = "pageSize") Long pageSize) {
        return BondsAssembler.BondsTagPageBO2BondsTagPageResponse(bondsService.getBondsTagPage(
                bondsTableRequest.getCode(),
                bondsTableRequest.getPlatform(),
                bondsTableRequest.getRegion(),
                bondsTableRequest.getCurrency(),
                bondsTableRequest.getName(),
                current,
                pageSize
        ));
    }
}
