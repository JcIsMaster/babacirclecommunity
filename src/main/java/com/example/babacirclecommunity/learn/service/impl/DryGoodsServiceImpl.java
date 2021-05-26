package com.example.babacirclecommunity.learn.service.impl;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
//import com.example.babacirclecommunity.gold.dao.GoldMapper;
//import com.example.babacirclecommunity.gold.entity.UserGoldCoins;
import com.example.babacirclecommunity.learn.dao.*;
import com.example.babacirclecommunity.learn.entity.Collect;
import com.example.babacirclecommunity.learn.entity.DryGoods;
import com.example.babacirclecommunity.learn.entity.Give;
import com.example.babacirclecommunity.learn.entity.LearnPostExceptional;
import com.example.babacirclecommunity.learn.service.IDryGoodsService;
import com.example.babacirclecommunity.learn.vo.DryGoodsTagVo;
import com.example.babacirclecommunity.learn.vo.DryGoodsVo;
import com.example.babacirclecommunity.learn.vo.PublicClassTagVo;
import com.example.babacirclecommunity.learn.vo.QuestionVo;
import com.example.babacirclecommunity.user.dao.UserMapper;
//import com.example.babacirclecommunity.weChatPay.dao.OrderMapper;
//import com.example.babacirclecommunity.weChatPay.entity.GoldCoinChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/16 15:59
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class DryGoodsServiceImpl implements IDryGoodsService {

    @Autowired
    private DryGoodsMapper dryGoodsMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private DryGoodsGiveMapper dryGoodsGiveMapper;

    @Autowired
    private DryGoodsCollectMapper dryGoodsCollectMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PublicClassMapper publicClassMapper;

//    @Autowired
//    private OrderMapper orderMapper;
//
//    @Autowired
//    private GoldMapper goldMapper;

    @Override
    public Object queryLearnList(int type, Paging paging, int orderRule) {

        Integer page=(paging.getPage()-1)*paging.getLimit();

        //提问
        if(type == 0){
            String sql2 = "";
            if (orderRule == 0){
                sql2 = "order by a.collect DESC ";
            }
            if (orderRule == 1){
                sql2 = "order by a.create_at DESC ";
            }
            if (orderRule == 2){
                sql2 = "order by a.favour DESC ";
            }
            sql2 = sql2 + "limit "+page+","+paging.getLimit()+"";
            List<QuestionVo> questionVos = questionMapper.queryQuestionList(sql2);
            return questionVos;
        }
        //干货
        if(type == 1){
            String sql = "";
            if (orderRule == 0){
                sql = "order by collect DESC ";
            }
            if (orderRule == 1){
                sql = "order by create_at DESC ";
            }
            if (orderRule == 2){
                sql = "order by favour DESC ";
            }
            sql = sql + "limit "+page+","+paging.getLimit()+"";
            List<DryGoodsVo> dryGoods = dryGoodsMapper.queryDryGoodsList(sql);
            return dryGoods;
        }
        //公开课
        if(type == 2){
            String sql3 = "";
            if (orderRule == 0){
                sql3 = "order by a.collect DESC ";
            }
            if (orderRule == 1){
                sql3 = "order by a.create_at DESC ";
            }
            if (orderRule == 2){
                sql3 = "order by a.buyer_num DESC ";
            }
            sql3 = sql3 + "limit "+page+","+paging.getLimit()+"";
            List<PublicClassTagVo> publicClassTagVos = publicClassMapper.queryPublicClassList(sql3);
            return publicClassTagVos;
        }
        return null;
    }

    @Override
    public DryGoodsTagVo queryDryGoodsById(int id,int userId) {
        DryGoodsTagVo goodsTagVo = dryGoodsMapper.queryDryGoodsById(id);
        //统计点赞数量(暂未写入tb_dry_goods表favour字段)
        //Integer giveCount = dryGoodsGiveMapper.selectGiveNumber(1,id);
        //goodsTagVo.setFavour(giveCount);
        //统计收藏数量(暂未写入tb_dry_goods表collect字段)
        //Integer collectCount = dryGoodsCollectMapper.selectCollectNumber(1,id);
        //goodsTagVo.setCollect(collectCount);
        //获取发帖人名称
        String uName = userMapper.selectUserById(goodsTagVo.getUId()).getUserName();
        if(uName==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        goodsTagVo.setUName(uName);
        //如果userId为0，用户处于未登录状态，状态设为未点赞
        if (userId == 0){
            goodsTagVo.setWhetherGive(0);
            goodsTagVo.setWhetherCollect(0);
            return goodsTagVo;
        }
        //我是否对该帖子点过赞
        Integer giveStatus = dryGoodsGiveMapper.whetherGive(1,userId, goodsTagVo.getId());
        if (giveStatus == 0) {
            goodsTagVo.setWhetherGive(0);
        } else {
            goodsTagVo.setWhetherGive(1);
        }
        //我是否对该帖子收过藏
        Integer collectStatus = dryGoodsCollectMapper.whetherCollect(1,userId, goodsTagVo.getId());
        if (collectStatus == 0) {
            goodsTagVo.setWhetherCollect(0);
        } else {
            goodsTagVo.setWhetherCollect(1);
        }
        return goodsTagVo;
    }

    @Override
    public int addDryGoods(DryGoods dryGoods) {
        dryGoods.setCreateAt(System.currentTimeMillis() / 1000 + "");
        return dryGoodsMapper.addDryGoods(dryGoods);
    }

    @Override
    public int giveLike(int id, int userId) {
        //查询数据库是否存在该条数据
        Give give = dryGoodsGiveMapper.selectCountWhether(1,userId,id);
        if(give == null){
            int i = dryGoodsGiveMapper.giveLike(1,id, userId, System.currentTimeMillis() / 1000 + "");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = dryGoodsMapper.updateDryGoodsGive(id,"+");
            if(j<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return j;
        }
        int i = 0;
        int j = 0;
        //如果当前状态是1 那就改为0 取消点赞
        if(give.getGiveCancel()==1){
            i = dryGoodsGiveMapper.updateGiveStatus(give.getId(), 0);
            j = dryGoodsMapper.updateDryGoodsGive(id,"-");
        }

        //如果当前状态是0 那就改为1 为点赞状态
        if(give.getGiveCancel()==0){
            i = dryGoodsGiveMapper.updateGiveStatus(give.getId(), 1);
            j = dryGoodsMapper.updateDryGoodsGive(id,"+");
        }

        if(i<=0 || j<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        return j;
    }

    @Override
    public int giveCollect(int id, int userId) {
        //查询数据库是否存在该条数据
        Collect collect = dryGoodsCollectMapper.selectCountWhether(1,userId,id);
        if(collect == null){
            int i = dryGoodsCollectMapper.giveCollect(1,id, userId, System.currentTimeMillis() / 1000 + "");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = dryGoodsMapper.updateDryGoodsCollect(id,"+");
            if(j<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return j;
        }
        int i = 0;
        int j = 0;
        //如果当前状态是1 那就改为0 取消收藏
        if(collect.getGiveCancel()==1){
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 0);
            j = dryGoodsMapper.updateDryGoodsCollect(id,"-");
        }

        //如果当前状态是0 那就改为1 为收藏状态
        if(collect.getGiveCancel()==0){
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 1);
            j = dryGoodsMapper.updateDryGoodsCollect(id,"+");
        }

        if(i<=0 || j<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        return j;
    }

    /**
    @Override
    public ResultUtil rewardGoldToDryGoods(LearnPostExceptional learnPostExceptional) {
        String time = System.currentTimeMillis() / 1000 + "";
        learnPostExceptional.setCreateAt(time);

        //根据帖子id查询发帖人
        Integer userId = dryGoodsMapper.queryDryGoodsUserIdById(learnPostExceptional.getTId());

        //计算用户余额,根据用户id查询自己所有的金币
        UserGoldCoins userGoldCoins = goldMapper.queryUserGoldNumber(learnPostExceptional.getRewarderId());
        //优先消耗不可提现金币余额,如果不可提现金币余额不足,再消耗可提现部分
        if(userGoldCoins.getMayNotWithdrawGoldCoins() < learnPostExceptional.getGoldNum()){
            int balance = learnPostExceptional.getGoldNum() - userGoldCoins.getMayNotWithdrawGoldCoins();
            if (userGoldCoins.getCanWithdrawGoldCoins() < balance){
                return ResultUtil.success(null,"金币余额不足",403);
            }
            //先扣除赞赏人余额不可提现金币
            int n = goldMapper.updateUserGold("may_not_withdraw_gold_coins = 0", learnPostExceptional.getRewarderId());
            //剩余不足部分在可提现金币余额中扣除
            int c = goldMapper.updateUserGold("can_withdraw_gold_coins = can_withdraw_gold_coins - " + balance, learnPostExceptional.getRewarderId());
            if(n <= 0 || c <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"修改打赏人金币失败");
            }
            //被打赏人的金币数量增加
            int g = goldMapper.updateUserGold("can_withdraw_gold_coins = can_withdraw_gold_coins + " + learnPostExceptional.getGoldNum(),userId);
            if(g <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"修改被打赏人金币失败");
            }
            //增加打赏记录
            int i = dryGoodsMapper.addRewardGoldRecording(learnPostExceptional);
            if (i <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"增加打赏记录失败");
            }
            //增加打赏人金币支出记录
            GoldCoinChange coinChange = new GoldCoinChange();
            coinChange.setUserId(learnPostExceptional.getRewarderId());
            coinChange.setSourceGoldCoin("干货帖打赏");
            coinChange.setPositiveNegativeGoldCoins("-"+learnPostExceptional.getGoldNum());
            coinChange.setCreateAt(time);
            int j = orderMapper.addGoldCoinChange(coinChange);
            if (j <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"增加金币变化记录失败");
            }
            //增加发帖人金币收入记录
            GoldCoinChange coinChange2 = new GoldCoinChange();
            coinChange2.setUserId(userId);
            coinChange2.setSourceGoldCoin("干货帖打赏收入");
            coinChange2.setPositiveNegativeGoldCoins("+"+learnPostExceptional.getGoldNum());
            coinChange2.setCreateAt(time);
            int k = orderMapper.addGoldCoinChange(coinChange2);
            if (k <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"增加金币变化记录失败");
            }
            //收入记录增加成功后给发帖人推送消息

            return ResultUtil.success(k,"成功",200);
        }
        //先扣除赞赏人余额不可提现金币
        int n = goldMapper.updateUserGold("may_not_withdraw_gold_coins = may_not_withdraw_gold_coins - " + learnPostExceptional.getGoldNum(), learnPostExceptional.getRewarderId());
        if(n <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改打赏人金币失败");
        }
        //被打赏人的金币数量增加
        int g = goldMapper.updateUserGold("can_withdraw_gold_coins = can_withdraw_gold_coins + " + learnPostExceptional.getGoldNum(),userId);
        if(g <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改被打赏人金币失败");
        }
        //增加打赏记录
        int i = dryGoodsMapper.addRewardGoldRecording(learnPostExceptional);
        if (i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加打赏记录失败");
        }
        //增加打赏人金币支出记录
        GoldCoinChange coinChange = new GoldCoinChange();
        coinChange.setUserId(learnPostExceptional.getRewarderId());
        coinChange.setSourceGoldCoin("干货帖打赏");
        coinChange.setPositiveNegativeGoldCoins("-"+learnPostExceptional.getGoldNum());
        coinChange.setCreateAt(time);
        int j = orderMapper.addGoldCoinChange(coinChange);
        if (j <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加金币变化记录失败");
        }
        //增加发帖人金币收入记录
        GoldCoinChange coinChange2 = new GoldCoinChange();
        coinChange2.setUserId(userId);
        coinChange2.setSourceGoldCoin("干货帖打赏收入");
        coinChange2.setPositiveNegativeGoldCoins("+"+learnPostExceptional.getGoldNum());
        coinChange2.setCreateAt(time);
        int k = orderMapper.addGoldCoinChange(coinChange2);
        if (k <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加金币变化记录失败");
        }
        //收入记录增加成功后给发帖人推送消息

        return ResultUtil.success(k,"成功",200);
    }
    */
}