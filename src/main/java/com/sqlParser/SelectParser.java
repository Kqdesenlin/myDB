package com.sqlParser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:解析select
 */
public class SelectParser {

    public List<String> getSelectItems(String sql)throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(sql));
        PlainSelect plain = (PlainSelect) select.getSelectBody();
        List<SelectItem> selectItems = plain.getSelectItems();
        List<String> strItems = new ArrayList<String>();
        if (null != selectItems) {
            for (SelectItem selectItem : selectItems) {
                strItems.add(selectItem.toString());
            }
        }
        return strItems;
    }

    public List<String> getSelectTables(String sql)throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        Select selectStatement = (Select) statement;
        PlainSelect plain = (PlainSelect) selectStatement.getSelectBody();
        FromItem fromItem = plain.getFromItem();
        if (fromItem instanceof Table){
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            return tablesNamesFinder.getTableList(selectStatement);
        }
        else if (fromItem instanceof SubSelect){
            return null;
        }
        return null;
    }

    public List<String> getJoinTables(String sql)throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        Select selectStatement = (Select) statement;
        PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
        List<Join> joinList = plainSelect.getJoins();
        List<String> tableWithJoin = new ArrayList<>();
        if (null != joinList) {
            for (Join join : joinList) {
                join.setLeft(true);
                tableWithJoin.add(join.toString());
            }
            return tableWithJoin;
        }
        return null;
    }

    public List<Map<String,Object>> getWhere(String sql) {
        try {
            Select select = (Select) CCJSqlParserUtil.parse(sql);
            SelectBody selectBody = select.getSelectBody();
            PlainSelect plainSelect = (PlainSelect) selectBody;
            //获得表达式
            Expression expr = CCJSqlParserUtil.parseCondExpression(plainSelect.getWhere().toString());
            List<Map<String,Object>> arrList = new ArrayList<>();
            //visit的构造模式，让visit适配expression
            expr.accept(new ExpressionDeParser() {

                int depth = 0;

                //parenthesis是插入语，也就是()，说明包含子查询或者子表达式
                @Override
                public void visit(Parenthesis parenthesis) {
                    depth++;
                    parenthesis.getExpression().accept(this);
                    depth--;
                }

                //
                @Override
                public void visit(OrExpression orExpression) {
                    visitBinaryExpr(orExpression,"OR");
                }

                @Override
                public void visit(AndExpression andExpression) {
                    visitBinaryExpr(andExpression,"AND");
                }

                private void visitBinaryExpr(BinaryExpression expr, String operator) {
                    Map<String,Object> map = new HashMap<>();
                    if (!(expr.getLeftExpression() instanceof OrExpression)
                    && !(expr.getLeftExpression() instanceof AndExpression)
                    && !(expr.getLeftExpression() instanceof  Parenthesis)) {
                        getBuffer();
                    }
                    expr.getLeftExpression().accept(this);
                    map.put("leftExpression", expr.getLeftExpression());
                    map.put("operator", operator);
                    if (!(expr.getRightExpression() instanceof OrExpression)
                    && !(expr.getRightExpression() instanceof  AndExpression)
                    && !(expr.getRightExpression() instanceof Parenthesis)) {
                        getBuffer();
                    }
                    expr.getRightExpression().accept(this);
                    map.put("rightExpression", expr.getRightExpression());
                    arrList.add(map);
                }
            });
            return arrList;
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        return null;
    }
}
