package com.Test;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

/**
 * @author: zhangQY
 * @date: 2021/3/24
 * @description:
 */
@Slf4j
public class SelectTest {


    public static void main(String[] args)throws Exception {
        //String sql = "select * from new_table n ,new_table2 n2 where n.id = n2.id2;";
        //String sql = "select c.name,c.id,c.origin as ori from city c,country t where c.id = 1 or (c.name = 'kerat' and c.CountryCode in (select CountryCode from citys)) or (c.id = t.id);";
        /**
        String sql = "SELECT column_name(s) " +
                "FROM table_name1 " +
                "LEFT JOIN table_name2 " +
                "ON table_name1.column_name=table_name2.column_name";
         */
        //String sql = "select * from (select a,b,c from table1) as t1";
        //String sql = "select * from ((select a,b,c from table1) union(select d,e,f from table2))";
        //String sql = "select * from (select t1.id from new_Table t1 union select t3.id2 from new_table2 t3) as t4;";
        //String sql = "select * from (select t1.id from new_Table t1 union select t3.id2 from new_table2 t3 union all select t4.id3 from new_table3 t4) as t5;";
        //String sql = "select id1, id2, id3 from t_test";\
//        String sql = "select id1,'abc',1,
//        Select select = (Select) CCJSqlParserUtil.parse(sql);
//        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
//        SelectBody selectBody = select.getSelectBody();
        selectItemTest();

    }

    public static void selectItemTest() throws Exception{
        // 1,id,(select)都是SelectExpressionItem
       //String sql = "select 1,id,(select * from new_table2 where id2 = id) from new_table;";
        // *是allcolumn
        //String sql = "select * from new_table;";
        String sql = "select * from new_table where 1=id;";
        //String sql = "select t1.*,t2.* from new_table t1,new_table t2;";
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        SelectBody selectBody = select.getSelectBody();
        if (null != plainSelect.getLimit()){
            Limit limit = plainSelect.getLimit();
            log.info("");
        }
    }
}
