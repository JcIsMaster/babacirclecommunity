package com.example.babacirclecommunity.circle.controller;


import com.example.babacirclecommunity.circle.entity.Attention;
import com.example.babacirclecommunity.circle.service.IAttentionService;
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
 * @date 2021/3/6 13:21
 */
@Api(tags = "关注API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/AttentionController")
public class AttentionController {

    @Autowired
    private IAttentionService iAttentionService;

    /**
     *
     * 添加关注信息
     * @return
     */
    @ApiOperation(value = "添加关注信息",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addAttention")
    public int addAttention(Attention attention){
        if(attention.getGuId()==0 || attention.getBgId()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return  iAttentionService.addAttention(attention);
    }

    /**
     *
     * 查询我关注的人发的去圈子帖子
     * @return
     */
    @ApiOperation(value = "查询我关注的人发的帖子",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryPostsPeopleFollow")
    public List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging){
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iAttentionService.queryPostsPeopleFollow(userId,paging);
    }

}
