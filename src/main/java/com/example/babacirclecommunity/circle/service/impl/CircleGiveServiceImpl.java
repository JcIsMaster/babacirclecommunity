package com.example.babacirclecommunity.circle.service.impl;

import com.example.babacirclecommunity.circle.dao.AttentionMapper;
import com.example.babacirclecommunity.circle.dao.CircleGiveMapper;
import com.example.babacirclecommunity.circle.dao.CircleMapper;
import com.example.babacirclecommunity.circle.dao.CommentMapper;
import com.example.babacirclecommunity.circle.service.ICircleGiveService;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.common.utils.DateUtils;
import com.example.babacirclecommunity.common.utils.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MQ
 * @date 2021/5/22 14:59
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CircleGiveServiceImpl implements ICircleGiveService {

    @Autowired
    private CircleGiveMapper circleGiveMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<CircleClassificationVo> queryGiveCircle(int userId, int otherId, Paging paging) {
        Integer pages=(paging.getPage()-1)*paging.getLimit();
        String pagings=" limit "+pages+","+paging.getLimit()+"";

        List<CircleClassificationVo> circles = circleGiveMapper.queryGiveCircle(otherId, pagings);
        for (int i=0;i<circles.size();i++){

            //得到图片组
            String[] strings = circleMapper.selectImgByPostId(circles.get(i).getId());
            circles.get(i).setImg(strings);

            //得到点过赞人的头像
            String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
            circles.get(i).setGiveAvatar(strings1);

            //得到点赞数量
            Integer integer1 = circleGiveMapper.selectGiveNumber(circles.get(i).getId());
            circles.get(i).setGiveNumber(integer1);


            //等于0在用户没有到登录的情况下 直接设置没有点赞
            if(userId==0){
                circles.get(i).setWhetherGive(0);
                circles.get(i).setWhetherAttention(0);
            }else{
                //查看我是否关注了此人
                int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                if(i1>0){
                    circles.get(i).setWhetherAttention(1);
                }

                //查询是否对帖子点了赞   0没有 1有
                Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                if(integer>0){
                    circles.get(i).setWhetherGive(1);
                }
            }


            //得到帖子评论数量
            Integer integer2 = commentMapper.selectCommentNumber(circles.get(i).getId());
            circles.get(i).setNumberPosts(integer2);


            //将时间戳转换为多少天或者多少个小时和多少年
            String time = DateUtils.getTime(circles.get(i).getCreateAt());
            circles.get(i).setCreateAt(time);
        }

        return circles;
    }
}
