package com.bupt.Jungle.FinancialDataAnalysis.starter.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisTwoFinancialDataRequest {
    /**
     * 金融数据X的种类
     */
    private String kindX;

    /**
     * 金融数据X分支的代码
     */
    private String codeX;

    /**
     * 金融数据Y的种类
     */
    private String kindY;

    /**
     * 金融数据Y分支的代码
     */
    private String codeY;
}
