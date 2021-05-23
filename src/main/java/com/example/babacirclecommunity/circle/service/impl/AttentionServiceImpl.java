package com.example.babacirclecommunity.circle.service.impl;


import com.example.babacirclecommunity.circle.dao.AttentionMapper;
import com.example.babacirclecommunity.circle.dao.CircleGiveMapper;
import com.example.babacirclecommunity.circle.dao.CircleMapper;
import com.example.babacirclecommunity.circle.dao.CommentMapper;
import com.example.babacirclecommunity.circle.entity.Attention;
import com.example.babacirclecommunity.circle.service.IAttentionService;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.circle.vo.CommentUserVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author MQ
 * @date 2021/3/6 13:26
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AttentionServiceImpl implements IAttentionService {

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private CircleGiveMapper circleGiveMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int addAttention(Attention attention) {
        if(attention.getUserId()==attention.getBgId()){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"自己不能关注自己哦");
        }

        //查询是否关注了他人
        int i=0;
        Attention attention1 = attentionMapper.queryWhetherExist(attention.getUserId(), attention.getBgId());
        if (attention1!= null) {
            //如果当前状态是1关注的  在进这个判断就是修改为0不关注的状态
            if(attention1.getIsDelete()==1){
                //修改关注状态 为取消关注
                i= attentionMapper.updatePostingFollow(0, attention.getUserId(), attention.getBgId());
            }
            if(attention1.getIsDelete()==0){
                //修改关注状态 为关注
                i = attentionMapper.updatePostingFollow(1, attention.getUserId(), attention.getBgId());
            }

        }else{
            //添加关注信息
            attention.setCreateAt(System.currentTimeMillis()/1000+"");
            i = attentionMapper.addAttention(attention);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
        }

        return i;
    }

    @Override
    public List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";
        //查询我关注的人发的帖子
        List<CircleClassificationVo> circleClassificationVos = attentionMapper.queryAttentionPerson(userId,sql);
        for (int i=0;i<circleClassificationVos.size();i++){

            //得到图片组
            String[] strings = circleMapper.selectImgByPostId(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setImg(strings);

            //得到点过赞人的头像
            String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setGiveAvatar(strings1);

            //得到点赞数量
            Integer integer1 = circleGiveMapper.selectGiveNumber(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setGiveNumber(integer1);


            //等于0在用户没有到登录的情况下 直接设置没有点赞
            if(userId==0){
                circleClassificationVos.get(i).setWhetherGive(0);
                circleClassificationVos.get(i).setWhetherAttention(0);
            }else{

                //查询是否对帖子点了赞   0没有 1有
                Integer integer = circleGiveMapper.whetherGive(userId, circleClassificationVos.get(i).getId());
                if(integer>0){
                    circleClassificationVos.get(i).setWhetherGive(1);
                }
            }


            //得到帖子评论数量
            Integer integer2 = commentMapper.selectCommentNumber(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setNumberPosts(integer2);

            //将所有关注状态为1关注状态
            circleClassificationVos.get(i).setWhetherAttention(1);
        }

        return circleClassificationVos;
    }
}
