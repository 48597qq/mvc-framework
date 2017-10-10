package com.wengll.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class StringUtil {

    public static final String SEPARATOR = String.valueOf((char) 29);

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(str != null){
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否非空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
