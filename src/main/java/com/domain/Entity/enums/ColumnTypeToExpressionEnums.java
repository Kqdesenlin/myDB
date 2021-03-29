package com.domain.Entity.enums;

import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;

/**
 * @author: zhangQY
 * @date: 2021/3/28
 * @description: 在建立临时表的过程中，对于expression的类型，转化到数据库数据类型
 */
public class ColumnTypeToExpressionEnums {

//    Int(LongValue,"INT"),
//    Double(DoubleValue,"DOUBLE"),
//    VarChar(StringValue,"VARCHAR"),
//    Char("CHAR","CHAR"),
//    UnKnown(,"UNKNOWN");

    private Expression expression;
    private String value;

    public static String findTypeByExpression(Expression expression) {
        return null;
    }
}
