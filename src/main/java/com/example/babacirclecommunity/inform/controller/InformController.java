package com.example.babacirclecommunity.inform.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.inform.service.IInformService;
import com.example.babacirclecommunity.inform.vo.InformCommentVo;
import com.example.babacirclecommunity.inform.vo.InformUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author MQ
 * @date 2021/6/15 11:57
 */
@Api(tags = "通知API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/InformController")
public class InformController {

    @Autowired
    private IInformService iInformService;

    /**
     * 查询评论，获赞通知
     * @return
     */
    @ApiOperation(value = "查询评论，获赞通知",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryCommentsNotice")
    public List<InformUserVo> queryCommentsNotice(int userId, int type, Paging paging)  {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iInformService.queryCommentsNotice(userId,type,paging);
    }

    /**
     * 修改消息状态为已读
     * @return
     */
    @ApiOperation(value = "修改消息状态为已读",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/modifyMessageState")
    public void modifyMessageState(int id)  {
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
         iInformService.modifyMessageState(id);
    }

    /**
     * 根据用户id查询未读的消息数量(评论，点赞)
     * @return
     */
    @ApiOperation(value = "根据用户id查询未读的消息数量(评论，点赞)",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryNumberUnreadMessagesBasedUserId")
    public InformCommentVo queryNumberUnreadMessagesBasedUserId(int userId)  {
        if(userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iInformService.queryNumberUnreadMessagesBasedUserId(userId);
    }


}
