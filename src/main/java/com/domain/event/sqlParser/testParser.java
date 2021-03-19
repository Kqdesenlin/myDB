package com.domain.event.sqlParser;

public class testParser {
    String s = "a";
    char[] cs = {'1','2'};

    public void change(String s1,char[] cs1){
        s1 = "1";
        cs1[0] = 'a';
    }
    public static void main(String[] args)throws Exception{
//        String sql = "SELECT A FROM MY_TABLE1 a,MY_TABLE2 b WHERE A = 1 and B = 2 and C = 3 and D = 4";
//        mainParser mainParser = new mainParser();
//        mainParser.mainParsing(sql);
        testParser testParser = new testParser();
        testParser.change(testParser.s, testParser.cs);
        System.out.println(testParser.s+testParser.cs[0]);

    }
}

