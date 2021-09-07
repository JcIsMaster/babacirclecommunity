package com.example.babacirclecommunity.learn.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.learn.entity.Question;
import com.example.babacirclecommunity.learn.vo.QuestionTagVo;
import com.example.babacirclecommunity.learn.vo.QuestionVo;
import com.example.babacirclecommunity.personalCenter.vo.QuestionPersonalVo;

import java.text.ParseException;
import java.util.List;

/**
 * @author JC
 * @date 2021/4/29 10:41
 */
public interface IQuestionService {

    /**
     * 查询提问信息
     * @param userId 用户id
     * @param orderRule 排序
     * @param tagId 标签id
     * @param content 搜索内容
     * @param paging 分页
     * @return
     */
    List<QuestionTagVo> queryQuestionList(int userId,int orderRule,Integer tagId,Integer planClassId,String content,Paging paging);

    /**
     * 发布提问帖
     * @param question
     * @param imgUrl
     * @return
     * @throws Exception
     */
    ResultUtil addQuestion(Question question,String imgUrl) throws Exception;

    /**
     * 根据id查询提问帖详情
     * @param id
     * @param userId
     * @return
     */
    QuestionVo queryQuestionById(int id,int userId);

    /**
     * 提问帖点赞
     * @param id
     * @param userId
     * @return
     */
    int giveLike(int id,int userId,int thumbUpId);

    /**
     * 提问帖收藏
     * @param id
     * @param userId
     * @return
     */
    int giveCollect(int id,int userId);

    /**
     * 根据用户id查询我的回答列表
     * @param userId
     * @param paging
     * @return
     */
    List<QuestionPersonalVo> queryMyAskList(int userId,Paging paging);

    /**
     * 得到提问海报
     * @param id 帖子id
     * @param pageUrl 二维码指向的地址
     * @return
     */
    List<String> getQuestionPosters(String id, String pageUrl);
}
