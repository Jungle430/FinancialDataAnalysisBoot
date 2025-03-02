package com.bupt.Jungle.FinancialDataAnalysis.application.service;

import com.bupt.Jungle.FinancialDataAnalysis.util.type.FinancialCalculateData;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;

/**
 * @author Jungle
 * <p>
 * 金融数据分析基本功能通用服务,使用策略模式注入,Map注入<b>(注意场景需不需要线程安全来选择Map的实现)</b>
 */
public interface AnalysisBaseService {
    /**
     * 获取一个金融种类下所有金融分支的信息
     *
     * @return left: 金融分支代码 right: 金融分支基本信息
     */
    List<ImmutablePair<String, String>> getAllBranchBaseData();

    /**
     * @return 获取一个金融分支下的所有数据
     */
    List<FinancialCalculateData> getAllFinancialBranchData(String code);
}
