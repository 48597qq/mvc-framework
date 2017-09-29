package com.wengll.framework.helper;

import com.wengll.framework.annotation.Action;
import com.wengll.framework.bean.Handler;
import com.wengll.framework.bean.Request;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 控制器助手类
 */
public final class ControllerHelper {

    // 用于存放请求与处理器的映射关系
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<>();

    static {
        // 获取所有的Controller类
        Set<Class<?>> controlerClassSet = ClassHelper.getControlerClassSet();
        if(!controlerClassSet.isEmpty()){
            // 遍历所有的Controller类
            controlerClassSet.forEach(controlerClass -> {
                // 遍历Controller类中的方法
                Method[] methods = controlerClass.getDeclaredMethods();

                if(ArrayUtils.isNotEmpty(methods)){
                    Stream.of(methods).forEach(method -> {

                        // 判断当前方法上是否带有Action注解
                        if(method.isAnnotationPresent(Action.class)){

                            // 从Action注解中获取URL映射规则
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();

                            // 验证映射规则，例如（get:/test）
                            if(mapping.matches("\\w+:/\\w*")){
                                String[] array = mapping.split(":");
                                if(ArrayUtils.isNotEmpty(array) && array.length == 2){
                                    String requestMethod = array[0];
                                    String requestPath = array[1];

                                    Request request = new Request(requestMethod, requestPath);
                                    Handler handler = new Handler(controlerClass, method);

                                    ACTION_MAP.put(request, handler);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * 获取handler
     * @param requestMethod  请求方法
     * @param requestPath 请求路径
     * @return
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
