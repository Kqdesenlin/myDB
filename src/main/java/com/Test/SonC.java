package com.Test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SonC {


    public static void main(String[] args) throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("a","a");
        map.put("b","b");
        Assert.assertTrue(map.containsKey("a"));
        log.info("");
    }

}
