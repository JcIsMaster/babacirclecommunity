package com.example.babacirclecommunity.resource.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.resource.vo.ResourcesVo;

import java.text.ParseException;
import java.util.List;

/**
 * @author MQ
 * @date 2021/5/27 17:56
 */
public interface IResourceService {

    /**
     * 查询资源数据
     * @param paging 分页
     * @param orderRule 0 推荐 1 最新 2最热
     * @param tagId 标签id
     * @param title 标题
     * @return
     */
    List<ResourceClassificationVo> queryResource(Paging paging,int orderRule,  int tagId, String title);

    /**
     * 查询单个资源帖子
     * @param id 帖子id
     * @param userId 用户id
     * @return
     */
    ResourcesVo selectSingleResourcePost(int id, int userId) throws ParseException;

    /**
     * 根据id查询他人发布的货源帖子
     * @param othersId 他人用户id
     * @param paging 分页
     * @return
     */
    List<ResourceClassificationVo> queryHavePostedPosts(int othersId, Paging paging);
}
