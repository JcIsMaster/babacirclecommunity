package com.example.babacirclecommunity.home.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/3/3 13:39
 * 社区实体类
 */
@Data
public class Community {

    private int id;

    /**
     * 社区名称
     */
    private String communityName;

    /**
     * 社区海报
     */
    private String posters;

    /**
     * 圈主用户id
     */
    private String userId;

    /**
     * 圈子介绍
     */
    private String introduce;

    /**
     * 圈子公告
     */
    private String announcement;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效（1有效，0 无效）默认1
     */
    private int isDelete;

    /**
     * 0资源圈子 1圈子的圈子
     */
    private int type;

    /**
     * 标签id 对应tb_tags表中的id
     */
    private int tagId;
}
