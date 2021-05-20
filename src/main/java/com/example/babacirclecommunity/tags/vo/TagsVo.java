package com.example.babacirclecommunity.tags.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/3/24 14:40
 */
@Data
public class TagsVo {


    /**
     * 每个标签发布帖子的数量
     */
    private int num;

    /**
     *二级标签id
     */
    private int id;

    /**
     * 标签名字
     */
    private String tagName;

    /**
     * 标签头像
     */
    private String imgUrl;
}
