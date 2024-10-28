package com.bupt.Jungle.FinancialDataAnalysis.dao;

import lombok.Data;
import lombok.NonNull;

import java.sql.Timestamp;

@Data
public class MetersTest {
    @NonNull
    private Timestamp ts;

    private Float current;

    private Integer voltage;

    private Float phase;

    private Integer groupId;

    private String location;
}
