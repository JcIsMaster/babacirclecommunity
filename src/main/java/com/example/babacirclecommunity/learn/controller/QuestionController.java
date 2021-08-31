package com.example.babacirclecommunity.learn.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.learn.entity.Question;
import com.example.babacirclecommunity.learn.service.IQuestionService;
import com.example.babacirclecommunity.learn.vo.QuestionTagVo;
import com.example.babacirclecommunity.learn.vo.QuestionVo;
import com.example.babacirclecommunity.personalCenter.vo.QuestionPersonalVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author JC
 * @date 2021/4/29 11:45
 */
@Api(tags = "提问API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/QuestionController")
public class QuestionController {

    @Autowired
    private IQuestionService iQuestionService;

    /**
     * 查询提问信息
     * @return
     */
    @ApiOperation(value = "查询提问信息",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryQuestionList")
    public List<QuestionTagVo> queryQuestionList(int userId,int orderRule,Integer tagId,Integer planClassId,String content,Paging paging){
        if(paging.getPage()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"page不要传0 或者参数错误");
        }
        return iQuestionService.queryQuestionList(userId,orderRule,tagId,planClassId,content,paging);
    }

    /**
     * 发布提问帖
     * @param question
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "发布提问帖",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/addQuestion")
    public ResultUtil addQuestion(Question question,String imgUrl) throws Exception{
        return iQuestionService.addQuestion(question,imgUrl);
    }

    /**
     * 根据id查询提问详情
     * @param id
     * @param userId
     * @return
     * @throws ParseException
     */
    @ApiOperation(value = "根据id查询提问详情",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryQuestionById")
    public QuestionVo queryQuestionById(int id, int userId) throws Exception{
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iQuestionService.queryQuestionById(id,userId);
    }

    /**
     * 提问帖点赞
     * @param id 提问帖子id
     * @param userId 点赞人id
     * @return
     */
    @ApiOperation(value = "点赞",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/giveLike")
    public int giveLike(int id, int userId,int thumbUpId){
        if(id==0 || userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iQuestionService.giveLike(id,userId,thumbUpId);
    }

    /**
     * 提问帖收藏
     * @param id 提问帖子id
     * @param userId 收藏人id
     * @return
     */
    @ApiOperation(value = "收藏",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/giveCollect")
    public int giveCollect(int id, int userId){
        if(id==0 || userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iQuestionService.giveCollect(id,userId);
    }

    /**
     * 查询提问人个人中心
     * @param userId
     * @param otherId
     * @return
     */
    @ApiOperation(value = "查询提问人个人中心",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryQuestionPersonal")
    public QuestionPersonalVo queryQuestionPersonal(int userId, int otherId, Paging paging){
        return iQuestionService.queryQuestionPersonal(userId,otherId,paging);
    }

    /**
     * 得到提问海报
     * @param id 帖子id
     * @param pageUrl 二维码指向的地址
     * @return
     */
    @ApiOperation(value = "得到提问海报",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/getQuestionPosters")
    public List<String> getQuestionPosters(String id, String pageUrl){
        return iQuestionService.getQuestionPosters(id,pageUrl);
    }

}
