package com.Test;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

/**
 * @author: zhangQY
 * @date: 2021/4/20
 * @description:
 */
@Slf4j
public class DeleteTableTest {

    public static void main(String... args)throws Exception {
        String sql = "drop table city;";
        Statement statement = CCJSqlParserUtil.parse(sql);
        log.info("");
    }
}
