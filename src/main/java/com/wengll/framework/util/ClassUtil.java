package com.wengll.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取类加载器
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     * @param className
     * @param isInitialized
     * @return
     */
    public static Class<?> loadClass(String className, boolean isInitialized){
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e){
            LOGGER.error("load class failure", e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url != null){
                    String protocol = url.getProtocol();
                    if(protocol.equals("file")){
                        // 加载当前路径下的class文件
                        String packagePath = url.getPath().replaceAll("%20", " ");
                        addClass(classSet, packagePath, packageName);
                    } else if(protocol.equals("jar")){
                        // 加载jar包当中的类
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if(jarURLConnection != null){
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if(jarFile != null){
                                Enumeration<JarEntry> jarEntitries = jarFile.entries();
                                while (jarEntitries.hasMoreElements()){
                                    JarEntry jarEntry = jarEntitries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if(jarEntryName.endsWith(".class")){
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                        doAddClass(classSet, className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            LOGGER.error("get class set failture", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName){
        File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());

        for(File file : files){
            String fileName = file.getName();
            if(file.isFile()){
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if(StringUtil.isNotEmpty(className)){
                    // 完整类的路径 = 包名 + "." + 类名
                    className = packageName + "." + className;
                }

                doAddClass(classSet, className);
            } else {
                // 获取包路径
                String subPackagePath = fileName;
                if(StringUtil.isNotEmpty(subPackagePath)){
                    subPackagePath = packagePath + "/" + subPackagePath;
                }

                // 获取包名
                String subPackageName = fileName;
                if(StringUtil.isNotEmpty(subPackageName)){
                    subPackageName = packageName + "/" + subPackageName;
                }

                // 迭代
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet, String className){
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }
}
