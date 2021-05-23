package com.example.babacirclecommunity.circle.vo;

import lombok.Data;

import java.util.List;

/**
 * @author MQ
 * @date 2021/3/11 15:16
 */
@Data
public class CircleVo {

    /**
     * 圈子id
     */
    private int id;

    /**
     * 标签id
     */
    private int tagId;

    /**
     * 社区头像
     */
    private String posters;

    /**
     * 社区名称
     */
    private String communityName;

    /**
     * 社区介绍
     */
    private String introduce;

    /**
     * 每个社区的人数
     */
    private int cnt;

    /**
     * 是否官方 （0不是，1是）默认为不是
     */
    private int whetherOfficial;

    /**
     * 每个圈子的最新圈子封面和id
     */
    List<CircleImgIdVo> circleVoList;
}
