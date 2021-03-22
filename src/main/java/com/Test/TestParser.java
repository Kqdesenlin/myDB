package com.Test;

import com.domain.event.sqlParser.SelectParser;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

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

//        SelectParser selectParser = new SelectParser();
////        List<String> list = selectParser.getSelectTables("select * from (select id from website,accont where website.id = account.id) as nt1;");
//        String sql = "select * from website where id = 1 or id =2;";
//        logger.info(selectParser.getWhere(sql).toString());
////        Addition
        String sql = "create table C(c#  char(8) primary key not null,cname   char(10),teacher char(20));";
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof CreateTable) {
            logger.info(statement.toString());
            List<ColumnDefinition> columnDefinitions = ((CreateTable) statement).getColumnDefinitions();
            for (ColumnDefinition columnDefinition : columnDefinitions) {
                logger.info(columnDefinition.getColumnName());
                logger.info(columnDefinition.getColumnSpecs().toString());
                logger.info(columnDefinition.getColDataType().toString());
            }
        }

    }
}
