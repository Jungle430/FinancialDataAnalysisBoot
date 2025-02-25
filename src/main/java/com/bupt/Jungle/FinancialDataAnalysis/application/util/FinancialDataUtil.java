package com.bupt.Jungle.FinancialDataAnalysis.application.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FinancialDataUtil {
    private final Map<String, String> en_to_cn;

    @Autowired
    private FinancialDataUtil(@Value("#{${financial-data.en-to-cn}}") Map<String, String> en_to_cn) {
        this.en_to_cn = en_to_cn;
    }

    public String getEnToCn(String en) {
        return en_to_cn.get(en);
    }
}
