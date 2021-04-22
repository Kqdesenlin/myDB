package com.application.api;

import com.Infrastructure.Service.TypeConverUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.application.dto.FakeNode;
import com.application.dto.SQLDto;
import com.application.dto.SelectResultDto;
import com.domain.Entity.result.OperateResult;
import com.domain.Entity.result.ResultCode;
import com.domain.Entity.result.SelectResult;
import com.domain.service.DataService;
import com.domain.service.SqlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:
 */
@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200",maxAge = 3600,allowCredentials = "true")
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
    @Autowired
    private DataService dataService;


    @PostMapping("/sql")
    public JSONArray testSQL(@RequestBody SQLDto sql, HttpServletRequest request) {

        JSONArray array = new JSONArray();
        if (null == sql) {
            return array;
        }
        String input = sql.getSql().replaceAll("\r|\n","");
        log.info(input);
        HttpSession session = request.getSession();
        List<String> sqlList = (List<String>)session.getAttribute("sqlBackUp");
        if (null == sqlList || sqlList.size() == 0) {
            session.setAttribute("sqlBackUp", Arrays.asList(input));
        } else {
            session.setAttribute("sqlBackUp",((List<String>)session.getAttribute("sqlBackUp")).add(input));
        }
        try {
                List<OperateResult> or = sqlService.mutilSqlMapToState(input);
            for (OperateResult result : or) {
                JSONObject obj = new JSONObject();
                obj.put("info",result.getInfo());
                obj.put("code",result.getCode());
                if (ResultCode.selectOk.equals(result.getCode())) {
                    SelectResult select = (SelectResult) result.getRtn();
                    SelectResultDto dto = TypeConverUtils.selectResultToDto(select);
                    obj.put("rtn",dto);
                } else {
                    obj.put("rtn",result.getRtn());
                }
                array.add(obj);
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }

    @GetMapping("/data")
    public JSONArray getTableInfo() {
        JSONArray jsonArray = new JSONArray();
        List<FakeNode> list = dataService.getTableInfoDto();
        jsonArray.addAll(list);
        return jsonArray;
    }

    @GetMapping("/updatedata")
    public JSONArray getUpdateTableInfo() {
        return dataService.getTableAndColumn();
    }




}
