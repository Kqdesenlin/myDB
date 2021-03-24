package com.Infrastructure.Visitor;

import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;

/**
 * @author: zhangQY
 * @date: 2021/3/24
 * @description: 最终可以解析的所有expression
 */
public class FinalParserClass {
    public static boolean ifCanFinalParser(Expression expression) {
        if ((expression instanceof Column) || (expression instanceof LongValue) ||
                (expression instanceof StringValue) || (expression instanceof DoubleValue)) {
            return true;
        }
        return false;
    }
}
