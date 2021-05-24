package com.example.babacirclecommunity.circle.vo;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/5/23 16:23
 */
@Data
public class CircleImgIdVo {

    /**
     * 圈子id
     */
    private int id;

    /**
     * 圈子封面图
     */
    private String cover;

    /**
     * 标签id
     */
    private int tagId;

    /**
     * 名称
     */
    private String communityName;
}
