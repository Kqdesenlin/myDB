package com.domain.Entity.enums;

public enum IndexTypeEnums {
    INDEX("INDEX","普通索引"),
    UNIQUE("UNIQUE","唯一索引"),
    UNKNOWN("UNKNOWN","未知");
    private final String key;

    private final String value;

    IndexTypeEnums(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey(){
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public static IndexTypeEnums findIndexType(String indexName) {
        if (null == indexName || indexName.isEmpty()) {
            return INDEX;
        } else {
            for (IndexTypeEnums enums : IndexTypeEnums.values()) {
                if (enums.getKey().equals(indexName)) {
                    return enums;
                }
            }
        }
        return UNKNOWN;
    }
}
