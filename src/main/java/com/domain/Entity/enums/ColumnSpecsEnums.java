package com.domain.Entity.enums;

public enum ColumnSpecsEnums {
    not_null("not","notnull",2),
    unique("unique","unique",1),
    primary_key("primary","primarykey",2);

    private String first;
    private String total;
    private int length;

    public String getFirst() {
        return this.first;
    }

    public String getTotal() {
        return this.total;
    }

    public int getLength() {
        return this.length;
    }

    ColumnSpecsEnums(String first,String total,int length) {
        this.first = first;
        this.total = total;
        this.length = length;
    }

    public static ColumnSpecsEnums findTypeByFirst(String first) {
        for (ColumnSpecsEnums enums : ColumnSpecsEnums.values()) {
            String key = enums.getFirst();
            if (key.equals(first)) {
                return enums;
            }
        }
        return null;
    }
}
