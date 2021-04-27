package com.Infrastructure.Visitor;

import com.Infrastructure.IndexInfo.IndexColumnResult;
import com.Infrastructure.IndexInfo.IndexInfo;
import com.Infrastructure.IndexInfo.IndexValues;
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
import java.util.stream.Collectors;

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

    /**
     * 索引中满足条件的两种结果
     * 第一种：满足条件，对饮的id可以回表查询
     * 第二种：不满足条件，对应的id直接丢弃。
     * 因此，我们默认对行，满足条件，也就是要查询
     * 所以， 当出现满足查询的条件，
     * @param expression
     * @param columnValueInfo
     * @return
     */
    public static IndexColumnResult indexParser(BinaryExpression expression, ColumnValueInfo columnValueInfo) {
        Expression leftExp = expression.getLeftExpression();
        String left = "";
        Expression rightExp = expression.getRightExpression();
        String right = "";
        boolean leftMark = true;
        if (FinalParserClass.ifColumn(leftExp)) {
            left = columnValueInfo.findValueByName(((Column)leftExp).getColumnName());
            if (left.equals("")) {
                return new IndexColumnResult(true);
            }
        } else if (FinalParserClass.ifConstant(leftExp)){
            left = ((StringValue)leftExp).getValue();
        } else {
            ExpressionVisitorWithRtn visitorWithRtn = new ExpressionVisitorWithRtn(columnValueInfo);
            leftExp.accept(visitorWithRtn);
            left = visitorWithRtn.getRtn();

        }
        if (FinalParserClass.ifColumn(rightExp)) {
            right = columnValueInfo.findValueByName(((Column)rightExp).getColumnName());
            if (right.equals("")) {
                return new IndexColumnResult(true);
            }
        } else if (FinalParserClass.ifConstant(rightExp)) {
            right = ((StringValue)rightExp).getValue();
        } else {
            ExpressionVisitorWithRtn visitorWithRtn = new ExpressionVisitorWithRtn(columnValueInfo);
            rightExp.accept(visitorWithRtn);
            right = visitorWithRtn.getRtn();
        }
        return new IndexColumnResult(left,right,true);
    }

    public static List<Boolean> binaryMarkJudge(BinaryExpression binaryExpression,ColumnValueInfo columnValueInfo) {
        Expression leftExp = binaryExpression.getLeftExpression();
        Expression rightExp = binaryExpression.getRightExpression();
        List<Boolean> booleans = new ArrayList<>();
        if (FinalParserClass.ifCanFinalParser(leftExp)) {
            booleans.add(true);
        } else {
            ExpressionVisitorWithBool visitor = new ExpressionVisitorWithBool(columnValueInfo);
            leftExp.accept(visitor);
            booleans.add(visitor.isIfPass());
        }
        if (FinalParserClass.ifCanFinalParser(rightExp)) {
            booleans.add(true);
        } else {
            ExpressionVisitorWithBool visitor = new ExpressionVisitorWithBool(columnValueInfo);
            rightExp.accept(visitor);
            booleans.add(visitor.isIfPass());
        }
        return booleans;
    }

    public static List<List<IndexValues>> indexCombine(BinaryExpression expression, List<IndexInfo> indexList) {
        Expression leftExp = expression.getLeftExpression();
        Expression rightExp = expression.getRightExpression();
        List<List<IndexValues>> leftIndex = new ArrayList<>();
        List<List<IndexValues>> rightIndex = new ArrayList<>();
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

    public static List<List<IndexValues>> indexSearch(BinaryExpression expression, List<IndexInfo> indexList) {
        Expression leftExp = expression.getLeftExpression();
        Expression rightExp = expression.getRightExpression();
        List<String> indexNameList = new ArrayList<>();
        for (IndexInfo indexInfo : indexList) {
            indexNameList.addAll(indexInfo.getRulesOrder());
        }
        indexNameList = indexNameList.stream().distinct().collect(Collectors.toList());
        List<List<IndexValues>> rtn = new ArrayList<>();
        if (FinalParserClass.ifColumn(leftExp) && FinalParserClass.ifConstant(rightExp)) {
            String columnName = ((Column) leftExp).getColumnName();
            if (indexNameList.contains(columnName)) {
                List<IndexValues> tempIndexList = new ArrayList<>();
                IndexValues temp = new IndexValues(columnName);
                String rightValue = ((StringValue)rightExp).toString();
                temp.addPoint(rightValue,rightValue);
                tempIndexList.add(temp);
                rtn.add(tempIndexList);
            }
        } else if (FinalParserClass.ifConstant(leftExp) && FinalParserClass.ifColumn(rightExp)) {
            String columnName = ((Column)rightExp).getColumnName();
            if (indexNameList.contains(columnName)) {
                List<IndexValues> tempIndexList = new ArrayList<>();
                IndexValues temp = new IndexValues(columnName);
                String leftValue = ((StringValue)leftExp).toString();
                temp.addPoint(leftValue,leftValue);
                tempIndexList.add(temp);
                rtn.add(tempIndexList);
            }
        }
        return rtn;
    }
}
