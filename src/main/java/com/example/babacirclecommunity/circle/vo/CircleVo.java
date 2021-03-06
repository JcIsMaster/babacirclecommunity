package com.example.babacirclecommunity.circle.vo;

import com.example.babacirclecommunity.circle.entity.Haplont;
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
     * 用户id
     */
    private int userId;

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
     * 帖子数量
     */
    private int cnt;

    /**
     * 圈子成员数量
     */
    private int memberCount;

    /**
     * 是否官方 （0不是，1是）默认为不是
     */
    private int whetherOfficial;

    /**
     *是否公开 （0不是，1是）默认是
     */
    private int whetherPublic;

    /**
     *是否加入了圈子 （0不是，1是）
     */
    private int whetherJoined = 0;

    /**
     * 每个圈子的最新圈子封面和id
     */
    List<CircleImgIdVo> circleVoList;

    List<Haplont> haplonts;


}
