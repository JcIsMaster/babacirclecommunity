package com.example.babacirclecommunity.CircleFriends.controller;

import com.example.babacirclecommunity.CircleFriends.service.ICircleFriendsService;
import com.example.babacirclecommunity.CircleFriends.vo.CircleFriendsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MQ
 * @date 2021/4/6 13:32
 */
@Api(tags = "海报API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/CircleFriendsController")
public class CircleFriendsController {

    @Autowired
    private ICircleFriendsService iCircleFriendsService;

    /**
     * 得到朋友圈分享图
     * @return
     */
    @ApiOperation(value = "得到朋友圈分享图",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectCircleFriendsFigure")
    public List<String> selectCircleFriendsFigure(String pageUrl,String id)  {
       return iCircleFriendsService.selectCircleFriendsFigure(pageUrl,id);
    }


    /**
     * 查询圈子海报图
     * @return
     */
    @ApiOperation(value = "查询圈子海报图",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryCirclePoster")
    public String[] queryCirclePoster()  {
        return iCircleFriendsService.queryCirclePoster();
    }



}
