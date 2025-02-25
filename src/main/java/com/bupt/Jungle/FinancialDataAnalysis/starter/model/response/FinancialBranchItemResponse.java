package com.bupt.Jungle.FinancialDataAnalysis.starter.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialBranchItemResponse {
    /**
     * 金融分支代码
     */
    private String code;

    /**
     * 金融分支信息
     */
    private String msg;
}
