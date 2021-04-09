package com.Infrastructure.Visitor;

import com.Infrastructure.TableInfo.ColumnValueInfo;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;

import java.util.Arrays;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/8
 * @description: 把二元表达式的两端的判断，解析为两个string
 */
public class BinaryExpressionToBinaryString {

    public static List<String> parser(BinaryExpression expression, ColumnValueInfo columnValueInfo) {
        Expression leftExp = expression.getLeftExpression();
        String left = "";
        Expression rightExp = expression.getRightExpression();
        String right = "";
        if (FinalParserClass.ifColumn(leftExp)) {
            left = columnValueInfo.findValueByName(leftExp.toString());
        } else if (FinalParserClass.ifConstant(leftExp)){
            left = leftExp.toString();
        } else {
            ExpressionVisitorWithRtn visitorWithRtn = new ExpressionVisitorWithRtn(columnValueInfo);
            leftExp.accept(visitorWithRtn);
            left = visitorWithRtn.getRtn();
        }
        if (FinalParserClass.ifColumn(rightExp)) {
            right = columnValueInfo.findValueByName(rightExp.toString());
        } else if (FinalParserClass.ifConstant(rightExp)) {
            right = rightExp.toString();
        } else {
            ExpressionVisitorWithRtn visitorWithRtn = new ExpressionVisitorWithRtn(columnValueInfo);
            rightExp.accept(visitorWithRtn);
            right = visitorWithRtn.getRtn();
        }
        return Arrays.asList(left,right);
    }
}
