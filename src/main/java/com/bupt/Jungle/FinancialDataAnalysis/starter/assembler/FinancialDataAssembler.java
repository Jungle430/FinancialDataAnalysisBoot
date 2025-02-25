package com.bupt.Jungle.FinancialDataAnalysis.starter.assembler;

import com.bupt.Jungle.FinancialDataAnalysis.application.model.FinancialKindBO;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.FinancialBranchItemResponse;
import com.bupt.Jungle.FinancialDataAnalysis.starter.model.response.FinancialKindResponse;
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
}
