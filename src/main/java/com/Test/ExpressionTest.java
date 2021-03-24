package com.Test;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import java.util.logging.Logger;

/**
 * @author: zhangQY
 * @date: 2021/3/24
 * @description:
 */
public class ExpressionTest {

    public static Logger logger = Logger.getLogger("log_" + ExpressionTest.class.getSimpleName());

    public static void main(String[] args)throws Exception {
        String sql = "select * from website w where (w.id = 1);";
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        Expression expression = plainSelect.getWhere();
        logger.info(expression.getClass().getSimpleName());
    }
}
