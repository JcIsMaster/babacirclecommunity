package com.example.babacirclecommunity.personalCenter.vo;

import com.example.babacirclecommunity.learn.vo.QuestionVo;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/27 19:59
 */
@Data
public class QuestionPersonalVo {

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论时间
     */
    private String createAt;

    /**
     * 问题id
     */
    private int tId;

    /**
     * 问题title
     */
    private String title;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 提问人id
     */
    private int userId;

    /**
     * 提问人名字
     */
    private String userName;

    /**
     * 提问人头像
     */
    private String avatar;

    /**
     * 问题是否删除 0.已删除 1.未删除
     */
    private int isDelete;

    /**
     * 点赞数量
     */
    private int favourNum;

    /**
     * 评论数量
     */
    private int commentNum;

    /**
     * 是否点赞
     */
    private int whetherGive;
}
