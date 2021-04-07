package com.application.api;

import com.alibaba.fastjson.JSONObject;
import com.application.dto.SQLDto;
import com.domain.Entity.result.OperateResult;
import com.domain.service.SqlService;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:
 */
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
    public JSONObject testSQL(@RequestBody SQLDto sql) {
        JSONObject obj = new JSONObject();
        try {
            OperateResult or = sqlService.sqlMapToDML(sql.getSql());
            obj.put("info",or.getInfo());
            obj.put("code",or.getCode());
            obj.put("rtn",or.getRtn());
            return obj;
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        return obj;
    }


}
