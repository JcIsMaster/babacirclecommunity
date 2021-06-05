package com.example.babacirclecommunity.talents.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/6/5 14:43
 */
@Data
public class TalentsVo {
    /**
     * id实质为user_id
     */
    private int id;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别 0女 1男
     */
    private int sex;
    /**
     * 城市
     */
    private String city;
    /**
     * 主要标签
     */
    private String tagPrimary;
    /**
     * 个性标签1
     */
    private String tagIndividualityOne;
    /**
     * 个性标签2
     */
    private String tagIndividualityTwo;
    /**
     * 一句话介绍
     */
    private String introduction;
    /**
     * 创建时间
     */
    private String createAt;
    /**
     * 是否展示(删除)
     */
    private int isDelete;
    /**
     * 是否关注
     */
    private int whetherAttention = 0;
    /**
     * 背景图
     */
    private String picture;
    /**
     * 介绍
     */
    private String introduce;
}
