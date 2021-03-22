package com.domain.Entity.enums;


public enum ColumnTypeEnums {
    Int("INT","整型"),
    Double("DOUBLE","浮点数"),
    VarChar("VARCHAR","变长字符串"),
    Char("CHAR","定长字符串"),
    UnKnown("UNKNOWN","未知");

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
        return ColumnTypeEnums.UnKnown;

//        return (ColumnTypeEnums) Arrays.asList(ColumnTypeEnums.values())
//                .stream().filter((columnTypeEnum) -> columnTypeEnum)
//                .findFirst().orElse(ColumnTypeEnums.Known);
    }

    public static boolean ifContains(String type){
        ColumnTypeEnums findEnum = findType(type);
        if (findEnum.equals(ColumnTypeEnums.UnKnown)){
            return false;
        }
        return true;
    }
}
