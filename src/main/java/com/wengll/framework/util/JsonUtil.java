package com.wengll.framework.util;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON工具类
 */
public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static final Gson gson = new Gson();

    /**
     * 将POJO转成JSON
     */
    public static <T> String toJson(T obj){
        return gson.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> type){
        return gson.fromJson(json, type);
    }
}
