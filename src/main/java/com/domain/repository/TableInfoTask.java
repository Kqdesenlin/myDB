package com.domain.repository;

import com.Infrastructure.TableInfo.TableInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhangQY
 * @date: 2021/4/20
 * @description:
 */
@Slf4j
@Component
@Configurable
@EnableScheduling
public class TableInfoTask {

    public static Map<String, TableInfo> storeTableMap = new HashMap<String, TableInfo>();

    @Scheduled(fixedRate = 5000)
    public void storeTableInfo()throws Exception {
        String oldJSON = JSON.toJSONString(storeTableMap);
        String newJSON = JSON.toJSONString(TableConstant.tableMap);
        JSONObject oldObject = JSONObject.parseObject(oldJSON);
        JSONObject newObject = JSONObject.parseObject(newJSON);

        if (!oldObject.equals(newObject)) {
//            storeTableMap = JSON.parseObject(newJSON,new TypeReference<HashMap<String,TableInfo>>(){});
            storeTableMap = (Map)JSONObject.parse(newJSON);
            writeInToFile(newJSON);
        }
    }

    public void writeInToFile(String s) {
        try {
                File file = new File("tableInfo.txt");
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(s);
            myWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
