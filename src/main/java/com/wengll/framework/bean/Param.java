package com.wengll.framework.bean;

import com.wengll.framework.util.CastUtil;
import com.wengll.framework.util.StringUtil;

import java.io.File;
import java.util.*;

/**
 * 请求参数对象
 */
public class Param {

    private List<FormParam> formParamList;

    private List<FileParam> fileParamList;

    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }

    /**
     * 获取请求参数映射
     * @return
     */
    private Map<String, Object> getFieldMap(){
        Map<String, Object> fieldMap = new HashMap<>();
        if(formParamList != null && !formParamList.isEmpty()){
            formParamList.forEach(formParam -> {
                String fieldName = formParam.getFieldName();
                Object fieldValue = formParam.getFieldValue();

                if(fieldMap.containsKey(fieldName)){
                    fieldValue = fieldMap.get(fieldName) + StringUtil.SEPARATOR + fieldValue;
                }
                fieldMap.put(fieldName, fieldValue);
            });
        }
        return fieldMap;
    }

    /**
     * 获取上传文件映射
     * @return
     */
    private Map<String, List<FileParam>> getFileMap(){
        Map<String, List<FileParam>> fileMap = new HashMap<>();
        if(fileParamList != null && !fileParamList.isEmpty()){
            fileParamList.forEach(fileParam -> {
                String fieldName = fileParam.getFieldName();

                List<FileParam> fileParamList;
                if(fileMap.containsKey(fieldName)){
                    fileParamList = fileMap.get(fieldName);
                } else {
                    fileParamList = new ArrayList<>();
                }

                fileParamList.add(fileParam);
                fileMap.put(fieldName, fileParamList);
            });
        }
        return fileMap;
    }

    /**
     * 获取所有上传文件
     */
    public List<FileParam> getFileList(String fieldName){
        return getFileMap().get(fieldName);
    }

    /**
     * 获取唯一上传文件
     */
    public FileParam getFile(String fieldName){
        List<FileParam> fileParamList = getFileMap().get(fieldName);
        if(fileParamList != null && !fileParamList.isEmpty()){
            return fileParamList.get(0);
        }
        return null;
    }

    /**
     * 验证参数是否为空
     */
    public boolean isEmpty(){
        return (formParamList == null || formParamList.isEmpty())
                && (fileParamList == null || fileParamList.isEmpty());
    }

    /**
     * 根据参数名获取boolean型参数
     */
    public boolean getBoolean(String name){
        return CastUtil.castBoolean(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取string型参数
     */
    public String getString(String name){
        return CastUtil.castString(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取int型参数
     */
    public int getInt(String name){
        return CastUtil.castInt(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取long型参数
     */
    public long getLong(String name){
        return CastUtil.castLong(getFieldMap().get(name));
    }

    /**
     * 根据参数名获取double型参数
     */
    public double getDouble(String name){
        return CastUtil.castDouble(getFieldMap().get(name));
    }
}
