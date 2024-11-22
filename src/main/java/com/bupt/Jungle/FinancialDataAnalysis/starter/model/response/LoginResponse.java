package com.bupt.Jungle.FinancialDataAnalysis.starter.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Boolean success;

    private String token;

    private String errMessage;
}
