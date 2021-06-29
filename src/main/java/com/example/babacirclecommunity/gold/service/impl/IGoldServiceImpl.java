package com.example.babacirclecommunity.gold.service.impl;


import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.common.utils.TimeUtil;
import com.example.babacirclecommunity.gold.dao.GoldMapper;
import com.example.babacirclecommunity.gold.entity.GoldCoinChange;
import com.example.babacirclecommunity.gold.entity.PostExceptional;
import com.example.babacirclecommunity.gold.entity.UserGoldCoins;
import com.example.babacirclecommunity.gold.service.IGoldService;
import com.example.babacirclecommunity.gold.vo.GoldTimeVo;
import com.example.babacirclecommunity.gold.vo.SingInVo;
import com.example.babacirclecommunity.gold.vo.UserGoldCoinsVo;
import com.example.babacirclecommunity.weChatPay.dao.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author MQ
 * @date 2021/4/13 14:31
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IGoldServiceImpl implements IGoldService {

    @Autowired
    private GoldMapper goldMapper;

    @Autowired
    private OrderMapper orderMapper;


    @Override
    public ResultUtil postExceptional(int rewardedUserId, PostExceptional postExceptional) {
        postExceptional.setCreateAt(System.currentTimeMillis() / 1000 + "");

        //根据用户id查询自己所有的金币
        UserGoldCoins userGoldCoins = goldMapper.queryUserGoldNumber(postExceptional.getUId());

        //先用可提现的金币进行打赏
        if (userGoldCoins.getCanWithdrawGoldCoins() < postExceptional.getAmountGoldCoins()) {
            if (userGoldCoins.getMayNotWithdrawGoldCoins() < postExceptional.getAmountGoldCoins()) {
                return ResultUtil.success(null, "你的金币不足", 403);
            }

            //修改用户不可提现金币
            int i = goldMapper.updateUserGold("may_not_withdraw_gold_coins=may_not_withdraw_gold_coins-" + postExceptional.getAmountGoldCoins(), postExceptional.getUId());
            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "修改金币失败");
            }

            //被打赏人的金币数量增加
            int i2 = goldMapper.updateUserGold("can_withdraw_gold_coins=can_withdraw_gold_coins+" + postExceptional.getAmountGoldCoins(), rewardedUserId);
            if (i2 <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "修改金币失败");
            }

            //帖子打赏
            int i1 = goldMapper.addPostExceptional(postExceptional);
            if (i1 <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "打赏失败");
            }

            //添加金币变化数据
            GoldCoinChange goldCoinChange = new GoldCoinChange();
            goldCoinChange.setCreateAt(System.currentTimeMillis() / 1000 + "");
            goldCoinChange.setUserId(rewardedUserId);
            goldCoinChange.setSourceGoldCoin("打赏");
            goldCoinChange.setPositiveNegativeGoldCoins("+" + postExceptional.getAmountGoldCoins());
            int i3 = orderMapper.addGoldCoinChange(goldCoinChange);
            if (i3 <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "打赏失败");
            }

            return ResultUtil.success(i1, "成功", 200);
        }

        //修改用户可提现金币
        int i = goldMapper.updateUserGold("can_withdraw_gold_coins=can_withdraw_gold_coins-" + postExceptional.getAmountGoldCoins(), postExceptional.getUId());
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改金币失败");
        }

        //被打赏人的金币数量增加
        int i2 = goldMapper.updateUserGold("can_withdraw_gold_coins=can_withdraw_gold_coins+" + postExceptional.getAmountGoldCoins(), rewardedUserId);
        if (i2 <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改金币失败");
        }

        //打赏
        int i1 = goldMapper.addPostExceptional(postExceptional);
        if (i1 <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "打赏失败");
        }

        //添加金币变化数据
        GoldCoinChange goldCoinChange = new GoldCoinChange();
        goldCoinChange.setCreateAt(System.currentTimeMillis() / 1000 + "");
        goldCoinChange.setUserId(rewardedUserId);
        goldCoinChange.setSourceGoldCoin("打赏");
        goldCoinChange.setPositiveNegativeGoldCoins("+" + postExceptional.getAmountGoldCoins());
        int i3 = orderMapper.addGoldCoinChange(goldCoinChange);
        if (i3 <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "金币充值失败");
        }

        return ResultUtil.success(i1, "成功", 200);
    }

    @Override
    public void signIn(int userId, int goldNumber) {
        //修改用户金币数据
        int i = goldMapper.updateUserGoldSignIn(userId, goldNumber, System.currentTimeMillis() / 1000 + "");
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "签到失败");
        }

        //添加金币变化数据
        GoldCoinChange goldCoinChange = new GoldCoinChange();
        goldCoinChange.setCreateAt(System.currentTimeMillis() / 1000 + "");
        goldCoinChange.setUserId(userId);
        goldCoinChange.setSourceGoldCoin("签到");
        goldCoinChange.setPositiveNegativeGoldCoins("+" + goldNumber);
        int i1 = orderMapper.addGoldCoinChange(goldCoinChange);
        if (i1 <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "签到失败");
        }

    }

    @Override
    public UserGoldCoinsVo queryCheckedInData(Integer userId) throws ParseException {
        //查询当前用户的签到天数 如果等于7天 就将签到天数修改为0 从第一天开始签到
        int i2 = goldMapper.queryConsecutiveNumberById(userId);
        if (i2 == 7) {
            int i = goldMapper.updateConsecutiveNumberById(userId);
            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "签到失败错误！");
            }
        }

        UserGoldCoinsVo userGoldCoinsVo = new UserGoldCoinsVo();

        //查询用户连续签到天数和上一次签到时间
        UserGoldCoins userGoldCoins = goldMapper.queryUserGoldNumber(userId);
        userGoldCoinsVo.setConsecutiveNumber(userGoldCoins.getConsecutiveNumber());

        long l = Long.parseLong(userGoldCoins.getLastCheckinTime());
        boolean thisTime = TimeUtil.getThisTime(l);
        if (thisTime == true) {
            userGoldCoinsVo.setWhetherCanCheckIn(0);
        } else {
            userGoldCoinsVo.setWhetherCanCheckIn(1);
        }

        return userGoldCoinsVo;
    }

    @Override
    public List<GoldCoinChange> queryGoldCoinChange(Integer userId, Paging paging) {
        Integer page = (paging.getPage() - 1) * paging.getLimit();
        String sql = "limit " + page + "," + paging.getLimit() + "";

        List<GoldCoinChange> goldCoinChanges = goldMapper.queryGoldCoinChange(userId, sql);
        return goldCoinChanges;
    }

    @Override
    public List<SingInVo> querySign(Integer userId) {
        List<SingInVo> singInVoList=new ArrayList<>();

        List<GoldTimeVo> goldTimeVos = goldMapper.querySign(userId);
        for (int i=0;i<goldTimeVos.size();i++){

            //得到时间戳取出年，月，日
            Date date=new Date(goldTimeVos.get(i).getCreateAt()*1000);
            Calendar now = Calendar.getInstance();
            now.setTime(date);
            int year=now.get(Calendar.YEAR);
            int month=now.get(Calendar.MONTH)+1;
            int day=now.get(Calendar.DAY_OF_MONTH);

            SingInVo singInVo=new SingInVo();
            singInVo.setYear(year);
            singInVo.setMonth(month);
            singInVo.setDay(day);
            singInVo.setType("holiday");
            singInVo.setMark("已签到");
            singInVo.setBgColor("#cce6ff");
            singInVo.setColor("#2a97ff");
            singInVoList.add(singInVo);
        }

        return singInVoList;
    }
}
