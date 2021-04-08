package com.application.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.application.dto.SQLDto;
import com.domain.Entity.result.OperateResult;
import com.domain.service.SqlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class SQLController {

    @Autowired
    private SqlService sqlService;
//    @PostMapping("/sql/postSQL")
//    public ResultDto postSQL(@RequestBody SQLVo sqlVo) throws JSQLParserException {
//        String sql = sqlVo.getSql();
//        ResultDto resultDto = sqlToEntity.sqlMapToDML(sql);
//        return resultDto;
//    }

    @CrossOrigin
    @PostMapping("/sql")
    public JSONArray testSQL(@RequestBody SQLDto sql) {
        JSONArray array = new JSONArray();
        if (null == sql) {
            return array;
        }
        log.info(sql.getSql());
        try {
            List<OperateResult> or = sqlService.mutilSqlMapToState(sql.getSql());
            for (OperateResult result : or) {
                JSONObject obj = new JSONObject();
                obj.put("info",result.getInfo());
                obj.put("code",result.getCode());
                obj.put("rtn",result.getRtn());
                array.add(obj);
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }


}
