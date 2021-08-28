package com.example.babacirclecommunity.my.service;

import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.my.entity.ComplaintsSuggestions;
import com.example.babacirclecommunity.my.vo.CommentsDifferentVo;
import com.example.babacirclecommunity.my.vo.GreatDifferentVo;
import com.example.babacirclecommunity.my.vo.PeopleCareAboutVo;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.user.entity.User;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/6/8 11:15
 */
public interface IMyService {

    /**
     * 查询我关注的人
     * @param paging 分页
     * @param userId 用户id
     * @return
     */
    List<PeopleCareAboutVo> queryPeopleCareAbout(Paging paging, int userId);

    /**
     * 查询我的粉丝
     * @param paging 分页
     * @param userId 用户id
     * @return
     */
    List<PeopleCareAboutVo> queryFan(Paging paging, int userId);

    /**
     * 建议
     * @param complaintsSuggestions
     * @return
     */
    int addComplaintsSuggestions(ComplaintsSuggestions complaintsSuggestions);

    /**
     * 点击头像进入的接口
     * @param bUserId 被观看人id
     * @param userId 观看人id
     * @return
     */
    void clickInterfaceHeadImageEnter(int bUserId, int userId);

    /**
     * 查询看过我的人
     * @param userId 登录人id
     * @param paging 分页
     * @return
     */
    Map<String,Object> queryPeopleWhoHaveSeenMe(int userId,Paging paging);

    /**
     * 修改用户信息
     * @param user
     * @return
     * @throws ParseException
     */
    int updateUserInformation(User user) throws ParseException;

    /**
     * 根据状态查询不同模块的评论
     * @param paging
     * @param userId 用户id
     * @return
     */
    List<CommentsDifferentVo> queryCommentsDifferentModulesBasedStatus(Paging paging, Integer userId);

    /**
     * 根据状态查询不同模块的点赞
     * @param paging
     * @param userId
     * @return
     */
    List<GreatDifferentVo> queryGreatDifferentModulesBasedStatus(Paging paging, Integer userId);

    /**
     * 根据状态查询不同模块的收藏
     * @param paging 分页
     * @param status 状态  0货源1合作2干货
     * @param userId 用户id
     * @return
     */
    Object queryFavoritesDifferentModulesAccordingStatus(Paging paging,Integer status,Integer userId);

    /**
     * 查询我近一个月浏览过的帖子
     * @param userId 用户id
     * @param tagsOne 12货源源  13合作
     * @param paging 分页
     * @return
     */
    List<CircleClassificationVo> queryCheckPostsBeenReadingPastMonth(int userId, int tagsOne, Paging paging);

    /**
     * 查询我发布的帖子
     * @param userId
     * @param paging
     * @return
     */
    List<CircleClassificationVo> queryMyCirclePost(int userId,Paging paging);

    /**
     * 我的圈子(常逛3+我创建的)
     * @param userId
     * @param paging
     * @return
     */
    Map<String,Object> myCircles(int userId, Paging paging);

    /**
     * 查询我发布的货源帖子
     * @param userId
     * @param tagId
     * @param paging
     * @return
     */
    List<ResourceClassificationVo> queryMyPostedPosts(int userId, int tagId, Paging paging);
}
