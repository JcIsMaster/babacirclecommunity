package com.example.babacirclecommunity.learn.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.learn.entity.DryGoods;
import com.example.babacirclecommunity.learn.entity.LearnPostExceptional;
import com.example.babacirclecommunity.learn.vo.DryGoodsTagVo;
import com.example.babacirclecommunity.personalCenter.vo.DryGoodsPersonalVo;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/16 15:56
 */
public interface IDryGoodsService {

    /**
     * 根据状态查询学习信息
     * @param type 0:提问; 1:干货; 2:公开课
     * @param paging 分页
     * @param orderRule 排序规则 0:推荐  1:最新  2:最热
     * @param tagId 标签id
     * @param content 搜索内容
     * @return
     */
    Object queryLearnList(int type, Paging paging, int orderRule,Integer tagId,String content);

    /**
     * 根据id查询干货详情
     * @param id 干货id
     * @param userId 用户id
     * @return
     */
    DryGoodsTagVo queryDryGoodsById(int id,int userId);

    /**
     * 发布干货帖
     * @param dryGoods
     * @return
     */
    int addDryGoods(DryGoods dryGoods);

    /**
     * 干货帖点赞
     * @param id 干货帖子id
     * @param userId 点赞人id
     * @param thumbUpId 被点赞人用户id
     * @return
     */
    int giveLike(int id, int userId,int thumbUpId);

    /**
     * 干货帖收藏
     * @param id 干货帖子id
     * @param userId 收藏人id
     * @return
     */
    int giveCollect(int id, int userId);

    /**
     * 干货帖打赏
     * @param learnPostExceptional 打赏详情对象
     * @return
     */
    ResultUtil rewardGoldToDryGoods(LearnPostExceptional learnPostExceptional);

    /**
     * 查询干货个人中心
     * @param userId
     * @param otherId
     * @param paging
     * @return
     */
    DryGoodsPersonalVo queryDryGoodsPersonal(int userId, int otherId, Paging paging);

    /**
     * 得到干货海报
     * @param id 帖子id
     * @param pageUrl 二维码指向的地址
     * @return
     */
    List<String> getDryPosters(String id, String pageUrl);
}
