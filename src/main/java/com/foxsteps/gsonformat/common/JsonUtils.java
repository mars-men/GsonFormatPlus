package com.foxsteps.gsonformat.common;

import com.foxsteps.gsonformat.entity.FieldApiInfo;
import com.foxsteps.gsonformat.enums.FieldApiTypeEnum;
import com.foxsteps.gsonformat.tools.json.JSONArray;
import com.foxsteps.gsonformat.tools.json.JSONException;
import com.foxsteps.gsonformat.tools.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangzejun
 * @description 新版json工具
 * @date 2020年07月05日 10:06:03
 */
public class JsonUtils {

    /**
     * 去掉json5的注释
     *
     * @param jsonStr
     * @return java.lang.String
     * @author wangzejun
     * @date 2020年07月06日 23:57:53
     */
    public static String removeComment(String jsonStr) {
        if (!StringUtils.isNotBlank(jsonStr)) {
            return "";
        }
        String json = jsonStr.replaceAll(" ", "");
        String[] jsonArr = json.split("\n");
        if(jsonArr.length>1){
            StringBuffer buffer=new StringBuffer();
            for (String jsonLine : jsonArr) {
                if(jsonLine.contains("//")){
                    buffer.append(jsonLine.substring(0, jsonLine.lastIndexOf("//"))).append("\n");
                }else{
                    buffer.append(jsonLine).append("\n");
                }
            }
            json=buffer.toString();
        }

        //json = json.replaceAll("//.*", "");
        json = json.replaceAll("\n", "");
        json = json.replaceAll("\t", "");
        json = json.replaceAll(",\\}", "}");
        json = json.replaceAll(",\\]", "]");
        return json;
    }

    /**
     * 从json5获取json字段的注释
     *
     * @param sourceJson 源json
     * @param formatJson 格式化后的json
     * @return java.lang.String
     * @author wangzejun
     * @date 2020年07月06日 23:58:15
     */
    public static String getJsonComment(String sourceJson, String formatJson) {
        Map<Integer, String> resultMap = new LinkedHashMap<>(8);
        if (!StringUtils.isNotBlank(sourceJson)) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        String[] splitArr = sourceJson.split("\n");
        for (String s : splitArr) {
            s = s.replaceAll(" ", "");
            s = s.replaceAll("\t", "");
            if (StringUtils.isNotBlank(s) && !s.startsWith("//")) {
                s = s.replaceAll(",\"", ",\n\"");
                s = s.replaceAll("\"\\}", "\"\n}");
                s = s.replaceAll("\\{\"", "{\n\"");
                s=s.replaceAll("\\[\\{", "[\n{");
                s=s.replaceAll("\\}\\]", "}\n]");
                buffer.append(s).append("\n");
            }
        }
        //System.out.println("yuan:"+buffer.toString());
        String[] formatArr = buffer.toString().split("\n");
        int index = 0;
        for (String s : formatArr) {
            StringBuffer fieldBuf = new StringBuffer();
            s=s.replaceAll("http://","");
            if (s.contains(":")) {
                String[] fieldArr = s.split("//");

                String fieldName = fieldArr[0].trim();
                String[] fieldNameArr = fieldName.split(":");
                fieldName = fieldNameArr[0];
                fieldName = fieldName.substring(1, fieldName.length() - 1);
                fieldBuf.append(fieldName);
                fieldBuf.append("\t");
                String fieldType = "string";
                String fieldComment = "";
                if (s.contains("//") && fieldArr.length>1) {
                    fieldComment = fieldArr[fieldArr.length-1];
                }else{
                    fieldComment="";
                }

                if (s.contains("{")) {
                    // "info:{"
                    fieldType = FieldApiTypeEnum.OBJECT.getValue();
                } else if (s.contains("[")) {
                    // "list:["
                    fieldType = FieldApiTypeEnum.ARRAY.getValue();
                } else {
                    fieldType = "string";
                }
                fieldBuf.append(fieldComment);
                fieldBuf.append("\t");
                fieldBuf.append(fieldType);
            }
            if (StringUtils.isNotBlank(fieldBuf.toString())) {
                resultMap.put(index, fieldBuf.toString());
            }
            index++;

        }
        StringBuffer resultBuf = new StringBuffer();
        formatJson=formatJson.replaceAll("\\[\"", "[\n\"");
        formatJson=formatJson.replaceAll("\"\\]", "\"\n]");
        formatJson=formatJson.replaceAll("\\[\\{", "[\n{");
        formatJson=formatJson.replaceAll("\\}\\]", "}\n]");
        //System.out.println("format:"+formatJson.toString());
        String[] goodFieldArr = formatJson.split("\n");
        for (Map.Entry<Integer, String> entry : resultMap.entrySet()) {
            int rowIndex = entry.getKey().intValue();
            String[] tempFieldPartArr = goodFieldArr[rowIndex].split("    ");
            for (int i = 0; i < tempFieldPartArr.length; i++) {
                if (StringUtils.isNotBlank(tempFieldPartArr[i])) {
                    break;
                } else {
                    if (i > 0) {
                        resultBuf.append("\t");
                    }
                }
            }

            resultBuf.append(entry.getValue());
            resultBuf.append("\n");
        }

        return resultBuf.toString();
    }

    /**
     * 格式化json
     *
     * @param json
     * @return java.lang.String
     * @author wangzejun
     * @date 2020年07月05日 10:32:39
     */
    public static String formatJson(String json) throws JSONException {
        if (!StringUtils.isNotBlank(json)) {
            return json;
        }
        json = json.trim();

        if (json.startsWith("{")) {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.toString(4);
        } else if (json.startsWith("[")) {
            JSONArray jsonArray = new JSONArray(json);
            return jsonArray.toString(4);
        }
        return json;
    }

}
