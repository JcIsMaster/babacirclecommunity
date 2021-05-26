package com.example.babacirclecommunity.learn.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/4/16 16:43
 */
@Data
public class DryGoodsVo {

    private int id;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 封面图
     */
    private String coverImg;
}
