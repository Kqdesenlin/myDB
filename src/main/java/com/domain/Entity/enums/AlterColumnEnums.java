package com.domain.Entity.enums;

public enum AlterColumnEnums {

    ADD("ADD"),
    ALTER("ALTER"),
    DROP("DROP"),
    NULL("NULL");

    private String type;

    public String getType() {
        return this.type;
    }

    AlterColumnEnums(String type) {
        this.type = type;
    }

    public AlterColumnEnums findType(String type) {
        for (AlterColumnEnums columnEnums :AlterColumnEnums.values()) {
            if (columnEnums.getType().equals(type)) {
                return columnEnums;
            }
        }
        return NULL;
    }
}
