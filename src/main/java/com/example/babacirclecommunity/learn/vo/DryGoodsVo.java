package com.example.babacirclecommunity.learn.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/16 16:43
 */
@Data
public class DryGoodsVo {

    private int id;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 封面图
     */
    private String coverImg;
    /**
     * 二级标签id
     */
    private int tagsTwo;
    /**
     * 提问tagsTwo对应的tagName
     */
    private String tagName;
    /**
     * 创建时间
     */
    private String createAt;
    /**
     * 发帖人id
     */
    private int uId;
}
