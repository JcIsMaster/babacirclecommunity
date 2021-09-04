package com.example.babacirclecommunity.gold.service;


import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.gold.entity.GoldCoinChange;
import com.example.babacirclecommunity.gold.entity.PostExceptional;
import com.example.babacirclecommunity.gold.vo.SingInVo;
import com.example.babacirclecommunity.gold.vo.UserGoldCoinsVo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/4/13 14:31
 */
public interface IGoldService {



    /**
     * 打赏帖子
     * @param rewardedUserId 被打赏人id
     * @param postExceptional
     * @return
     */
    ResultUtil postExceptional(int rewardedUserId, PostExceptional postExceptional);

    /**
     * 签到
     * @param userId 签到用户id
     * @param goldNumber 签到得到的金币数量
     */
    void signIn(int userId,int goldNumber);

    /**
     * 查询签到的数据
     * @param userId 用户id
     * @return
     */
    UserGoldCoinsVo queryCheckedInData(Integer userId) throws ParseException;

    /**
     * 查询金币变化数据
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    Map<String,Object> queryGoldCoinChange(Integer userId, Paging paging);

    /**
     *查询签到
     * @param userId 用户id
     * @return
     */
    List<SingInVo> querySign(Integer userId);
}
