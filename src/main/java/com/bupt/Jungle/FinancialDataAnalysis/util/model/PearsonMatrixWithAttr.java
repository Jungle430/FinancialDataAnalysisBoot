package com.bupt.Jungle.FinancialDataAnalysis.util.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 皮尔逊矩阵返回包装
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PearsonMatrixWithAttr {
    /**
     * x属性列表
     */
    private List<String> attributesX;

    /**
     * y属性列表
     */
    private List<String> attributesY;

    /**
     * 皮尔逊矩阵
     */
    private double[][] pearsonMatrix;
}
