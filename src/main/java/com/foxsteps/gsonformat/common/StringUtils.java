package com.foxsteps.gsonformat.common;

import org.apache.http.util.TextUtils;

/**
 * Created by dim on 16/11/5.
 */
public class StringUtils {

    /**
     * 转成驼峰
     *
     * @param text
     * @return
     */
    public static String captureStringLeaveUnderscore(String text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        String temp = text.replaceAll("^_+", "");

        if (!TextUtils.isEmpty(temp)) {
            text = temp;
        }
        String[] strings = text.split("_");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            stringBuilder.append(captureName(strings[i]));
        }
        return stringBuilder.toString();
    }

    public static boolean isAcronym(String word) {
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isFirsrtUpper(String word) {
        char c = word.charAt(0);
        if(Character.isUpperCase(c)){
            return true;
        }
        return false;
    }


    public static String captureName(String text) {

        if (text.length() > 0) {
            text = text.substring(0, 1).toUpperCase() + text.substring(1);
        }
        return text;
    }

    public static String getPackage(String generateClassName) {
        int index = generateClassName.lastIndexOf(".");
        if (index > 0) {
            return generateClassName.substring(0, index);
        }
        return null;
    }

    public static boolean isNotBlank(String str) {
        if (str == null || "".equals(str.trim())) {
            return false;
        }
        return true;
    }

}
