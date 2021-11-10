package com.example.babacirclecommunity.circle.service;

import com.example.babacirclecommunity.circle.entity.Circle;
import com.example.babacirclecommunity.circle.entity.CommunityTopic;
import com.example.babacirclecommunity.circle.entity.CommunityUser;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.circle.vo.CircleVo;
import com.example.babacirclecommunity.circle.vo.CommunitySearchVo;
import com.example.babacirclecommunity.circle.vo.CommunityVo;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.home.entity.Community;
import com.example.babacirclecommunity.user.vo.UserRankVo;
import com.example.babacirclecommunity.user.vo.UserVo;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

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
     * 查询视频帖子详情
     * @param id
     * @param paging
     * @param userId
     * @throws ParseException
     * @return
     */
    List<CircleClassificationVo> queryCircleOfVideos(int id,Paging paging,int userId) throws ParseException;

    /**
     * 查询单个圈子的帖子
     * @param id 圈子id
     * @param userId 用户id
     * @param paging
     * @return
     * @throws ParseException
     */
    CircleClassificationVo querySingleCircle(int id,int userId,Paging paging) throws ParseException;

    /**
     * 查询推荐圈子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    List<CircleClassificationVo> queryReferenceCircles(int userId, Paging paging);

    /**
     * 查询广场热门话题
     * @param userId
     * @return
     */
    Map<String,Object> queryHotTopic(int userId);

    /**
     * 查询所有话题
     * @return
     */
    List<CommunityTopic> queryAllTopic();

    /**
     * 根据话题查询圈子
     * @param userId
     * @param topicId
     * @param paging
     * @return
     */
    List<CircleVo> queryCommunityByTopic(int userId,int topicId,Paging paging);

    /**
     * 添加圈子
     * @param community
     * @throws ParseException
     */
    void addCircle(Community community) throws ParseException;

    /**
     * 发布圈子
     * @param circle
     * @param imgUrl 图片地址
     * @throws ParseException,IOException,InterruptedException,Exception
     */
    void publishingCircles(Circle circle, String imgUrl) throws ParseException, IOException, InterruptedException, Exception;

    /**
     * 进入单元体的接口
     * 根据社区分类id查询圈子信息
     * @param id 标签id
     * @param userId 用户id
     * @return
     */
    CommunityVo selectCommunityCategoryId(int id, int userId);

    /**
     * 加入圈子
     * @param communityUser
     * @return
     */
    int joinCircle(CommunityUser communityUser);

    /**
     * 单元体导航栏点击查询
     * @param typeId 单元体导航栏id
     * @param userId 用户id
     * @param tagId 标签id
     * @param paging 分页
     * @return
     */
    List<CircleClassificationVo> queryClickUnitNavigationBar(int typeId,int userId,int tagId,Paging paging);


    /**
     * 发现圈子(新)
     * @param userId
     * @param paging
     * @return
     */
    Map<String,Object> fundCircles(int userId, Paging paging);

    /**
     * 我的圈子
     * @param userId
     * @param paging
     * @return
     */
    Map<String,Object> myCircles(int userId, Paging paging);

    /**
     * 我的圈子-加入的圈子
     * @param userId
     * @param paging
     * @return
     */
    Map<String,Object> joinedCircles(int userId, Paging paging);

    /**
     * 官方圈子列表
     * @param paging
     * @return
     */
    List<Community> queryOfficialCircleList(Paging paging);

    /**
     * 修改圈子
     * @param community
     */
    void updateCircle(Community community);

    /**
     * 成员管理
     * @param communityId 圈子id
     * @param userId 用户id
     */
    void memberManagement(int communityId,int userId);

    /**
     * 查询圈子成员
     * @param communityId 圈子id
     * @param userId
     * @return
     */
    List<UserVo> queryCircleMembers(int communityId,int userId);

    /**
     * 添加单元体标签
     * @param tagId 标签id
     * @param hName 名称
     */
    void addTagHaplont(int tagId,String hName );

    /**
     * 删除圈子
     * @param id 帖子id
     * @param tagId
     */
    void deletePosts(int id,int tagId);

    /**
     * 删除帖子
     * @param id 帖子id
     */
    void deleteCircles(int id);

    /**
     * 置顶我的圈子
     * @param id
     * @param userId
     */
    void TopPosts(int id,int userId);

    /**
     * 根据圈子名称模糊查询圈子
     * @param communityName
     * @return
     */
    List<CommunitySearchVo> queryCirclesByName(String communityName);

    /**
     * 查询热门圈子列表
     * @param paging
     * @param userId
     * @return
     */
    List<CircleVo> queryHotCircleList(int userId,Paging paging);

    /**
     * 设置圈子排行榜开关/规则
     * @param rankingSwitch
     * @param rankingRules
     * @param id
     * @return
     */
    ResultUtil setCircleRanking(int rankingSwitch, String rankingRules, int id);

    /**
     * 查看圈子排行榜
     * @param communityId
     * @return
     */
    List<UserRankVo> queryCircleRanking(int communityId);
}
