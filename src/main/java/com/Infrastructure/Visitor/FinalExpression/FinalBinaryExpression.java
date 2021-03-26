package com.Infrastructure.Visitor.FinalExpression;

import lombok.Data;
import net.sf.jsqlparser.expression.Expression;

/**
 * @author: zhangQY
 * @date: 2021/3/24
 * @description: 最终二元表达式
 */
@Data
public class FinalBinaryExpression {

    /**
     * 左表达式
     */
    Expression leftExpression;

    /**
     * 右表达式
     */
    Expression rightExpression;

    /**
     * 中间符号条件
     */
    String condition;
}
