package com.example.babacirclecommunity.sameCity.entity;

import lombok.Data;

/**
 * @author JC
 * @date 2021/10/23 17:28
 */
@Data
public class ParameterJson {

    /**
     * 是否商家
     */
    private int is_shops;

    /**
     * 是职业还是状态
     */
    private int is_status;

    /**
     * 平台
     */
    private String platform;

    /**
     * 平台logo
     */
    private String platformLogo;

    /**
     * 店铺名
     */
    private String stor;

    /**
     * 职业
     */
    private String job;

    /**
     * 状态
     */
    private String status;

    /**
     * 需求
     */
    private String twoSelect;

    /**
     * 速度
     */
    private String thSelect;
}
