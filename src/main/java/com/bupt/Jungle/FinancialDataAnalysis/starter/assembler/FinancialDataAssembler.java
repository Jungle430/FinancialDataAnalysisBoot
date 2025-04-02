package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.FinancialKindBO;
import com.bupt.Jungle.FinancialDataAnalysis.domain.model.FinancialKindRiseAndFallBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.FinancialBranchItemResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.FinancialKindResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.FinancialKindRiseAndFallResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.TwoFinancialPearsonMatrixAnalysisResponse;
import com.bupt.Jungle.FinancialDataAnalysis.util.model.PearsonMatrixWithAttr;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.List;

public class FinancialDataAssembler {
    public static FinancialKindResponse FinancialKindBOs2Response(List<FinancialKindBO> financialKindBOs) {
        return FinancialKindResponse
                .builder()
                .kinds(financialKindBOs)
                .build();
    }

    public static List<FinancialBranchItemResponse> buildFinancialBranchItemResponsesFromList(List<ImmutablePair<String, String>> list) {
        List<FinancialBranchItemResponse> financialBranchItemResponses = new ArrayList<>(list.size());

        list.forEach((pair) ->
                financialBranchItemResponses.add(
                        FinancialBranchItemResponse
                                .builder()
                                .code(pair.getLeft())
                                .msg(pair.getRight())
                                .build()
                )
        );
        return financialBranchItemResponses;
    }

    public static TwoFinancialPearsonMatrixAnalysisResponse buildTwoFinancialPearsonMatrixAnalysisResponseFromPearsonMatrixWithAttr(PearsonMatrixWithAttr pearsonMatrixWithAttr) {
        TwoFinancialPearsonMatrixAnalysisResponse twoFinancialPearsonMatrixAnalysisResponse = new TwoFinancialPearsonMatrixAnalysisResponse();
        twoFinancialPearsonMatrixAnalysisResponse.setAttributesX(pearsonMatrixWithAttr.getAttributesX());
        twoFinancialPearsonMatrixAnalysisResponse.setAttributesY(pearsonMatrixWithAttr.getAttributesY());
        twoFinancialPearsonMatrixAnalysisResponse.setPearsonMatrix(pearsonMatrixWithAttr.getPearsonMatrix());
        return twoFinancialPearsonMatrixAnalysisResponse;
    }

    public static FinancialKindRiseAndFallResponse FinancialKindRiseAndFallBO2Response(FinancialKindRiseAndFallBO financialKindRiseAndFallBO) {
        FinancialKindRiseAndFallResponse financialKindRiseAndFallResponse = new FinancialKindRiseAndFallResponse();
        financialKindRiseAndFallResponse.setFinancialKindX(financialKindRiseAndFallBO.getFinancialKindBOX());
        financialKindRiseAndFallResponse.setFinancialKindY(financialKindRiseAndFallBO.getFinancialKindBOY());
        financialKindRiseAndFallResponse.setRiseAndFallPearsonCorrelationCoefficient(financialKindRiseAndFallBO.getRiseAndFallPearsonCorrelationCoefficient());
        return financialKindRiseAndFallResponse;
    }
}
