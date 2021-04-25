package com.Test.JSON;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: zhangQY
 * @date: 2021/4/22
 * @description:
 */
@Slf4j
public class JSONTest {


    public static void main(String... args)throws Exception {
        Team team = new Team("gougoudui");
        team.addPlayer(new Player("didi")).addPlayer(new Player("dici"));
//        String oldString = JSON.toJSONString(team);
//        Team newT =  JSON.parseObject(oldString,Team.class);
        JSONObject object = (JSONObject) JSONObject.toJSON(team);
        Team newT = JSONObject.toJavaObject(object,Team.class);
        log.info("");
    }
}
