package com.Infrastructure.Enum;

import edu.emory.mathcs.backport.java.util.Arrays;


public enum ColumnTypeEnums {
    Int("Int","整型"),
    Double("Double","浮点数"),
    String("String","字符串"),
    Char("Char","字符"),
    Known("Known","未知");

    private final String key;
    private final String value;

    ColumnTypeEnums(String key,String value){
        this.key = key;
        this.value = value;
    }

    public String getKey(){
        return this.key;
    }

    public String getValue(){
        return this.value;
    }

    public static ColumnTypeEnums findType(String type){
        for (ColumnTypeEnums columnTypeEnum : ColumnTypeEnums.values()){
            String key = columnTypeEnum.getKey();
            if (key.equals(type)){
                return columnTypeEnum;
            }
        }
        return ColumnTypeEnums.Known;

//        return (ColumnTypeEnums) Arrays.asList(ColumnTypeEnums.values())
//                .stream().filter((columnTypeEnum) -> columnTypeEnum)
//                .findFirst().orElse(ColumnTypeEnums.Known);
    }

    public static boolean ifContains(String type){
        ColumnTypeEnums findEnum = findType(type);
        if (findEnum.equals(ColumnTypeEnums.Known)){
            return false;
        }
        return true;
    }
}
