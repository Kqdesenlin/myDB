package com.interfaces.api;

import com.domain.Entity.result.OperateResult;
import com.domain.service.SqlToEntity;
import com.interfaces.dto.ResultDto;
import com.interfaces.vo.SQLVo;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:
 */
@RestController
public class SQLController {

    SqlToEntity sqlToEntity = new SqlToEntity();

    @PostMapping("/sql/postSQL")
    public ResultDto postSQL(@RequestBody SQLVo sqlVo) throws JSQLParserException {
        String sql = sqlVo.getSql();
        ResultDto resultDto = sqlToEntity.sqlMapToDML(sql);
        return resultDto;
    }
}
