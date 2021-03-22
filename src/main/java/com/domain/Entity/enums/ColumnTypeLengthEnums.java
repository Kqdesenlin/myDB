package com.domain.Entity.enums;

public enum ColumnTypeLengthEnums {
    VarChar("VARCHAR",255),
    Char("CHAR",65535);

    private final String key;
    private final int length;

    ColumnTypeLengthEnums(String key,int length) {
        this.key = key;
        this.length =length;
    }

    public String getKey() {
        return this.key;
    }

    public int getLength() {
        return this.length;
    }

    public static ColumnTypeLengthEnums findType(String type) {
        for (ColumnTypeLengthEnums columnTypeLengthEnums : ColumnTypeLengthEnums.values()) {
            String key = columnTypeLengthEnums.getKey();
            if (key.equals(type)) {
                return columnTypeLengthEnums;
            }
        }
        return null;
    }
    public static boolean ifContains(String type) {
        if (null == findType(type)) {
            return false;
        }
        return true;
    }


}
