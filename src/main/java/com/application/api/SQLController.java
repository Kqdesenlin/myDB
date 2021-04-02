package com.application.api;

import com.domain.service.SqlService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:
 */
@RestController
@RequestMapping("/api")
public class SQLController {

    SqlService sqlToEntity = new SqlService();

//    @PostMapping("/sql/postSQL")
//    public ResultDto postSQL(@RequestBody SQLVo sqlVo) throws JSQLParserException {
//        String sql = sqlVo.getSql();
//        ResultDto resultDto = sqlToEntity.sqlMapToDML(sql);
//        return resultDto;
//    }
    @GetMapping("/sql")
    public Object testSQL( String sql){
        return new Object();
    }


}
