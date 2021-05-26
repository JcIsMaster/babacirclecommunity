package com.example.babacirclecommunity.gold.vo;

import com.example.babacirclecommunity.gold.entity.UserGoldCoins;
import lombok.Data;

import java.util.List;

/**
 * @author MQ
 * @date 2021/4/14 15:22
 */
@Data
public class UserSignInVo {

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户金币对象
     */
    private UserGoldCoins userGoldCoins;

    /**
     * 签到金币数量
     */
    private List<Integer> listGold;


}
