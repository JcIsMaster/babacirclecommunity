package com.example.babacirclecommunity.plan.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/7/12 16:04
 */
@Data
public class Plan {

    private int id;

    /**
     * 目标参数
     */
    private String optionsArray;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效（0 有效  1 无效）
     */
    private int isDelete;
}
