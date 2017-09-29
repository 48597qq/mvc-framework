package com.wengll.framework.helper;

import com.wengll.framework.annotation.Inject;
import com.wengll.framework.util.ReflectionUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 依赖注入助手类
 */
public class IocHelper {
    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if(!beanMap.isEmpty()){
            // 获取当前的Class类和类别的实例
            beanMap.forEach((beanClass, beanInstance) -> {
                // 获取bean的类定义的所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtils.isNotEmpty(beanFields)) {
                    Stream.of(beanFields).forEach(beanField -> {
                        // 判断当前Bean Field是否带有Inject注解
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null) {
                                // 将类中带有Inject注解的成员变量注入实例
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    });
                }
            });
        }
    }
}
