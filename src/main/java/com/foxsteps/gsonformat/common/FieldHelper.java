package com.foxsteps.gsonformat.common;

import com.foxsteps.gsonformat.config.Constant;
import com.foxsteps.gsonformat.entity.FieldApiInfo;
import com.foxsteps.gsonformat.enums.FieldApiTypeEnum;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dim on 17/1/21.
 */
public class FieldHelper {

    private final static String FIELD_NAME = "fieldName";

    private final static String FIELD_NAME_COMMENT = "fieldComment";

    private final static String TYPE = "type";

    private final static String REQUIRED = "required";

    private final static String DEFAULT_VALUE = "defaultValue";

    private final static String FIELD_DESC = "fieldDesc";

    private final static Map<String, Integer> fieldIndexMap = new LinkedHashMap<String, Integer>(12) {
        {
            put(FIELD_NAME, 0);
            put(FIELD_NAME_COMMENT, 1);
            put(TYPE, 2);
            put(REQUIRED, 3);
            put(DEFAULT_VALUE, 4);
            put(FIELD_DESC, 5);
        }
    };

    /**
     * 生成字段名
     *
     * @param name
     * @return
     */
    public static String generateLuckyFieldName(String name) {

        if (name == null) {
            return Constant.DEFAULT_PREFIX + new Random().nextInt(333);
        }
        Matcher matcher = Pattern.compile("(\\w+)").matcher(name);
        StringBuilder sb = new StringBuilder("_$");
        while (matcher.find()) {
            sb.append(StringUtils.captureName(matcher.group(1)));
        }
        return sb.append(new Random().nextInt(333)).toString();
    }

    /**
     * 解析soapi系统字段信息
     *
     * @param apiInfo
     * @return key 为fieldName
     * @author wangzejun
     * @date 2020年07月04日 16:10:18
     */
    public static Map<String, FieldApiInfo> getFieldApiInfo(String apiInfo, String suffixStr) {
        Map<String, FieldApiInfo> apiFieldMap = new HashMap<>(8);
        if (!StringUtils.isNotBlank(apiInfo)) {
            return apiFieldMap;
        }
        String[] apiFieldArr = apiInfo.split("\n");
        if (apiFieldArr == null || apiFieldArr.length == 0) {
            return apiFieldMap;
        }

        Map<String, List<String>> childrenMap = new HashMap<>(8);
        List<FieldApiInfo> firstFieldList = new ArrayList<>();
        String parentName = "";
        dealWithField(apiFieldArr, childrenMap, firstFieldList, parentName, suffixStr);
        if (childrenMap.size() > 0) {
            for (Map.Entry<String, List<String>> childEntry : childrenMap.entrySet()) {
                List<String> childList = childEntry.getValue();
                childList = sortChildList(childList);
                String[] childArr = childList.toArray(new String[childList.size()]);
                Map<String, List<String>> thirdChildMap = new HashMap<>(8);
                dealWithField(childArr, thirdChildMap, firstFieldList, childEntry.getKey(), suffixStr);
                if (thirdChildMap.size() > 0) {
                    for (Map.Entry<String, List<String>> thirdEntry : thirdChildMap.entrySet()) {
                        Map<String, List<String>> firthChildMap = new HashMap<>(8);
                        List<String> thirdList = thirdEntry.getValue();
                        thirdList = sortChildList(thirdList);
                        String[] thirdArr = thirdList.toArray(new String[thirdList.size()]);
                        dealWithField(thirdArr, firthChildMap, firstFieldList, thirdEntry.getKey(), suffixStr);
                        if (firthChildMap.size() > 0) {
                            for (Map.Entry<String, List<String>> fourthEntry : firthChildMap.entrySet()) {
                                Map<String, List<String>> fiveChildMap = new HashMap<>(8);
                                List<String> fourthList = fourthEntry.getValue();
                                fourthList = sortChildList(fourthList);
                                String[] fourthArr = fourthList.toArray(new String[fourthList.size()]);
                                dealWithField(fourthArr, fiveChildMap, firstFieldList, fourthEntry.getKey(), suffixStr);
                            }
                        }
                    }
                }
            }
        }
        for (FieldApiInfo fieldApiInfo : firstFieldList) {
            String key = null;
            if (StringUtils.isNotBlank(fieldApiInfo.getParentName())) {
                key = fieldApiInfo.getParentName() + "-" + fieldApiInfo.getFieldName();
            } else {
                key = fieldApiInfo.getFieldName();
            }

            FieldApiInfo oldInfo = apiFieldMap.get(key);
            if (oldInfo!=null && !StringUtils.isNotBlank(oldInfo.getFieldComment())) {
                apiFieldMap.put(key, fieldApiInfo);
            }

            if (!apiFieldMap.containsKey(key)) {
                apiFieldMap.put(key, fieldApiInfo);
            }
        }

        return apiFieldMap;
    }

    /**
     * 处理同级字段的排序
     * 例子：
     * 一级字段A
     * 一级子类字段
     * A
     * B
     * 一级子类字段B
     * 处理结果：现在要将一级字段字段排成这样
     * 一级字段A
     * 一级子类字段B
     * 一级子类字段
     * A
     * B
     *
     * @param childList
     * @return java.util.List<java.lang.String>
     * @author wangzejun
     * @date 2020年07月06日 23:54:32
     */
    private static List<String> sortChildList(List<String> childList) {
        List<String> resultList = new ArrayList();
        for (String field : childList) {
            if (!field.startsWith("\t")) {
                resultList.add(field);
            }
        }

        for (String field : childList) {
            if (field.startsWith("\t")) {
                resultList.add(field);
            }
        }
        return resultList;
    }

    /**
     * 处理字段成标准的注释
     *
     * @param apiFieldArr
     * @param childrenMap
     * @param firstFieldList
     * @param parentName
     * @param suffixStr
     * @return void
     * @author wangzejun
     * @date 2020年07月06日 23:57:01
     */
    private static void dealWithField(String[] apiFieldArr, Map<String, List<String>> childrenMap, List<FieldApiInfo> firstFieldList, String parentName, String suffixStr) {
        String tempParentName = "";
        for (String apiField : apiFieldArr) {
            String[] fieldArr = apiField.split("\t");
            String fieldName = fieldArr[fieldIndexMap.get(FIELD_NAME)];
            String fieldType = "";
            boolean isCustomType = false;
            if (fieldArr.length >= 3) {
                fieldType = fieldArr[fieldIndexMap.get(TYPE)];
                isCustomType = fieldType.endsWith(FieldApiTypeEnum.ARRAY.getValue()) || fieldType.equalsIgnoreCase(FieldApiTypeEnum.ARRAY.getValue());
                isCustomType = isCustomType || FieldApiTypeEnum.OBJECT.getValue().equalsIgnoreCase(fieldType);
            }
            if (apiField.startsWith("\t") && isCustomType) {
                tempParentName = fieldName;
            } else if (apiField.startsWith("\t") && StringUtils.isNotBlank(tempParentName)) {
                List<String> childList;
                if (childrenMap.containsKey(tempParentName)) {
                    childList = childrenMap.get(tempParentName);
                } else {
                    childList = new ArrayList<>();
                }
                childList.add(apiField.replaceFirst("\t", ""));
                childrenMap.put(tempParentName, childList);
            } else {
                addFirstField(firstFieldList, parentName, fieldArr);
            }

            if (isCustomType) {
                String fieldComment = fieldArr[fieldIndexMap.get(FIELD_NAME_COMMENT)];
                tempParentName = StringUtils.captureName(fieldName) + suffixStr;
                FieldApiInfo fieldApiInfo = new FieldApiInfo();
                fieldApiInfo.setFieldName(tempParentName);
                fieldApiInfo.setFieldComment(fieldComment + "Item");
                firstFieldList.add(fieldApiInfo);
            }
        }
    }

    /**
     * 处理一级字段
     *
     * @param firstFieldList
     * @param parentName
     * @param fieldArr
     * @return void
     * @author wangzejun
     * @date 2020年07月06日 23:55:14
     */
    private static void addFirstField(List<FieldApiInfo> firstFieldList, String parentName, String[] fieldArr) {
        String fieldName = fieldArr[fieldIndexMap.get(FIELD_NAME)];
        String fieldComment = fieldArr[fieldIndexMap.get(FIELD_NAME_COMMENT)];
        //System.out.println(fieldName);
        String fieldDesc = "";
        String required = "";
        String fieldType = "";
        if (fieldArr.length >= 3) {
            fieldType = fieldArr[fieldIndexMap.get(TYPE)];
        }

        if (fieldArr.length >= 3 && fieldArr.length >= 5) {
            fieldDesc = fieldArr[fieldIndexMap.get(FIELD_DESC)];
            required = fieldArr[fieldIndexMap.get(REQUIRED)];
        }

        FieldApiInfo fieldApiInfo = new FieldApiInfo();
        if (StringUtils.isNotBlank(parentName)) {
            fieldApiInfo.setParentName(parentName);
            fieldApiInfo.setFieldName(fieldName);
        } else {
            fieldApiInfo.setParentName("");
            fieldApiInfo.setFieldName(fieldName);
        }

        fieldApiInfo.setType(fieldType);
        if (StringUtils.isNotBlank(fieldComment)) {
            fieldApiInfo.setFieldComment(fieldComment);
        } else if (StringUtils.isNotBlank(fieldDesc)) {
            fieldApiInfo.setFieldComment(fieldDesc);
        }
        fieldApiInfo.setRequired(required);
        firstFieldList.add(fieldApiInfo);
    }


    /**
     * 从json5获取注释
     *
     * @param fieldApiInfoMap
     * @param fieldName
     * @param innerName
     * @return java.lang.String
     * @author wangzejun
     * @date 2020年07月06日 23:55:42
     */
    public static String getFieldComment(Map<String, FieldApiInfo> fieldApiInfoMap, String fieldName, String innerName) {
        if (fieldApiInfoMap == null || fieldApiInfoMap.isEmpty()) {
            return "";
        }

        String key = fieldName;
        if (innerName != null) {
            key = innerName + "-" + fieldName;
        }

        if (!fieldApiInfoMap.containsKey(key)) {
            return "";
        }

        FieldApiInfo fieldApiInfo = fieldApiInfoMap.get(key);
        if (fieldApiInfo == null || !StringUtils.isNotBlank(fieldApiInfo.getFieldComment())) {
            return "";
        }
        return fieldApiInfo.getFieldComment().trim();
    }
}
