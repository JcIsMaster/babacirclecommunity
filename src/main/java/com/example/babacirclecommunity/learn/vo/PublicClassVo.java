package com.example.babacirclecommunity.learn.vo;

import com.example.babacirclecommunity.learn.entity.ClassList;
import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/4 17:49
 */
@Data
public class PublicClassVo {

    private int id;
    /**
     * 公开课发布人id
     */
    private int uId;
    /**
     * 公开课发布人名称
     */
    private String uName;
    /**
     * 公开课发布人头像
     */
    private String uAvatar;
    /**
     * 公开课发布人介绍
     */
    private String introduce;
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
     * 封面图
     */
    private String coverImg;
    /**
     * 课程列表
     */
    private String classList;
    private List<ClassList> classLists;
    /**
     * 公开课价格 0为免费内容
     */
    private int price;
    /**
     * 收藏数量
     */
    private int collect;
    /**
     * 公开课购买人数
     */
    private int buyerNum;
    /**
     * 发布时间
     */
    private String createAt;
    /**
     * 删除状态1:有效；0:无效； 默认1
     */
    private int isDelete;
    /**
     * 看贴人对该帖子的收藏状态 0:未收藏； 1:已收藏
     */
    private int whetherCollect;
    /**
     * 看贴人对该课程的购买状态 0:未购买； 1:已购买
     */
    private int buyerStatus;
}
