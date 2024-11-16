package com.bupt.Jungle.FinancialDataAnalysis.dao.DO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetersTestDO {
    private Timestamp ts;

    private Float current;

    private Integer voltage;

    private Float phase;

    private Integer groupId;

    private String location;
}
