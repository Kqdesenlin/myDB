package com.Test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.util.regex.Pattern;

/**
 * @author: zhangQY
 * @date: 2021/4/16
 * @description:
 */
@Slf4j
public class PatternTest {
    private static final Pattern REGEX_CUST_ID = Pattern.compile("^[a-z]*\\d{10}$");

    public static void main(String... args)throws Exception{
        String custId = "2432356746";
        System.out.println(REGEX_CUST_ID.matcher(custId).matches());
        Assert.assertTrue("匹配正则", REGEX_CUST_ID.matcher(custId).matches());
    }
}
