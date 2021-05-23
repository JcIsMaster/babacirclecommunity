package com.example.babacirclecommunity.file;

import com.example.babacirclecommunity.common.utils.Upload;
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

    @ApiOperation(value = "文件上传", notes = "文件上传")
    @ResponseBody
    @PostMapping("/uploadFile")
    public List<String> uploadFile(@RequestParam("files") MultipartFile file) throws Exception {
        return this.upload.upload(file);
    }

}
