package com.example.babacirclecommunity.circle.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/10/5 16:53
 */
@Data
public class HotTopicVo {

    private int id;

    /**
     * 话题名
     */
    private String topicName;

    /**
     * 话题参与人数
     */
    private int cs;
}
