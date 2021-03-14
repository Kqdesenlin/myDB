package sqlParser;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.List;

public class mainParser {
    public void mainParsing(String sql)throws Exception{
        Statement statement = CCJSqlParserUtil.parse(sql);
        Select selectStatement = (Select) statement;
        PlainSelect plainSelect = (PlainSelect)selectStatement.getSelectBody();
        //获取select和from之间的items
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        System.out.println(selectItems.toString());
        //获取表名
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tables = tablesNamesFinder.getTableList(selectStatement);
        System.out.println(tables.toString());
        Expression where = plainSelect.getWhere();
        whereJudge(where);

    }

    public void whereJudge(Expression where){
        if(where instanceof AndExpression){
            AndExpression andExpression = (AndExpression) where;
            Expression leftExpression = andExpression.getLeftExpression();
            Expression rightExpression = andExpression.getRightExpression();
            whereJudge(leftExpression);
            whereJudge(rightExpression);
        } else if(where instanceof EqualsTo){
            System.out.println(where.toString());
        }
    }


}
