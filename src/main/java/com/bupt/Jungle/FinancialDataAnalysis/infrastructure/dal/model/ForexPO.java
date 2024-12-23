package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForexPO {
    // 时间戳
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    private Timestamp ts;

    // 收盘价
    private Double closingPrice;

    // 开盘价
    private Double openingPrice;

    // 最高价
    private Double highestPrice;

    // 最低价
    private Double lowestPrice;

    // 涨跌幅
    private Double riseAndFall;

    // 基础货币所属的地区
    private String baseRegion;

    // 基础货币名称
    private String baseCurrency;

    // 基础货币中文名称
    private String baseCurrencyCn;

    // 报价货币所属地区
    private String quoteRegion;

    // 报价货币名称
    private String quoteCurrency;

    // 报价货币中文名称
    private String quoteCurrencyCn;
}
