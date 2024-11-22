package com.bupt.Jungle.FinancialDataAnalysis.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginBO {
    private Boolean success;

    private String token;

    private String errMessage;
}
