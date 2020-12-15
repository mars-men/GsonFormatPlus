package com.foxsteps.gsonformat.entity;

import com.foxsteps.gsonformat.common.CheckUtil;
import com.foxsteps.gsonformat.tools.json.JSONObject;
import com.foxsteps.gsonformat.tools.ux.CellProvider;
import com.foxsteps.gsonformat.tools.ux.Selector;
import org.apache.http.util.TextUtils;

/**
 * Created by dim on 2015/7/15.
 */
public class FieldEntity implements Selector, CellProvider {

    protected String key;
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
     * 值
     */
    protected String value;

    /**
     * 字段序号
     */
    protected Integer sortNo;

    /**
     * 依赖的实体类
     */
    protected ClassEntity targetClass;

    protected boolean generate = true;

    public ClassEntity getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(ClassEntity targetClass) {
        this.targetClass = targetClass;
    }

    public boolean isGenerate() {
        return generate;
    }

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getGenerateFieldName() {
        return CheckUtil.getInstant().handleArg(fieldName);
    }

    public void setFieldName(String fieldName) {
        if (TextUtils.isEmpty(fieldName)) {
            return;
        }
        this.fieldName = fieldName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public String getRealType() {
        if (targetClass != null) {
            return targetClass.getClassName();
        }
        return type;
    }

    public String getBriefType() {
        if (targetClass != null) {
            return targetClass.getClassName();
        }
        int i = type.indexOf(".");
        if (i > 0) {
            return type.substring(i);
        }
        return type;
    }

    public String getFullNameType() {
        if (targetClass != null) {
            return targetClass.getQualifiedName();
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void checkAndSetType(String text) {
        if (type != null && CheckUtil.getInstant().checkSimpleType(type.trim())) {
            //基本类型
            if (CheckUtil.getInstant().checkSimpleType(text.trim())) {
                this.type = text.trim();
            }
        } else {
            //实体类:
            if (targetClass != null && !targetClass.isLock()) {
                if (!TextUtils.isEmpty(text)) {
                    targetClass.setClassName(text);
                }
            }
        }
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void setSelect(boolean select) {
        setGenerate(select);
    }

    public boolean isSameType(Object o) {
        if (o instanceof JSONObject) {
            if (targetClass != null) {
                return targetClass.isSame((JSONObject) o);
            }
        } else {
            return DataType.isSameDataType(DataType.typeOfString(type), DataType.typeOfObject(o));
        }
        return false;
    }

    @Override
    public String getCellTitle(int index) {
        String result = "";
        switch (index) {
            case 0:
                result = getKey();
                break;
            case 1:
                result = getValue();
                break;
            case 2:
                result = getBriefType();
                break;
            case 3:
                result = getFieldName();
                break;
            case 4:
                result = getFieldComment();
                break;
        }
        return result;
    }

    @Override
    public void setValueAt(int column, String text) {
        switch (column) {
            case 2:
                checkAndSetType(text);
                break;
            case 3:
                if (CheckUtil.getInstant().containsDeclareFieldName(text)) {
                    return;
                }
                CheckUtil.getInstant().removeDeclareFieldName(getFieldName());
                setFieldName(text);
                break;
            case 4:
                setFieldComment(text);
                break;
        }
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }
}
