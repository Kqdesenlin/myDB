package com.Test;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;

/**
 * @author: zhangQY
 * @date: 2021/3/30
 * @description:
 */
@Slf4j
public class DeleteTest {

    public static void main(String[] args)throws Exception {
        String sql = "delete from (select * from new_table) where id = 1";
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (statement instanceof Delete) {
            log.info("");
        }
    }
}
