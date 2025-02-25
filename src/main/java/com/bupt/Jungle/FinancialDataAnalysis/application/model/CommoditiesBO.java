package com.bupt.Jungle.FinancialDataAnalysis.application.model;

import com.bupt.Jungle.FinancialDataAnalysis.util.annotation.Attribute;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommoditiesBO {
    /**
     * 时间戳
     */
    @Getter
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

    @Attribute(english = "closing price", simplifiedChinese = "收盘价")
    public Double getClosingPrice() {
        return this.closingPrice;
    }

    @Attribute(english = "opening price", simplifiedChinese = "开盘价")
    public Double getOpeningPrice() {
        return this.openingPrice;
    }

    @Attribute(english = "highest price", simplifiedChinese = "最高价")
    public Double getHighestPrice() {
        return this.highestPrice;
    }

    @Attribute(english = "lowest price", simplifiedChinese = "最低价")
    public Double getLowestPrice() {
        return this.lowestPrice;
    }

    @Attribute(english = "trade volume", simplifiedChinese = "交易量")
    public Double getTradeVolume() {
        return this.tradeVolume;
    }

    @Attribute(english = "rise and fall", simplifiedChinese = "涨跌幅")
    public Double getRiseAndFall() {
        return this.riseAndFall;
    }
}
