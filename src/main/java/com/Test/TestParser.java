package com.Test;

import junit.framework.Test;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import com.sqlParser.MainParser;
import com.sqlParser.SelectParser;

import java.util.List;
import java.util.logging.Logger;

public class TestParser {

    private static Logger logger = Logger.getLogger("log_" + TestParser.class.getName());

    public static void main(String[] args)throws Exception{
//        MainParser mainParser = new MainParser();
//        //单表查询
//        mainParser.entrance("select * from t_website");
//        //别名查询
//        mainParser.entrance("select * from t_website t1");
//        //多表查询
//        mainParser.entrance("select * from t_website, t_account,t_access");
//        //内链接
//        mainParser.entrance("select * from t_website inner join t_account on t_website.id = t_account.id");
//        //左外连接
//        mainParser.entrance("select * from t_website left join t_account on t_website.id = t_account.id");
//        //右外连接
//        mainParser.entrance("select * from t_website right join t_account on t_website.id = t_account.id");
//        //全连接
//        mainParser.entrance("select * from t_website full join t_account on t_website.id = t_account.id");
//        //嵌套
//        mainParser.entrance("select * from (select id from t_website)");

        SelectParser selectParser = new SelectParser();
//        List<String> list = selectParser.getSelectTables("select * from (select id from website,accont where website.id = account.id) as nt1;");
        String sql = "select * from website where id = 1 or id =2;";
        logger.info(selectParser.getWhere(sql).toString());
        Addition
    }
}
