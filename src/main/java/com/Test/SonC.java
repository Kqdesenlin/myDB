package com.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SonC {


    public static void main(String[] args) throws Exception {
        String a = "abc";
        Integer i = Integer.valueOf(a,10);
        log.info(i.toString());
    }

}
