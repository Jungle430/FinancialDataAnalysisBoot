package com.bupt.Jungle.FinancialDataAnalysis.application.model;

import com.bupt.Jungle.FinancialDataAnalysis.util.type.FinancialCalculateData;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockIndexBO implements FinancialCalculateData {
    /**
     * 时间戳
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Timestamp ts;

    /**
     * 收盘价
     */
    private Double closingPrice;

    /**
     * 开盘价
     */
    private Double openingPrice;

    /**
     * 最高价
     */
    private Double highestPrice;

    /**
     * 最低价
     */
    private Double lowestPrice;

    /**
     * 交易量
     */
    private Double tradeVolume;

    /**
     * 涨跌幅
     */
    private Double riseAndFall;


    @Override
    public Timestamp getFinancialDataTimestamp() {
        return this.ts;
    }

    @Override
    public List<String> getCalculationAttributeEnglishNames() {
        return Arrays.asList(
                "closing_price",
                "opening_price",
                "highest_price",
                "lowest_price",
                "trade_volume",
                "rise_and_fall"
        );
    }

    @Override
    public List<String> getCalculationAttributeSimplifiedChineseNames() {
        return Arrays.asList(
                "收盘价",
                "开盘价",
                "最高价",
                "最低价",
                "交易量",
                "涨跌幅"
        );
    }

    @Override
    public List<Double> getCalculationAttributeData() {
        return Arrays.asList(
                closingPrice,
                openingPrice,
                highestPrice,
                lowestPrice,
                tradeVolume,
                riseAndFall
        );
    }

    @Override
    public Double getRiseAndFallData() {
        return riseAndFall;
    }
}
