package com.foxsteps.gsonformat.entity;

/**
 * @author wangzejun
 * @description api字段信息
 * @date 2020年07月04日 16:06:35
 */
public class FieldApiInfo {

    /**
     * 父级名字
     */
    private String parentName;

    /**
     * 类型
     */
    protected String type;

    /**
     * 生成的名字
     */
    protected String fieldName;

    /**
     * 生成的注释
     */
    protected String fieldComment;

    /**
     * 默认值
     */
    protected String defaultValue;

    /**
     * 是否必填
     *
     * @return
     */
    protected String required;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
