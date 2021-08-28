package com.example.babacirclecommunity.resource.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.resource.entity.Collection;
import com.example.babacirclecommunity.resource.entity.Resources;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.resource.vo.ResourcesVo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/5/27 17:56
 */
public interface IResourceService {

    /**
     * 查询资源数据
     * @param paging 分页
     * @param orderRule 0 推荐 1 最新 2最热 3爆款
     * @param tagId 标签id
     * @param title 标题
     * @return
     */
    List<ResourceClassificationVo> queryResource(Paging paging,int orderRule,int tagId, String title);

    /**
     * 查询单个资源帖子
     * @param id 帖子id
     * @param userId 用户id
     * @return
     */
    ResourcesVo selectSingleResourcePost(int id, int userId) throws ParseException;

    /**
     * 根据id查询他人发布的货源帖子
     * @param userId
     * @param othersId 他人用户id
     * @param paging 分页
     * @return
     */
    Map<String,Object> queryHavePostedPosts(int userId,int othersId, Paging paging);

    /**
     * 编辑用户资源介绍
     * @param userId
     * @param introduce
     * @return
     */
    ResultUtil editUserResourceIntroduce(int userId, String introduce);

    /**
     * 根据二级标签id查询推荐数据
     * @param id 二级标签id
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    List<ResourceClassificationVo> selectRecommendedSecondaryTagId(int id,int userId,int tid);

    /**
     * 根据一级标签id查询所有视频
     * @param id 一级标签id
     * @param paging 分页
     * @param userId
     * @throws ParseException
     * @return
     */
    List<ResourcesVo> queryAllVideosPrimaryTagId(int id, Paging paging,int userId) throws ParseException;


    /**
     * 收藏帖子
     * @param collection 收藏对象
     * @return
     */
    int collectionPost(Collection collection);

    /**
     * 发布
     * @param resources 对象
     * @param imgUrl 图片地址
     * @throws Exception
     * @return
     */
    int issueResourceOrCircle(Resources resources, String imgUrl) throws Exception;

    /**
     * 得到资源海报图
     * @param pageUrl 二维码指向的地址
     * @param id 帖子id
     * @return
     * @throws Exception
     */
    List<String> getPosterImage(String pageUrl,String id) throws Exception;

    /**
     * 删除货源
     * @param id
     */
    void deleteResourceById(int id);
}
