package com.example.babacirclecommunity.circle.service.impl;

import com.example.babacirclecommunity.circle.dao.*;
import com.example.babacirclecommunity.circle.entity.Browse;
import com.example.babacirclecommunity.circle.service.ICircleService;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.circle.vo.CommentUserVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.DateUtils;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/5/20 19:39
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CircleServiceImpl implements ICircleService {

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private CircleGiveMapper circleGiveMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private BrowseMapper browseMapper;

    @Override
    public List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";
        //查询我关注的人发的帖子
        List<CircleClassificationVo> circleClassificationVos = circleMapper.queryAttentionPerson(userId,sql);
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

    @Override
    public List<CircleClassificationVo> queryImagesOrVideos(int type, Paging paging, int userId) {
        Integer pages=(paging.getPage()-1)*paging.getLimit();
        String pagings=" limit "+pages+","+paging.getLimit()+"";

        List<CircleClassificationVo> circles = circleMapper.queryImagesOrVideos(type, pagings);
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

    @Override
    public CircleClassificationVo querySingleCircle(int id, int userId) throws ParseException {
        //查询单个圈子
        CircleClassificationVo circleClassificationVo = circleMapper.querySingleCircle(id);
        if(circleClassificationVo==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该圈子的帖子不存在");
        }
        //在用户登录的情况下 增加帖子浏览记录
        if(userId!=0){
            //得到上一次观看帖子的时间
            Browse browse = new Browse();
            String s = browseMapper.selectCreateAt(id, userId);
            if(s==null){
                //增加浏览记录
                browse.setCreateAt(System.currentTimeMillis()/1000+"");
                browse.setUId(userId);
                browse.setZqId(id);
                browse.setType(1);
                //增加浏览记录
                int i = browseMapper.addBrowse(browse);
                if(i<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
                }

                //修改帖子浏览数量
                int i1 = circleMapper.updateBrowse(id);
                if(i1<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            }else{
                //得到过去时间和现在的时间是否相隔1440分钟 如果相隔了 就添加新的浏览记录
                long minutesApart = TimeUtil.getMinutesApart(s);
                if(minutesApart>=1440){
                    //增加浏览记录
                    browse.setCreateAt(System.currentTimeMillis()/1000+"");
                    browse.setUId(userId);
                    browse.setZqId(id);
                    browse.setType(1);
                    //增加浏览记录
                    int i = browseMapper.addBrowse(browse);
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
                    }

                    //修改帖子浏览数量
                    int i1 = circleMapper.updateBrowse(id);
                    if(i1<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }

                }
            }

        }


        //得到图片组
        String[] strings = circleMapper.selectImgByPostId(circleClassificationVo.getId());
        circleClassificationVo.setImg(strings);

        //得到点过赞人的头像
        String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circleClassificationVo.getId());
        circleClassificationVo.setGiveAvatar(strings1);

        //得到点赞数量
        Integer integer1 = circleGiveMapper.selectGiveNumber(circleClassificationVo.getId());
        circleClassificationVo.setGiveNumber(integer1);


        //等于0在用户没有到登录的情况下 直接设置没有点赞
        if(userId==0){
            circleClassificationVo.setWhetherGive(0);
        }else{
            //查看我是否关注了此人
            int i1 = attentionMapper.queryWhetherAttention(userId, circleClassificationVo.getUId());
            if(i1>0){
                circleClassificationVo.setWhetherAttention(1);
            }

            //查询是否对帖子点了赞   0没有 1有
            Integer integer = circleGiveMapper.whetherGive(userId, circleClassificationVo.getId());
            if(integer==0){
                circleClassificationVo.setWhetherGive(0);
            }else{
                circleClassificationVo.setWhetherGive(1);
            }
        }


        //得到帖子评论数量
        Integer integer2 = commentMapper.selectCommentNumber(circleClassificationVo.getId());
        circleClassificationVo.setNumberPosts(integer2);

        //将时间戳转换为多少天或者多少个小时和多少年
        String time = DateUtils.getTime(circleClassificationVo.getCreateAt());
        circleClassificationVo.setCreateAt(time);


        return circleClassificationVo;
    }
}
