package com.wengll.framework.helper;

import com.wengll.framework.bean.FileParam;
import com.wengll.framework.bean.FormParam;
import com.wengll.framework.bean.Param;
import com.wengll.framework.util.FileUtil;
import com.wengll.framework.util.StreamUtil;
import com.wengll.framework.util.StringUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件上传助手类
 */
public class UploadHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);

    /**
     * Apache Commons FileUpload提供的文件上传对象
     */
    private static ServletFileUpload servletFileUpload;

    /**
     * 初始化
     */
    public static void init(ServletContext servletContext){
        File respository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, respository));
        int uploadLimit = ConfigHelper.getAppUploadLimit();
        if(uploadLimit != 0){
            servletFileUpload.setFileSizeMax(uploadLimit);
        }
    }

    /**
     * 判断请求是否为multipart类型
     */
    public static boolean isMultipart(HttpServletRequest request){
        return ServletFileUpload.isMultipartContent(request);
    }

    public static Param createParam(HttpServletRequest request) throws IOException{
        List<FormParam> formParamList = new ArrayList<>();
        List<FileParam> fileParamList = new ArrayList<>();

        try {
            Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
            if(fileItemListMap != null && !fileItemListMap.isEmpty()){

                for(Map.Entry<String, List<FileItem>> fileItemListEntry : fileItemListMap.entrySet()){
                    String fieldName = fileItemListEntry.getKey();
                    List<FileItem> fileItemList = fileItemListEntry.getValue();

                    if(fileItemList != null && !fileItemList.isEmpty()){
                        for (FileItem fileItem : fileItemList){

                            if(fileItem.isFormField()){
                                String fieldValue = fileItem.getString("UTF-8");

                                // 设置form表单参数
                                formParamList.add(new FormParam(fieldName, fieldValue));
                            } else {
                                String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(), "UTF-8"));
                                if(StringUtil.isNotEmpty(fileName)){
                                    long fileSize = fileItem.getSize();
                                    String contentType = fileItem.getContentType();
                                    InputStream inputStream = fileItem.getInputStream();

                                    // 设置上传文件
                                    fileParamList.add(new FileParam(fieldName, fileName, fileSize, contentType, inputStream));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            LOGGER.error("create param failure", e);
            throw new RuntimeException(e);
        }
        return new Param(formParamList, fileParamList);
    }

    /**
     * 上传单个文件
     * @param basePath  上传路径
     * @param fileParam 文件对象
     */
    public static void uploadFile(String basePath, FileParam fileParam){
        try {
            if(fileParam != null){
                // 获取完整路径
                String filePath = basePath + fileParam.getFileName();

                // 创建文件
                FileUtil.createFile(filePath);

                // 创建输入输出通道
                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));

                // 复制
                StreamUtil.copyStream(inputStream, outputStream);
            }
        } catch (Exception e){
            LOGGER.error("upload file failure", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量上传文件
     * @param basePath 上传路径
     * @param fileParamList 文件对象列表
     */
    public static void uploadFile(String basePath, List<FileParam> fileParamList){
        try {
            if(fileParamList != null && !fileParamList.isEmpty()){
                for(FileParam fileParam : fileParamList){
                    uploadFile(basePath, fileParam);
                }
            }
        } catch (Exception e){
            LOGGER.error("upload file failure", e);
            throw new RuntimeException(e);
        }
    }
}
