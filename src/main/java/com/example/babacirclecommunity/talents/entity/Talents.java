package com.example.babacirclecommunity.talents.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/6/4 11:59
 */
@Data
public class Talents {

    /**
     * id
     */
    private int id;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 职位
     */
    private String position;

    /**
     * 特长（擅长）
     */
    private String specialty;

    /**
     * 一句话介绍
     */
    private String introduction;

    /**
     * 图片作品
     */
    private String imgWorks;

    /**
     * 视频作品
     */
    private String videoWorks;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否展示(删除)
     */
    private int isDelete;

}
