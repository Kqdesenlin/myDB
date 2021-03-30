package com.application.api;

import com.domain.service.SqlToEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:
 */
@RestController
public class SQLController {

    SqlToEntity sqlToEntity = new SqlToEntity();

//    @PostMapping("/sql/postSQL")
//    public ResultDto postSQL(@RequestBody SQLVo sqlVo) throws JSQLParserException {
//        String sql = sqlVo.getSql();
//        ResultDto resultDto = sqlToEntity.sqlMapToDML(sql);
//        return resultDto;
//    }
}
