package com.wengll.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 */
public class ReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建实例
     */
    public static Object newInstance(Class<?> cls){
        Object instance = null;
        try {
            instance = cls.newInstance();
        } catch (Exception e){
            LOGGER.error("new instance failure", e);
            throw new RuntimeException(e);
        }
        return instance;
    }
}
