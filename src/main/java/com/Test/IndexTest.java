package com.Test;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

/**
 * @author: zhangQY
 * @date: 2021/4/23
 * @description:
 */
@Slf4j
public class IndexTest {

    public static void main(String... args)throws Exception{
//        String sql = "create unique index testIndex on city (name,local_his)";
//        Statement statement = CCJSqlParserUtil.parse(sql);
//        CreateIndex createIndex = (CreateIndex)statement;
//        log.info("");
        String sql = "drop index testIndex;";
        Statement statement = CCJSqlParserUtil.parse(sql);
        log.info("");
    }
}
