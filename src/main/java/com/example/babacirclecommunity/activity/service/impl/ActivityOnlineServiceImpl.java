package com.example.babacirclecommunity.activity.service.impl;

import com.example.babacirclecommunity.activity.dao.ActivityOnlineMapper;
import com.example.babacirclecommunity.activity.entity.*;
import com.example.babacirclecommunity.activity.service.IActivityOnlineService;
import com.example.babacirclecommunity.activity.vo.*;
import com.example.babacirclecommunity.circle.dao.CircleMapper;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.gold.dao.GoldMapper;
import com.example.babacirclecommunity.gold.entity.GoldCoinChange;
import com.example.babacirclecommunity.gold.entity.UserGoldCoins;
import com.example.babacirclecommunity.weChatPay.dao.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * @author JC
 * @date 2021/10/6 11:40
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ActivityOnlineServiceImpl implements IActivityOnlineService {

    @Autowired
    private ActivityOnlineMapper activityOnlineMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private GoldMapper goldMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public List<ActivityOnlineListVo> queryActivityOnlineList(Paging paging) {
        return activityOnlineMapper.queryActivityOnlineList(getPaging(paging));
    }

    @Override
    public ActivityOnlineVo queryActivityOnlineDetailsById(int id) {
        ActivityOnlineVo activityOnlineVo = activityOnlineMapper.queryActivityOnlineDetail(id);
        if (activityOnlineVo != null) {
            //活动参与者列表（最多显示6条）
            activityOnlineVo.setActivityOnlineParticipateVos(activityOnlineMapper.queryActivityOnlineParticipateVo(id));
            //查询线上活动参与者人数
            activityOnlineVo.setParticipateNum(activityOnlineMapper.queryActivityOnlineParticipateCount(id));
            //修改线上活动浏览量
            synchronized (this){
                int i = activityOnlineMapper.updateActivityOnlineBrowse(id);
                if (i <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览异常");
                }
            }
        }

        return activityOnlineVo;
    }

    @Override
    public ResultUtil createActivityOnline(ActivityOnline activityOnline) throws ParseException {
        if (activityOnline.getInitiatorUserId() == 0) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"参数异常");
        }
        //查询是否为圈主，否则不允许创建
        int circleCount = circleMapper.myCircleCount(activityOnline.getInitiatorUserId());
        if (circleCount == 0){
            ResultUtil.error("您还不是圈主");
        }
        activityOnline.setCreateAt(String.valueOf(System.currentTimeMillis() / 1000));
        int i = activityOnlineMapper.createActivityOnline(activityOnline);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"创建线上活动失败");
        }
        return ResultUtil.success(i);
    }

    @Override
    public ResultUtil participateActivityOnline(ActivityOnlineParticipate activityOnlineParticipate) {
        if (activityOnlineParticipate.getUserId() == 0) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"用户未登录，参数错误");
        }
        //查询活动信息返回
        ActivityOnlineJoinVo activityOnlineJoinVo = activityOnlineMapper.queryActivityOnlineJoinVo(activityOnlineParticipate.getActivityOnlineId());
        //查询用户是否已参与该活动
        ActivityOnlineParticipate onlineParticipate = activityOnlineMapper.queryActivityOnlineParticipateInfo(activityOnlineParticipate.getUserId(), activityOnlineParticipate.getActivityOnlineId());
        //未参与则新增活动参与记录
        if (onlineParticipate != null){
            //查询砍价帮列表
            activityOnlineJoinVo.setActivityOnlineDiscounts(activityOnlineMapper.queryActivityOnlineDiscountVo(onlineParticipate.getId(),"limit 5"));
            //查询当前总优惠
            activityOnlineJoinVo.setTotalDiscount(activityOnlineMapper.queryTotalDiscountNow(onlineParticipate.getId()));
            //设置活动参与时间
            activityOnlineJoinVo.setParticipateTime(onlineParticipate.getCreateAt());
            //设置活动参与的id
            activityOnlineJoinVo.setParticipateId(onlineParticipate.getId());
        }
        else {
            //查询活动物品库存是否足够
            synchronized (this){
                if (activityOnlineJoinVo.getStock() <= 0) {
                    return ResultUtil.errorMsg(211,"活动库存已售完，敬请关注下次活动");
                }
                //新增活动参与记录
                String time = String.valueOf(System.currentTimeMillis() / 1000);
                activityOnlineParticipate.setCreateAt(time);
                int i = activityOnlineMapper.addActivityOnlineParticipateInfo(activityOnlineParticipate);
                if (i <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"新增参与活动记录异常");
                }
                //设置活动参与时间
                activityOnlineJoinVo.setParticipateTime(time);
                //设置活动参与的id
                activityOnlineJoinVo.setParticipateId(activityOnlineParticipate.getId());
            }
        }

        return ResultUtil.success(activityOnlineJoinVo);
    }

    @Override
    public List<ActivityOnlineDiscountVo> queryActivityOnlineDiscountVo(int onlineParticipateId, Paging paging) {
        return activityOnlineMapper.queryActivityOnlineDiscountVo(onlineParticipateId,getPaging(paging));
    }

    @Override
    public ResultUtil helpBargain(ActivityOnlineDiscount activityOnlineDiscount) {
        ActivityOnlineHelpInfoVo onlineHelpInfoVo = activityOnlineMapper.queryHelpBargainInfo(activityOnlineDiscount.getOnlineParticipateId());
        //判断活动是否已结束
        if ((Long.parseLong(onlineHelpInfoVo.getCreateAt()) + 86400) < (System.currentTimeMillis() / 1000)) {
            return ResultUtil.errorMsg(1,"该助力活动已结束");
        }
        //判断是否已助力砍价过
        ActivityOnlineDiscount onlineDiscount = activityOnlineMapper.hasItHelped(activityOnlineDiscount.getOnlineParticipateId(), activityOnlineDiscount.getUserId());
        if (onlineDiscount != null) {
            return ResultUtil.errorMsg(2,"您已助力过该活动");
        }

        activityOnlineDiscount.setCreateAt(String.valueOf(System.currentTimeMillis() / 1000));

        //当前总优惠
        BigDecimal totalDiscountNow = activityOnlineMapper.queryTotalDiscountNow(activityOnlineDiscount.getOnlineParticipateId());
        //达到底价优惠最大值
        BigDecimal maxDiscount = onlineHelpInfoVo.getOriginalPrice().subtract(onlineHelpInfoVo.getDiscountPrice());
        //查询当前总优惠是否已达到最大值
        if (totalDiscountNow.compareTo(maxDiscount) > -1) {
            return ResultUtil.errorMsg(3,"已达到底价，可以直接购买了");
        }
        //获取该活动的随机砍价范围 onlineHelpInfoVo.getSingleDiscountRate()
        //随机砍价额度
        BigDecimal rate = BigDecimal.valueOf(Math.random() * onlineHelpInfoVo.getSingleDiscountRate() + 0.1).setScale(1,BigDecimal.ROUND_DOWN);
        if (rate.add(totalDiscountNow).compareTo(maxDiscount) > -1) {
            rate = maxDiscount.subtract(totalDiscountNow);
        }
        activityOnlineDiscount.setDiscountRate(rate);
        int i = activityOnlineMapper.helpBargain(activityOnlineDiscount);
        if (i <= 0) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"助力失败");
        }
        //修改活动转发数量

        return ResultUtil.success(rate);
    }

    @Override
    public ResultUtil helpBargainInfo(int onlineParticipateId) {
        ActivityOnlineHelpInfoVo onlineHelpInfoVo = activityOnlineMapper.queryHelpBargainInfo(onlineParticipateId);
        //判断活动是否已结束
        if ((Long.parseLong(onlineHelpInfoVo.getCreateAt()) + 86400) < (System.currentTimeMillis() / 1000)) {
            return ResultUtil.error("该助力活动已结束");
        }
        //最新三条砍价记录
        List<ActivityOnlineDiscountVo> discountVos = activityOnlineMapper.queryActivityOnlineDiscountVo(onlineParticipateId, "limit 3");
        onlineHelpInfoVo.setActivityOnlineDiscounts(discountVos);
        //当前已助力总优惠
        onlineHelpInfoVo.setTotalDiscount(activityOnlineMapper.queryTotalDiscountNow(onlineParticipateId));
        return ResultUtil.success(onlineHelpInfoVo);
    }

    @Override
    public ResultUtil buyActivityItems(ActivityOnlineOrder activityOnlineOrder) {
        //判断是否已购买过该活动产品
        ActivityOnlineOrder ad = activityOnlineMapper.queryWhetherPurchased(activityOnlineOrder.getActivityOnlineId(),activityOnlineOrder.getUserId());
        if (ad != null) {
            return ResultUtil.error("您已购买过活动物品");
        }

        //查询库存是否足够
        synchronized (this) {
            int onlineStock = activityOnlineMapper.queryActivityOnlineStock(activityOnlineOrder.getActivityOnlineId());
            if (onlineStock <= 0) {
                return ResultUtil.errorMsg(211,"活动库存已售完，敬请关注下次活动");
            }

            //根据活动id查询发布活动人
            Integer userId = activityOnlineMapper.queryActivityOnlineInitiatorUserId(activityOnlineOrder.getActivityOnlineId());
            if (userId == null) {
                throw new ApplicationException(CodeType.RESOURCES_NOT_FIND, "活动发布者资源未找到");
            }
            //当前时间戳
            String time = String.valueOf(System.currentTimeMillis() / 1000);
            activityOnlineOrder.setCreateAt(time);
            //计算用户余额,根据用户id查询自己所有的金币
            UserGoldCoins userGoldCoins = goldMapper.queryUserGoldNumber(activityOnlineOrder.getUserId());
            //优先消耗不可提现金币余额,如果不可提现金币余额不足,再消耗可提现部分
            if (userGoldCoins.getMayNotWithdrawGoldCoins() < activityOnlineOrder.getPrice()) {
                int balance = activityOnlineOrder.getPrice() - userGoldCoins.getMayNotWithdrawGoldCoins();
                if (userGoldCoins.getCanWithdrawGoldCoins() < balance) {
                    return ResultUtil.success(null, "金币余额不足", 403);
                }
                //先扣除购买人余额不可提现金币
                int n = goldMapper.updateUserGold("may_not_withdraw_gold_coins = 0", activityOnlineOrder.getUserId());
                //剩余不足部分在可提现金币余额中扣除
                int c = goldMapper.updateUserGold("can_withdraw_gold_coins = can_withdraw_gold_coins - " + balance, activityOnlineOrder.getUserId());
                if (n <= 0 || c <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "修改购买人金币失败");
                }
                //被购买人的金币数量增加
                int g = goldMapper.updateUserGold("can_withdraw_gold_coins = can_withdraw_gold_coins + " + activityOnlineOrder.getPrice(), userId);
                if (g <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "修改被购买人金币失败");
                }
                //增加购买记录
                int i = activityOnlineMapper.addBuyActivityItemsRecording(activityOnlineOrder);
                if (i <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "增加购买记录失败");
                }
                //增加购买人金币支出记录
                GoldCoinChange coinChange = new GoldCoinChange();
                coinChange.setUserId(activityOnlineOrder.getUserId());
                coinChange.setSourceGoldCoin("在线活动物品购买");
                coinChange.setPositiveNegativeGoldCoins(activityOnlineOrder.getPrice());
                coinChange.setCreateAt(time);
                coinChange.setSourceGoldCoinType(4);
                coinChange.setExpenditureOrIncome(0);
                int j = orderMapper.addGoldCoinChange(coinChange);
                if (j <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "增加金币变化记录失败");
                }
                //增加发帖人金币收入记录
                GoldCoinChange coinChange2 = new GoldCoinChange();
                coinChange2.setUserId(userId);
                coinChange2.setSourceGoldCoin("在线活动物品售出收入");
                coinChange2.setPositiveNegativeGoldCoins(activityOnlineOrder.getPrice());
                coinChange2.setCreateAt(time);
                coinChange2.setSourceGoldCoinType(4);
                coinChange2.setExpenditureOrIncome(1);
                int k = orderMapper.addGoldCoinChange(coinChange2);
                if (k <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "增加金币变化记录失败");
                }

                //减少库存
                int reduceStock = activityOnlineMapper.reduceActivityOnlineStock(activityOnlineOrder.getActivityOnlineId());
                if (reduceStock <= 0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "库存减少失败");
                }
                //收入记录增加成功后给发帖人推送消息

                return ResultUtil.success(reduceStock, "成功", 200);
            }
            //先扣除购买人余额不可提现金币
            int n = goldMapper.updateUserGold("may_not_withdraw_gold_coins = may_not_withdraw_gold_coins - " + activityOnlineOrder.getPrice(), activityOnlineOrder.getUserId());
            if (n <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "修改购买人金币失败");
            }
            //被购买人的金币数量增加
            int g = goldMapper.updateUserGold("can_withdraw_gold_coins = can_withdraw_gold_coins + " + activityOnlineOrder.getPrice(), userId);
            if (g <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "修改被购买人金币失败");
            }
            //增加购买记录
            int i = activityOnlineMapper.addBuyActivityItemsRecording(activityOnlineOrder);
            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "增加购买记录失败");
            }
            //增加购买人金币支出记录
            GoldCoinChange coinChange = new GoldCoinChange();
            coinChange.setUserId(activityOnlineOrder.getUserId());
            coinChange.setSourceGoldCoin("在线活动物品购买");
            coinChange.setPositiveNegativeGoldCoins(activityOnlineOrder.getPrice());
            coinChange.setCreateAt(time);
            coinChange.setSourceGoldCoinType(4);
            coinChange.setExpenditureOrIncome(0);
            int j = orderMapper.addGoldCoinChange(coinChange);
            if (j <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "增加金币变化记录失败");
            }
            //增加发帖人金币收入记录
            GoldCoinChange coinChange2 = new GoldCoinChange();
            coinChange2.setUserId(userId);
            coinChange2.setSourceGoldCoin("在线活动物品售出收入");
            coinChange2.setPositiveNegativeGoldCoins(activityOnlineOrder.getPrice());
            coinChange2.setCreateAt(time);
            coinChange2.setSourceGoldCoinType(4);
            coinChange2.setExpenditureOrIncome(1);
            int k = orderMapper.addGoldCoinChange(coinChange2);
            if (k <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "增加金币变化记录失败");
            }

            //减少库存
            int reduceStock = activityOnlineMapper.reduceActivityOnlineStock(activityOnlineOrder.getActivityOnlineId());
            if (reduceStock <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR, "库存减少失败");
            }

            //收入记录增加成功后给发帖人推送消息

            return ResultUtil.success(reduceStock, "成功", 200);
        }
    }

    @Override
    public ResultUtil queryActivityOrders(int userId,Paging paging) {
        if (userId == 0) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"请登录后查看");
        }
        //根据用户id查询发布的所有活动及活动成交订单
        List<ActivityOnlineOrderVo> onlineOrderVos = activityOnlineMapper.queryActivityOrdersByUser(userId, getPaging(paging));
        return ResultUtil.success(onlineOrderVos);
    }

    /**
     * 分页获取
     * @param paging
     * @return
     */
    public String getPaging(Paging paging) {
        int page = (paging.getPage() - 1) * paging.getLimit();
        return "limit " + page + "," + paging.getLimit();
    }

    /**
     * 查询到期活动进行截至操作
     * 秒   分   时     日   月   周几
     * 0    *    *     *   *     0-7
     */
    @Scheduled(cron = "1 0 0 * * ?")
    public void activityDeadline(){
        //查询进行中的活动
        List<ActivityOnline> activities = activityOnlineMapper.queryNotDueActivityOnline();
        //获取当前时间戳
        long currentTime = System.currentTimeMillis() / 1000;
        for (ActivityOnline activity : activities) {
            //如果活动报名结束时间已过，停止活动
            if (currentTime > Long.parseLong(activity.getFinishTime())){
                int i = activityOnlineMapper.dueActivityOnlineById(activity.getId());
                if (i <= 0){
                    log.error("线上活动id:" + activity.getId() + ",到期线上活动截止失败,时间:" + currentTime);
                }
            }
        }
        log.info("到期线上活动进行截至操作,时间:" + currentTime);
    }
}
