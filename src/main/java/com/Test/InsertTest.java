package com.Test;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.insert.Insert;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/25
 * @description:
 */
public class InsertTest {

    public static void main(String[] args)throws Exception {
        //String sql = "insert into world.city(name,countrycode,district,population) select `name`,`Code`,region,population from world.country where code = 'ABW';\n";
        String sql = "insert asdaas sd into world.city(name,countrycode,district,population) values('sadd','ds','sdasd',(select population from world.country where code = 'ABW'));";
        //String sql = "insert into new_table(id) values((select id2 from new_table2 where id2 = 1));";
        //String sql = "insert into new_table(id) values(1,'A',null,1.23);";
        Insert insert = (Insert) CCJSqlParserUtil.parse(sql);
        insert.getColumns();
        insert.getItemsList();
        insert.getTable();
        insert.getSelect();
        ItemsList list = insert.getItemsList();
        if (list instanceof ExpressionList) {
            List<Expression> expressions = ((ExpressionList) list).getExpressions();
            for (Expression expression : expressions) {
                System.out.println(expression.getClass().getSimpleName());
                System.out.println(expression.toString());
            }
        }
    }
}
