package com.example.babacirclecommunity.talents.controller;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.talents.entity.Talents;
import com.example.babacirclecommunity.talents.service.ITalentService;
import com.example.babacirclecommunity.talents.vo.TalentsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author JC
 * @date 2021/6/4 14:31
 */
@Api(tags = "人才API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/TalentsController")
public class TalentsController {

    @Autowired
    private ITalentService iTalentService;

    @ApiOperation(value = "查询人才列表",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryTalentsList")
    public List<Talents> queryTalentsList(int userId,String content, String city, Paging paging){
        return iTalentService.queryTalentsList(userId,content,city,paging);
    }

    @ApiOperation(value = "根据id查询人才名片",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryTalentById")
    public TalentsVo queryTalentById(int userId){
        return iTalentService.queryTalentById(userId);
    }
}
