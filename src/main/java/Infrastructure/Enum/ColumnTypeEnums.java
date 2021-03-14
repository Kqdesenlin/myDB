package Infrastructure.Enum;

import edu.emory.mathcs.backport.java.util.Arrays;

public enum ColumnTypeEnums {
    Int("Int","整型"),
    Double("Double","浮点数"),
    String("String","字符串"),
    Char("Char","字符"),
    Known("Known","未知");
    private String key;
    private String value;
    ColumnTypeEnums(String key,String value){
        this.key = key;
        this.value = value;
    }
    public static ColumnTypeEnums findType(String type){
        return (ColumnTypeEnums) Arrays.asList(ColumnTypeEnums.values()).stream()
                .filter(columnTypeEnum -> columnTypeEnum.equals(type))
                .findFirst().orElse(ColumnTypeEnums.Known);
    }

    public static boolean ifContains(String type){
        ColumnTypeEnums findEnum = findType(type);
        if (findEnum.equals(ColumnTypeEnums.Known)){
            return false;
        }
        return true;
    }
}
