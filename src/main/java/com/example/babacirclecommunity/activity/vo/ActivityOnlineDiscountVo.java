package com.example.babacirclecommunity.activity.vo;

import com.example.babacirclecommunity.activity.entity.ActivityOnlineDiscount;
import lombok.Data;

/**
 * @author JC
 * @date 2021/10/13 17:58
 */
@Data
public class ActivityOnlineDiscountVo extends ActivityOnlineDiscount {

    /**
     * 砍价人名称
     */
    private String userName;

    /**
     * 砍价人头像
     */
    private String avatar;
}
