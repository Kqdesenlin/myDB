package com.Infrastructure.Visitor;

import com.Infrastructure.Service.TypeConverUtils;
import com.Infrastructure.TableInfo.ColumnValueInfo;
import com.Infrastructure.Visitor.IndexVisitor.IndexExpressionVisitorWithRtn;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/4/8
 * @description: 把二元表达式的两端的判断，解析为两个string
 */
public class BinaryExpressionParser {

    public static List<String> parser(BinaryExpression expression, ColumnValueInfo columnValueInfo) {
        Expression leftExp = expression.getLeftExpression();
        String left = "";
        Expression rightExp = expression.getRightExpression();
        String right = "";
        if (FinalParserClass.ifColumn(leftExp)) {
            left = columnValueInfo.findValueByName(((Column)leftExp).getColumnName());
        } else if (FinalParserClass.ifConstant(leftExp)){
            left = ((StringValue)leftExp).getValue();
        } else {
            ExpressionVisitorWithRtn visitorWithRtn = new ExpressionVisitorWithRtn(columnValueInfo);
            leftExp.accept(visitorWithRtn);
            left = visitorWithRtn.getRtn();
        }
        if (FinalParserClass.ifColumn(rightExp)) {
            right = columnValueInfo.findValueByName(((Column)rightExp).getColumnName());
        } else if (FinalParserClass.ifConstant(rightExp)) {
            right = ((StringValue)rightExp).getValue();
        } else {
            ExpressionVisitorWithRtn visitorWithRtn = new ExpressionVisitorWithRtn(columnValueInfo);
            rightExp.accept(visitorWithRtn);
            right = visitorWithRtn.getRtn();
        }
        return Arrays.asList(left,right);
    }

    public static List<List<String>> indexCombine(BinaryExpression expression,List<String> indexList) {
        Expression leftExp = expression.getLeftExpression();
        Expression rightExp = expression.getRightExpression();
        List<List<String>> leftIndex = new ArrayList<>();
        List<List<String>> rightIndex = new ArrayList<>();
        if (!FinalParserClass.ifCanFinalParser(leftExp)) {
            IndexExpressionVisitorWithRtn visitor = new IndexExpressionVisitorWithRtn(indexList);
            leftExp.accept(visitor);
            leftIndex = visitor.getRtn();
        }
        if (!FinalParserClass.ifCanFinalParser(rightExp)) {
            IndexExpressionVisitorWithRtn visitor = new IndexExpressionVisitorWithRtn(indexList);
            rightExp.accept(visitor);
            rightIndex = visitor.getRtn();
        }
        return TypeConverUtils.IndexListCombined(leftIndex,rightIndex);
    }

    public static List<List<String>> indexSearch(BinaryExpression expression, List<String> indexList) {
        Expression leftExp = expression.getLeftExpression();
        Expression rightExp = expression.getRightExpression();
        List<List<String>> rtn = new ArrayList<>();
        if (FinalParserClass.ifColumn(leftExp) && FinalParserClass.ifConstant(rightExp)) {
            String columnName = ((Column) leftExp).getColumnName();
            if (indexList.contains(columnName)) {
                List<String> tempIndexList = new ArrayList<>();
                tempIndexList.add(columnName);
                rtn.add(tempIndexList);
            }
        } else if (FinalParserClass.ifConstant(leftExp) && FinalParserClass.ifColumn(rightExp)) {
            String columnName = ((Column)rightExp).getColumnName();
            if (indexList.contains(columnName)) {
                List<String> tempIndexList = new ArrayList<>();
                tempIndexList.add(columnName);
                rtn.add(tempIndexList);
            }
        }
        return rtn;
    }
}
