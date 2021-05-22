package com.example.babacirclecommunity.circle.controller;

import com.example.babacirclecommunity.circle.service.ICircleGiveService;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MQ
 * @date 2021/5/22 14:55
 */
@Api(tags = "圈子点赞API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/CircleGiveController")
public class CircleGiveController {


    @Autowired
    private ICircleGiveService iCircleGiveService;

    /**
     *
     * 查询我点过赞的圈子帖子
     * @return
     */
    @ApiOperation(value = "查询我点过赞的圈子帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryGiveCircle")
    public List<CircleClassificationVo> queryGiveCircle(int userId, int otherId, Paging paging) {
        if(otherId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCircleGiveService.queryGiveCircle(userId,otherId, paging);
    }

    /**
     *
     * 点赞
     * @return
     */
    @ApiOperation(value = "点赞",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/givePost")
    public int givePost(int id,int userId){
        if(id==0 || userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCircleGiveService.givePost(id, userId);
    }



}
