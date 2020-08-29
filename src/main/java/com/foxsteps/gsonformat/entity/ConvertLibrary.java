package com.foxsteps.gsonformat.entity;

import com.foxsteps.gsonformat.config.Config;
import com.foxsteps.gsonformat.config.Constant;

/**
 * Created by didm on 16/11/7.
 */
public enum ConvertLibrary {

    /**
     * 转换类库
     */
    Jackson,
    FastJson,
    Gson,
    LoganSquare,
    AutoValue,
    Other,
    Lombok;

    public static ConvertLibrary from() {
        return from(Config.getInstant().getAnnotationStr());
    }

    private static ConvertLibrary from(String annotation) {
        if (Config.getInstant().getAnnotationStr().equals(Constant.gsonAnnotation)) {
            return Gson;
        }
        if (Config.getInstant().getAnnotationStr().equals(Constant.fastAnnotation)) {
            return FastJson;
        }
        if (Config.getInstant().getAnnotationStr().equals(Constant.loganSquareAnnotation)) {
            return LoganSquare;
        }
        if (Config.getInstant().getAnnotationStr().equals(Constant.autoValueAnnotation)) {
            return AutoValue;
        }
        if (Config.getInstant().getAnnotationStr().equals(Constant.jacksonAnnotation)) {
            return Jackson;
        }
        if (Config.getInstant().getAnnotationStr().equals(Constant.lombokAnnotation)) {
            return Lombok;
        }
        return Other;
    }
}
