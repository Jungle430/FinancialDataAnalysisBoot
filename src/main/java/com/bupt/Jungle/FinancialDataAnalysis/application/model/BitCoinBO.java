package com.bupt.Jungle.FinancialDataAnalysis.application.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BitCoinBO {
    // 时间戳
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy/MM/dd")
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
}
