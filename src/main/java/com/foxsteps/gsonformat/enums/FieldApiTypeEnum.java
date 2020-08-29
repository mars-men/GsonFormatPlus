package com.foxsteps.gsonformat.enums;

/**
 * @author wangzejun
 * @description api字段类型枚举
 * @date 2020年07月04日 18:02:23
 */
public enum FieldApiTypeEnum {

    /**
     * integer-Integer
     */
    INTEGER("integer", "Integer"),

    /**
     * string-String
     */
    STRING("string", "String"),

    /**
     * text-String
     */
    TEXT("text", "String"),

    /**
     * boolean-Boolean
     */
    BOOLEAN("boolean", "Boolean"),

    /**
     * decimal-BigDecimal
     */
    DECIMAL("decimal", "BigDecimal"),

    /**
     * object-Object
     */
    OBJECT("object", "Object"),

    /**
     * []-Array
     */
    ARRAY("[]", "Array"),
    ;

    /**
     * value
     */
    private String value;

    /**
     * title
     */
    private String title;

    FieldApiTypeEnum(String value, String title) {
        this.value = value;
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }
}
