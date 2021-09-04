package com.example.babacirclecommunity.learn.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/19 15:37
 */
@Data
public class DryGoodsTagVo {

    private int id;
    /**
     * 发帖人id
     */
    private int uId;
    /**
     * 标题
     */
    private String title;
    /**
     * 一级标签id
     */
    private int tagsOne;
    /**
     * 二级标签id
     */
    private int tagsTwo;
    /**
     * 单元体类型id
     */
    private int haplontId;
    /**
     * 点赞数量
     */
    private int favour;
    /**
     * 收藏数量
     */
    private int collect;
    /**
     * 描述
     */
    private String description;
    /**
     * 封面图
     */
    private String coverImg;
    /**
     * 内容
     */
    private String content;
    /**
     * 删除状态 1:有效；0:无效； 默认1
     */
    private int isDelete;
    /**
     * 发布时间
     */
    private String createAt;
    /**
     * 干货tagsTwo对应的tagName
     */
    private String tagName;
    /**
     * 收到金币数量
     */
    private int goldNum;
    /**
     * 看贴人对该帖子的点赞状态 0:未点赞； 1:已点赞
     */
    private int whetherGive;
    /**
     * 看贴人对该帖子的收藏状态 0:未收藏； 1:已收藏
     * private int whetherCollect;
     */
}
