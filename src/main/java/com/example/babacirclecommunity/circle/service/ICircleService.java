package com.example.babacirclecommunity.circle.service;

import com.example.babacirclecommunity.circle.entity.Circle;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.circle.vo.CircleVo;
import com.example.babacirclecommunity.circle.vo.CommunityVo;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.home.entity.Community;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/5/20 19:39
 */
public interface ICircleService {

    /**
     * 查询我关注的人发的帖子
     * @param userId
     * @param paging
     * @return
     */
    List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging);

    /**
     * 查询视频或者图片
     * @param type 0图片 ，1视频
     * @param paging
     * @param userId
     * @return
     */
    List<CircleClassificationVo> queryImagesOrVideos(int type,Paging paging,int userId);

    /**
     * 查询单个圈子的帖子
     * @param id 圈子id
     * @param userId 用户id
     * @return
     * @throws ParseException
     */
    CircleClassificationVo querySingleCircle(int id,int userId) throws ParseException;

    /**
     * 查询推荐圈子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    List<CircleClassificationVo> queryReferenceCircles(int userId, Paging paging);

    /**
     * 查询我的圈子 （圈子广场）
     * @param userId 当前登录用户id
     * @param paging 分页
     * @return
     */
    List<CircleVo> queryCheckMyCirclesSquare(int userId, Paging paging);

    /**
     * 添加圈子
     * @param community
     */
    void addCircle(Community community) throws ParseException;

    /**
     * 发布圈子
     * @param circle
     * @param imgUrl 图片地址
     */
    void publishingCircles(Circle circle, String imgUrl) throws ParseException, IOException;

    /**
     * 进入单元体的接口
     * 根据社区分类id查询圈子信息
     * @param id 标签id
     * @param userId 用户id
     * @return
     */
    CommunityVo selectCommunityCategoryId(int id, int userId);

}
