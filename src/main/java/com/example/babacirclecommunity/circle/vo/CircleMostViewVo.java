package com.example.babacirclecommunity.circle.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/8/28 11:50
 */
@Data
public class CircleMostViewVo {

    /**
     * 圈子标签id
     */
    private int tagId;

    /**
     * 圈子标签名
     */
    private String communityName;

    /**
     * 圈子封面
     */
    private String posters;
}
