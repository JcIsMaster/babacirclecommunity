package com.example.babacirclecommunity.plan.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/7/14 16:09
 */
@Data
public class ClassVideo {

    private int id;

    /**
     * 视频名
     */
    private String videoName;

    /**
     * 视频介绍
     */
    private String videoDesc;

    /**
     * 视频封面
     */
    private String videoCover;

    /**
     * 视频地址
     */
    private String videoAddress;

    /**
     * 类型0.视频  1.图文  默认0
     */
    private int videoType;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效
     */
    private int isDelete;
}
