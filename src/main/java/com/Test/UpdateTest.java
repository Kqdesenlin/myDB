package com.Test;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

/**
 * @author: zhangQY
 * @date: 2021/4/7
 * @description:
 */
@Slf4j
public class UpdateTest {

    public static void main(String[] args)throws Exception {
        String sql = "update new_table set column2 = 'abc',id= where id = 1";
        Statement statement = CCJSqlParserUtil.parse(sql);
        log.info("");
    }
}
