package com.Test;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

import java.util.logging.Logger;

/**
 * @author: zhangQY
 * @date: 2021/3/24
 * @description:
 */
public class SelectTest {

    public static Logger logger = Logger.getLogger("log_" + SelectTest.class.getSimpleName());

    public static void main(String[] args)throws Exception {
        //String sql = "select * from new_table n ,new_table2 n2 where n.id = n2.id2;";
        //String sql = "select c.name,c.id,c.origin as ori from city c,country t where c.id = 1 or (c.name = 'kerat' and c.CountryCode in (select CountryCode from citys)) or (c.id = t.id);";
        String sql = "SELECT column_name(s) " +
                "FROM table_name1 " +
                "LEFT JOIN table_name2 " +
                "ON table_name1.column_name=table_name2.column_name";
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        SelectBody selectBody = select.getSelectBody();

    }
}
