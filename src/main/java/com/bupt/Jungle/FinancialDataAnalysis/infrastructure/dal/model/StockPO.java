package com.bupt.Jungle.FinancialDataAnalysis.infrastructure.dal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

// 股票
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPO {
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

    // 交易量
    private Double tradeVolume;

    // 涨跌幅
    private Double riseAndFall;

    // 股票代码
    private String code;

    // 交易平台
    private String platform;

    // 公司所在地区
    private String region;

    // 交易货币
    private String currency;

    // 公司名称
    private String name;

    // 交易地区
    private String marketRegion;
}
