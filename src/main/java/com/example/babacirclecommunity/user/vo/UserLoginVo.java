package com.example.babacirclecommunity.user.vo;

import lombok.Data;

/**
 * @author JC
 * @date 2021/8/25 14:15
 */
@Data
public class UserLoginVo {

    /**
     * 历史浏览量
     */
    private int historyPageView;

    /**
     * 粉丝数量
     */
    private int fansNum;

    /**
     * 关注数量
     */
    private int attentionNum;
}
