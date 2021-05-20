package com.example.babacirclecommunity.tags.vo;

import com.example.babacirclecommunity.tags.entity.Tag;
import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/10 10:25
 */
@Data
public class AllTagVo {

    /**
     * ID
     */
    private int id;

    /**
     * 名称
     */
    private String tagName;

    /**
     * 一级标签ID
     */
    private int tId;

    /**
     * 型(0 资源 1 圈子)
     */
    private int type;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是有有效（1有效，0无效）
     */
    private int isDelete;

    /**
     * 图标地址
     */
    private String imgUrl;

    /**
     * 这个标签发帖数量
     */
    private int num;

    /**
     * 二级标签
     */
    List<Tag> tag;
}
