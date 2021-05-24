package com.example.babacirclecommunity.file.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Upload;
import com.example.babacirclecommunity.file.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author MQ
 * @date 2021/5/23 19:32
 */
@Api(tags = "文件API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/UpdateController")
public class UpdateController {

    @Autowired
    private Upload upload;


    @Autowired
    private IFileService iFileService;

    @ApiOperation(value = "文件上传", notes = "文件上传")
    @ResponseBody
    @PostMapping("/uploadFile")
    public List<String> uploadFile(@RequestParam("files") MultipartFile file) throws Exception {
        return this.upload.upload(file);
    }

    @ApiOperation(value = "删除服务器图片", notes = "删除服务器图片")
    @ResponseBody
    @PostMapping("/deleteFile")
    public void deleteFile(int type,String imgUrl) {
        if(type>1){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        iFileService.deleteFile(type,imgUrl);
    }



}
