package com.example.babacirclecommunity.my.entity;

import lombok.Data;

/**
 * @author MQ
 * @date 2021/3/12 17:03
 * 投诉与建议实体类
 */
@Data
public class ComplaintsSuggestions {

    private int id;

    /**
     * 内容
     */
    private String content;

    /**
     * 投诉人id
     */
    private int userId;


    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效（1有效，0无效） 默认1
     */
    private int isDelete;
}
