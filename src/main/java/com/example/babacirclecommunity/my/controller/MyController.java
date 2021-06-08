package com.example.babacirclecommunity.my.controller;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.my.service.IMyService;
import com.example.babacirclecommunity.my.service.impl.MyServiceImpl;
import com.example.babacirclecommunity.my.vo.PeopleCareAboutVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/6/8 11:14
 */
@Api(tags = "我的API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/MyController")
public class MyController {

    @Autowired
    private IMyService iMyService;

    /**
     * 查询我关注的人
     * @return
     */
    @ApiOperation(value = "查询我关注的人",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryPeopleCareAbout")
    public Map<String,Object> queryPeopleCareAbout(Paging paging, int userId){
        return  iMyService.queryPeopleCareAbout(paging,userId);
    }

    /**
     * 查询我的粉丝
     * @return
     */
    @ApiOperation(value = "查询我的粉丝",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryFan")
    public Map<String,Object> queryFan(Paging paging, int userId){
        return  iMyService.queryFan(paging,userId);
    }



}
