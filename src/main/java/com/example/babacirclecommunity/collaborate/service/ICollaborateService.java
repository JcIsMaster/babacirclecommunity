package com.example.babacirclecommunity.collaborate.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.resource.entity.Collection;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.resource.vo.ResourcesVo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/5/31 10:46
 */
public interface ICollaborateService {

    /**
     * 查询合作数据
     * @param paging
     * @param orderRule
     * @param tagId
     * @param title
     * @return
     */
    List<ResourceClassificationVo> queryCollaborate(Paging paging, int orderRule, int tagId, String title);

    /**
     *  查询单个合作帖子
     * @param id
     * @param userId
     * @throws ParseException
     * @return
     */
    ResourcesVo querySingleCollaboratePost(int id, int userId) throws ParseException;

    /**
     * 根据id查询他人发布的合作帖子
     * @param userId
     * @param othersId
     * @param paging
     * @return
     */
    Map<String,Object> queryHaveCollaboratePostedPosts(int userId, int othersId, Paging paging);

    /**
     * 编辑用户资源介绍
     * @param userId
     * @param introduce
     * @return
     */
    ResultUtil editUserCollaborateIntroduce(int userId, String introduce);

    /**
     * 根据二级标签id查询推荐数据 点进帖子详情 触发
     * @param id
     * @param userId
     * @param tid
     * @return
     */
    List<ResourceClassificationVo> queryRecommendedSecondaryTagId(int id,int userId,int tid);

    /**
     * 根据一级标签id查询所有视频
     * @param id
     * @param paging
     * @param userId
     * @return
     * @throws ParseException
     */
    List<ResourcesVo> queryAllCollaborateVideosPrimaryTagId(int id, Paging paging,int userId) throws ParseException;

    /**
     * 收藏帖子
     * @param collection
     * @return
     */
    int collectionCollaboratePost(Collection collection);
}
