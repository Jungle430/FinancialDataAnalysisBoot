package com.bupt.Jungle.FinancialDataAnalysis.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 计算相关性系数时显示的属性值
 * <p>
 * 用法: 注释在getter上
 * <p>
 * <pre>
 *  {@code
 *  public class BondsBO {
 *     private Double closingPrice;
 *
 *     @Attribute(english = "closing price", simplifiedChinese = "收盘价")
 *     public Double getClosingPrice() {
 *         return this.closingPrice;
 *     }
 * }
 *  }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Attribute {
    String english();

    String simplifiedChinese();
}
