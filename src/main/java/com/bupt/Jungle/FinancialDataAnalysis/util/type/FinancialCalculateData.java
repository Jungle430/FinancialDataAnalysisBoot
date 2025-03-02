package com.bupt.Jungle.FinancialDataAnalysis.util.type;

import java.sql.Timestamp;
import java.util.List;

/**
 * 此接口定义了用于后续金融数据计算所需的基本方法,旨在统一数据访问方式
 * <p>
 * 避免因使用反射机制而导致的方法滥用问题,同时提升系统性能
 * <p>
 * 实现该接口的类应确保各方法返回数据的一致性和准确性
 */
public interface FinancialCalculateData {
    /**
     * @return 获取当前金融数据的时间戳
     * <p>
     * <pre>
     * {@code
     * @Override
     * public Timestamp getFinancialDataTimestamp() {
     *      return this.ts;
     * }
     * }</pre>
     */
    Timestamp getFinancialDataTimestamp();

    /**
     * 获取用于计算的金融属性的英文字段名称列表
     * <p>
     * 这些名称应与后续获取计算属性数据的方法 {@link #getCalculationAttributeData()} 返回的数据顺序一一对应,以确保在进行数据处理和计算时能够正确匹配属性和对应的值
     * <p>
     * 该方法返回的列表数据不应该随着对象数据不同而变动,只要是一个类返回列表的数据就应该是一样的
     *
     * @return 包含计算属性英文字段名称的列表
     * <p>
     * <pre>
     * {@code
     * @Override
     *     public List<String> getCalculationAttributeEnglishNames() {
     *         return Arrays.asList(
     *                 "closing_price",
     *                 "opening_price",
     *                 "highest_price",
     *                 "lowest_price",
     *                 "trade_volume",
     *                 "rise_and_fall"
     *         );
     *     }
     * }</pre>
     */
    List<String> getCalculationAttributeEnglishNames();

    /**
     * 获取用于计算的金融属性的简体中文(中国大陆)字段名称列表
     * <p>
     * 这些名称应与后续获取计算属性数据的方法 {@link #getCalculationAttributeData()} 返回的数据顺序一一对应,以确保在进行数据处理和计算时能够正确匹配属性和对应的值
     * <p>
     * 该方法返回的列表数据不应该随着对象数据不同而变动,只要是一个类返回列表的数据就应该是一样的
     *
     * @return 包含计算属性简体中文字段名称的列表
     * <p>
     * <pre>
     * {@code
     * @Override
     *     public List<String> getCalculationAttributeSimplifiedChineseNames() {
     *         return Arrays.asList(
     *                 "收盘价",
     *                 "开盘价",
     *                 "最高价",
     *                 "最低价",
     *                 "交易量",
     *                 "涨跌幅"
     *         );
     *     }
     * }</pre>
     */
    List<String> getCalculationAttributeSimplifiedChineseNames();

    /**
     * 获取用于计算的金融属性数据列表
     * <p>
     * 列表中的每个元素对应一个计算属性的值，其顺序应与
     * {@link #getCalculationAttributeEnglishNames()} 和 {@link #getCalculationAttributeSimplifiedChineseNames()}
     * 方法返回的属性名称列表顺序一致
     * <p>
     * 该方法返回的列表长度和属性顺序不应该随着对象数据不同而变动,只要是一个类返回列表长度和属性顺序就应该是一样的
     *
     * @return 包含计算属性数据的列表
     * <p>
     *
     * <pre>
     * {@code
     * @Override
     *     public List<Double> getCalculationAttributeData() {
     *         return Arrays.asList(
     *                 closingPrice,
     *                 openingPrice,
     *                 highestPrice,
     *                 lowestPrice,
     *                 tradeVolume,
     *                 riseAndFall
     *         );
     *     }
     * }</pre>
     */
    List<Double> getCalculationAttributeData();
}
