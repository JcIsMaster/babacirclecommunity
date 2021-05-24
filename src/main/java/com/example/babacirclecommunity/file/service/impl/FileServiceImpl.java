package com.example.babacirclecommunity.file.service.impl;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.file.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
 * @author MQ
 * @date 2021/5/23 20:09
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl implements IFileService {
    @Override
    public void deleteFile(int type, String imgUrl) {
        //得到最后一个斜杆后面的值
        String substring = imgUrl.substring(imgUrl.lastIndexOf("/"));

        String documentType="";
        //0代表是图片
        if(type==0){
            documentType="img";
        }

        //1代表是视屏
        if(type==1){
            documentType="video";
        }
        File file = new File("e://file/"+documentType+""+substring+"");
        //判断文件是否存在
        if (file.exists()){
            boolean delete = file.delete();
            if(!delete){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"删除服务器文件错误!");
            }
        }else{
            throw new ApplicationException(CodeType.SERVICE_ERROR,"图片不存在!");
        }
    }
}
