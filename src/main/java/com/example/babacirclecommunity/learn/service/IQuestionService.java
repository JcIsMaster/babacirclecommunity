package com.example.babacirclecommunity.learn.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.learn.entity.Question;
import com.example.babacirclecommunity.learn.vo.QuestionTagVo;
import com.example.babacirclecommunity.learn.vo.QuestionVo;
import com.example.babacirclecommunity.personalCenter.vo.QuestionPersonalVo;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/29 10:41
 */
public interface IQuestionService {

    /**
     * 发布提问帖
     * @param question
     * @return
     */
    int addQuestion(Question question);

    /**
     * 根据id查询提问帖详情
     * @param id
     * @param userId
     * @return
     */
    QuestionTagVo queryQuestionById(int id,int userId);

    /**
     * 提问帖点赞
     * @param id
     * @param userId
     * @return
     */
    int giveLike(int id,int userId);

    /**
     * 提问帖收藏
     * @param id
     * @param userId
     * @return
     */
    int giveCollect(int id,int userId);

    /**
     * 查询提问人个人中心
     * @param userId
     * @param otherId
     * @param paging
     * @return
     */
    QuestionPersonalVo queryQuestionPersonal(int userId,int otherId,Paging paging);

    /**
     * 得到提问海报
     * @param id 帖子id
     * @param pageUrl 二维码指向的地址
     * @return
     */
    List<String> getQuestionPosters(String id, String pageUrl);
}
