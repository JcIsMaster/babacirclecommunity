package com.example.babacirclecommunity.circle.controller;

import com.example.babacirclecommunity.circle.service.IAttentionService;
import com.example.babacirclecommunity.circle.service.ICircleService;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/5/20 19:38
 */
@Api(tags = "圈子API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/CircleController")
public class CircleController {

   @Autowired
   private ICircleService iCircleService;

    /**
     *
     * 查询我关注的人发的帖子
     * @return
     */
    @ApiOperation(value = "查询我关注的人发的帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryPostsPeopleFollow")
    public List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging){
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iCircleService.queryPostsPeopleFollow(userId,paging);
    }


    /**
     * 查询视频或者图片帖子
     * @return
     */
    @ApiOperation(value = "查询视频或者图片帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryImagesOrVideos")
    public List<CircleClassificationVo> queryImagesOrVideos(int type,Paging paging,int userId)  {
        return iCircleService.queryImagesOrVideos(type,paging,userId);
    }

    /**
     *
     * 查询单个圈子的帖子
     * @return
     */
    @ApiOperation(value = "查询单个圈子的帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/querySingleCircle")
    public CircleClassificationVo querySingleCircle(int id,int userId) throws ParseException {
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iCircleService.querySingleCircle(id, userId);
    }



}
