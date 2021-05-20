package com.example.babacirclecommunity.circle.vo;

import lombok.Data;

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
     * 每个社区的人数
     */
    private int cnt;
}
