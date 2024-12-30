package com.bupt.Jungle.FinancialDataAnalysis.starter.controller;

import com.bupt.Jungle.FinancialDataAnalysis.application.service.BitCoinService;
import com.bupt.Jungle.FinancialDataAnalysis.starter.annotation.Performance;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.BitCoinAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.CurrencyAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.assembler.RegionAssembler;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.request.BitCoinTableRequest;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.BitCoinEchartsResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.BitCoinTagPageResponse;
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
@RequestMapping("/bitCoin")
@Tag(name = "比特币")
public class BitCoinController {
    private final BitCoinService bitCoinService;

    @Autowired
    public BitCoinController(BitCoinService bitCoinService) {
        this.bitCoinService = bitCoinService;
    }

    @GetMapping("/region/list")
    @Operation(summary = "获取所有交易国家|地区信息")
    public RegionsResponse getAllRegion() {
        return RegionAssembler.buildRegionsResponseFromRegionBOs(bitCoinService.getAllRegion());
    }

    @GetMapping("/currency/list")
    @Operation(summary = "获取所有交易货币信息")
    public CurrenciesResponse getAllCurrency() {
        return CurrencyAssembler.buildCurrenciesResponseFromCurrencyBOs(bitCoinService.getAllCurrency());
    }

    @PostMapping("/table/{current}/{pageSize}")
    @Operation(summary = "比特币表格查询")
    @Parameters({
            @Parameter(name = "bitCoinTableRequest", description = "查询条件"),
            @Parameter(name = "current", description = "当前页数", in = ParameterIn.PATH),
            @Parameter(name = "pageSize", description = "页大小", in = ParameterIn.PATH),
    })
    public BitCoinTagPageResponse getBitCoinTableData(@RequestBody BitCoinTableRequest bitCoinTableRequest,
                                                      @PathVariable(name = "current") Long current,
                                                      @PathVariable(name = "pageSize") Long pageSize) {
        return BitCoinAssembler.BitCoinTagPageBO2BitCoinTagPageResponse(bitCoinService.getBitCoinTagPage(
                bitCoinTableRequest.getCode(),
                bitCoinTableRequest.getPlatform(),
                bitCoinTableRequest.getRegion(),
                bitCoinTableRequest.getCurrency(),
                current,
                pageSize
        ));
    }

    @Performance
    @GetMapping("/echarts/{code}")
    @Operation(summary = "比特币图像查询")
    @Parameters({
            @Parameter(name = "code", description = "比特币代码", in = ParameterIn.PATH)
    })
    public BitCoinEchartsResponse getBitCoinEchartsData(@PathVariable(name = "code") String code) {
        return BitCoinAssembler.buildBitCoinEchartsResponseFromBitCoinEchartsBO(bitCoinService.getBitCoinEchartsData(code));
    }
}
