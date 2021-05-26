package com.example.babacirclecommunity.learn.service.impl;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.learn.dao.DryGoodsCollectMapper;
import com.example.babacirclecommunity.learn.dao.DryGoodsGiveMapper;
import com.example.babacirclecommunity.learn.dao.QuestionMapper;
import com.example.babacirclecommunity.learn.entity.Collect;
import com.example.babacirclecommunity.learn.entity.Give;
import com.example.babacirclecommunity.learn.entity.Question;
import com.example.babacirclecommunity.learn.service.IQuestionService;
import com.example.babacirclecommunity.learn.vo.QuestionTagVo;
import com.example.babacirclecommunity.user.dao.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JC
 * @date 2021/4/29 10:46
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DryGoodsGiveMapper dryGoodsGiveMapper;

    @Autowired
    private DryGoodsCollectMapper dryGoodsCollectMapper;

    @Override
    public int addQuestion(Question question) {
        question.setCreateAt(System.currentTimeMillis() / 1000 + "");
        return questionMapper.addQuestion(question);
    }

    @Override
    public QuestionTagVo queryQuestionById(int id, int userId) {
        QuestionTagVo questionTagVo = questionMapper.queryQuestionById(id);
        //获取发帖人名称,头像
        String uName = userMapper.selectUserById(questionTagVo.getUId()).getUserName();
        String avatar = userMapper.selectUserById(questionTagVo.getUId()).getAvatar();
        if(uName==null || avatar==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        questionTagVo.setUName(uName);
        questionTagVo.setAvatar(avatar);
        //如果userId为0，用户处于未登录状态，状态设为未点赞
        if (userId == 0){
            questionTagVo.setWhetherGive(0);
            questionTagVo.setWhetherCollect(0);
            return questionTagVo;
        }
        //我是否对该帖子点过赞
        Integer giveStatus = dryGoodsGiveMapper.whetherGive(0,userId, questionTagVo.getId());
        if (giveStatus == 0) {
            questionTagVo.setWhetherGive(0);
        } else {
            questionTagVo.setWhetherGive(1);
        }
        //我是否对该帖子收过藏
        Integer collectStatus = dryGoodsCollectMapper.whetherCollect(0,userId, questionTagVo.getId());
        if (collectStatus == 0) {
            questionTagVo.setWhetherCollect(0);
        } else {
            questionTagVo.setWhetherCollect(1);
        }
        return questionTagVo;
    }

    @Override
    public int giveLike(int id, int userId) {
        //查询数据库是否存在该条数据
        Give give = dryGoodsGiveMapper.selectCountWhether(0,userId,id);
        if(give == null){
            int i = dryGoodsGiveMapper.giveLike(0,id, userId, System.currentTimeMillis() / 1000 + "");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = questionMapper.updateQuestionGive(id,"+");
            if(j<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return j;
        }
        int i = 0;
        int j = 0;
        //如果当前状态是1 那就改为0 取消点赞
        if(give.getGiveCancel()==1){
            i = dryGoodsGiveMapper.updateGiveStatus(give.getId(), 0);
            j = questionMapper.updateQuestionGive(id,"-");
        }
        //如果当前状态是0 那就改为1 为点赞状态
        if(give.getGiveCancel()==0){
            i = dryGoodsGiveMapper.updateGiveStatus(give.getId(), 1);
            j = questionMapper.updateQuestionGive(id,"+");
        }
        if(i<=0 || j<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return j;
    }

    @Override
    public int giveCollect(int id, int userId) {
        //查询数据库是否存在该条数据
        Collect collect = dryGoodsCollectMapper.selectCountWhether(0,userId,id);
        if(collect == null){
            int i = dryGoodsCollectMapper.giveCollect(0,id, userId, System.currentTimeMillis() / 1000 + "");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = questionMapper.updateQuestionCollect(id,"+");
            if(j<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return j;
        }
        int i = 0;
        int j = 0;
        //如果当前状态是1 那就改为0 取消收藏
        if(collect.getGiveCancel()==1){
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 0);
            j = questionMapper.updateQuestionCollect(id,"-");
        }
        //如果当前状态是0 那就改为1 为收藏状态
        if(collect.getGiveCancel()==0){
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 1);
            j = questionMapper.updateQuestionCollect(id,"+");
        }
        if(i<=0 || j<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return j;
    }
}
