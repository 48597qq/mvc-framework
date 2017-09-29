package com.wengll.framework;

import com.wengll.framework.annotation.Controller;
import com.wengll.framework.helper.BeanHelper;
import com.wengll.framework.helper.ClassHelper;
import com.wengll.framework.helper.IocHelper;
import com.wengll.framework.util.ClassUtil;

/**
 * 加载相应的Helper类
 */
public final class HelperLoader {

    public static void init(){
        Class<?>[] classList = {ClassHelper.class, BeanHelper.class, IocHelper.class, Controller.class};
        for(Class<?> cls : classList){
            ClassUtil.loadClass(cls.getName(), true);
        }
    }
}
