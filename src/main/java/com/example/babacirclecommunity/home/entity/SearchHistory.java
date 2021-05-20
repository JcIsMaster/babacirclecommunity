package com.example.babacirclecommunity.home.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/5/20 16:33
 */
@Data
public class SearchHistory {

    /**
     * 历史内容id
     */
    private int id;

    /**
     * 历史内容
     */
    private String historicalContent;

    /**
     * 用户id
     */
    private int userId;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效（1有效 0无效） 默认1
     */
    private int isDelete;
}
