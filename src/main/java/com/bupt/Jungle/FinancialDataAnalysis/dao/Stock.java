package com.bupt.Jungle.FinancialDataAnalysis.dao;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Stock {
    private Timestamp date;

    private Double closing_price;

    private Double opening_price;

    private Double highest_price;

    private Double lowest_price;

    private Double trading_volume;

    private Double rise_and_fall;

    private String company;
}
