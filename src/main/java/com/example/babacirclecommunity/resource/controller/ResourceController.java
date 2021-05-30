package com.example.babacirclecommunity.resource.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.resource.service.IResourceService;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/5/27 17:55
 */
@Api(tags = "资源API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/ResourceController")
public class ResourceController {

    @Autowired
    private IResourceService iResourceService;

    /**
     *
     * 查询资源数据
     * @return
     */
    @ApiOperation(value = "查询资源数据",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryResource")
    public List<ResourceClassificationVo> queryResource(Paging paging,int orderRule,int tagId, String title){
        return iResourceService.queryResource(paging,orderRule,tagId,title);
    }

}
