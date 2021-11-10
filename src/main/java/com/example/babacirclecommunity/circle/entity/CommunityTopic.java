package com.example.babacirclecommunity.circle.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/10/5 17:03
 */
@Data
public class CommunityTopic {

    /**
     * id
     */
    private int id;

    /**
     * 话题名
     */
    private String topicName;

    /**
     * 创建时间
     */
    private String createAt;
}
