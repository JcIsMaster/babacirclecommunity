package com.example.babacirclecommunity.circle.entity;

import lombok.Data;

/**
 * @author MQ
 * @create 2021/2/22
 * 浏览记录 实体类
 **/
@Data
public class Browse {

    private int id;

    /**
     * 用户id
     */
    private int uId;

    /**
     * 帖子id
     */
    private int zqId;

    /**
     * 资源或圈子(0资源，1圈子)
     */
    private int type;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 是否删除(1有效，0无效)
     */
    private int isDelete;
}
