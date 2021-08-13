package com.example.babacirclecommunity.circle.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/8/12 17:38
 */
@Data
public class CommunitySearchVo {

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
     * 是否官方 （0不是，1是）默认为不是
     */
    private int whetherOfficial;
}
