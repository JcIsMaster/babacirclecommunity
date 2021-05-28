package com.example.babacirclecommunity.common.utils;


import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author MQ
 * @PROJECT_NAME: demo
 * @DESCRIPTION:
 * @DATE: 2020/6/18 16:37
 */
public class FilesUtils {

    //图片存储路径
    private static final String FTP_BASEPATH = "D:/yijia/";
    private static final String DATA_PATH = "D:/yijia/";



    /**
     * 格式化时间
     */
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /**
     *
     * @param file 文件
     * @param fileName 原文件名
     * @return
     */
    public String upload(MultipartFile file, String fileName){

        //按时间每天分开存储
        String timePath = sdf.format(new Date());

        //生成新的文件名称
        fileName = getFileName(fileName);

        // 生成新的文件名
        String realPath = FTP_BASEPATH + "/" + timePath + File.separator + fileName;

        //文件地址
        File dest = new File(realPath);

        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            String path = DATA_PATH + timePath +"/"+ fileName;
            return path;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return "false";
        } catch (IOException e) {
            e.printStackTrace();
            return "false";
        }

    }

    /**
     * 生成新的文件名
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName){
        return 123+ getSuffix(fileOriginName);
    }


    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
