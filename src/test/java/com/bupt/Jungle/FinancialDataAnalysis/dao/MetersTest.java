package com.bupt.Jungle.FinancialDataAnalysis.dao;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MetersTest {
    private Timestamp ts;

    private Float current;

    private Integer voltage;

    private Float phase;

    private Integer groupId;

    private String location;
}
