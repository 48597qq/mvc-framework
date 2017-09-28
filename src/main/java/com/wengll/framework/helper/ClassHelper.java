package com.wengll.framework.helper;

import com.wengll.framework.annotation.Controller;
import com.wengll.framework.annotation.Service;
import com.wengll.framework.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 类操作助手类
 */
public class ClassHelper {

    // 定义类集合，用于存放预加载的类
    private static final Set<Class<?>> CLASS_SET;


    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    /**
     * 获取应用包名下的所有类
     * @return
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * 获取应用包名下所有Service类
     * @return
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> cls : CLASS_SET){
            if(cls.isAnnotationPresent(Service.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包名下所有Controler类
     * @return
     */
    public static Set<Class<?>> getControlerClassSet(){
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> cls : CLASS_SET){
            if(cls.isAnnotationPresent(Controller.class)){
                classSet.add(cls);
            }
        }
        return classSet;
    }

    /**
     * 获取应用包名下所有的bean类（包括Service、Controler类）
     * @return
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanClassSet = new HashSet<>();
        beanClassSet.addAll(getServiceClassSet());
        beanClassSet.addAll(getControlerClassSet());
        return beanClassSet;
    }
}
