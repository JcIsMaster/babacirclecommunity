package com.example.babacirclecommunity.personalCenter.controller;

import com.example.babacirclecommunity.circle.vo.CircleVo;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.personalCenter.service.IPersonalCenterService;
import com.example.babacirclecommunity.personalCenter.vo.PersonalVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/20 16:32
 */
@Api(tags = "个人中心API")
@CrossOrigin(origins = "*", maxAge = 100000)
@RestController
@Slf4j
@RequestMapping("/PersonalController")
public class PersonalController {

    @Autowired
    private IPersonalCenterService iPersonalCenterService;

    @ApiOperation(value = "查询个人中心接口",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryPersonalCenter")
    public PersonalVo queryPersonalCenter(int userId,int otherId){
        return iPersonalCenterService.queryPersonalCenter(userId,otherId);
    }

    @ApiOperation(value = "查询创建或加入的圈子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryCircleByUSerId")
    public List<CircleVo> queryCircleByUSerId(int otherId, int type, Paging paging){
        return iPersonalCenterService.queryCircleByUSerId(otherId,type,paging);
    }
}
