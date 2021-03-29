package com.domain.Entity.enums;


import com.Infrastructure.Visitor.FinalParserClass;
import net.sf.jsqlparser.expression.*;

public enum ColumnTypeEnums {
    Int("INT","整型"),
    Double("DOUBLE","浮点数"),
    VarChar("VARCHAR","变长字符串"),
    Char("CHAR","定长字符串"),
    Null("NULL","为空"),
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

    public static String findTypeByExpression(Expression expression) {
        if (!FinalParserClass.ifConstant(expression)) {
            return UnKnown.getKey();
        } else {
            if (expression instanceof LongValue) {
                return Int.getKey();
            }
            if (expression instanceof DoubleValue) {
                return Double.getKey();
            }
            if (expression instanceof StringValue) {
                return VarChar.getKey();
            }
            if (expression instanceof NullValue) {
                return Null.getKey();
            }
        }
        return UnKnown.getKey();
    }
}
