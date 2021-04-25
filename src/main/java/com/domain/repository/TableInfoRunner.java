package com.domain.repository;

import com.Infrastructure.TableInfo.TableInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhangQY
 * @date: 2021/4/22
 * @description:
 */
@Slf4j
@Component
public class TableInfoRunner implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
        File file = new File("tableInfo.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        if (!sb.toString().isEmpty()){
            writeIntoTableConstant(sb.toString());
        }
    }

    public void writeIntoTableConstant(String newJson) {
        Map<String, JSONObject> map = JSON.parseObject(newJson,Map.class);
        Map<String,TableInfo> middle = new HashMap<>();
        for (Map.Entry<String,JSONObject> entry:map.entrySet()) {
            String tableName = entry.getKey();
            TableInfo tableInfo = JSONObject.toJavaObject(entry.getValue(),TableInfo.class);
            middle.put(tableName,tableInfo);
        }
        TableConstant.tableMap = middle;
    }
}
