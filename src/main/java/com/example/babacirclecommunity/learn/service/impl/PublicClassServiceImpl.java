package com.example.babacirclecommunity.learn.service.impl;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.gold.dao.GoldMapper;
import com.example.babacirclecommunity.gold.entity.GoldCoinChange;
import com.example.babacirclecommunity.gold.entity.UserGoldCoins;
import com.example.babacirclecommunity.learn.dao.DryGoodsCollectMapper;
import com.example.babacirclecommunity.learn.dao.PublicClassMapper;
import com.example.babacirclecommunity.learn.entity.ClassOrder;
import com.example.babacirclecommunity.learn.entity.Collect;
import com.example.babacirclecommunity.learn.service.IPublicClassService;
import com.example.babacirclecommunity.learn.vo.PublicClassTagVo;
import com.example.babacirclecommunity.learn.vo.PublicClassVo;
import com.example.babacirclecommunity.personalCenter.vo.ClassPersonalVo;
import com.example.babacirclecommunity.user.dao.UserMapper;
import com.example.babacirclecommunity.user.entity.User;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import com.example.babacirclecommunity.weChatPay.dao.OrderMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/6 9:47
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PublicClassServiceImpl implements IPublicClassService {

    @Autowired
    private PublicClassMapper publicClassMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DryGoodsCollectMapper dryGoodsCollectMapper;

    @Autowired
    private GoldMapper goldMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public PublicClassVo queryPublicClassById(int id, int userId) {
        PublicClassVo publicClassVo = publicClassMapper.queryPublicClassById(id);
        //获取发帖人名称,头像,介绍
        User user = userMapper.selectUserById(publicClassVo.getUId());
        if(user.getUserName()==null || user.getAvatar()==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        publicClassVo.setUName(user.getUserName());
        publicClassVo.setUAvatar(user.getAvatar());
        publicClassVo.setIntroduce(user.getIntroduce());
        //课程列表
//        List<ClassList> cs = JSONArray.parseArray(publicClassVo.getClassList(), ClassList.class);
//        publicClassVo.setClassLists(cs);
        //如果userId为0，用户处于未登录状态，状态设为未收藏
        if (userId == 0){
            publicClassVo.setWhetherCollect(0);
            publicClassVo.setBuyerStatus(0);
            return publicClassVo;
        }
        //我是否对该帖子收过藏
        Integer collectStatus = dryGoodsCollectMapper.whetherCollect(2,userId, publicClassVo.getId());
        if (collectStatus == 0) {
            publicClassVo.setWhetherCollect(0);
        } else {
            publicClassVo.setWhetherCollect(1);
        }
        //我是否购买过该课程
        int buyerStatus = publicClassMapper.queryBuyerStatus(id,userId);
        if(buyerStatus == 0){
            publicClassVo.setBuyerStatus(0);
        } else {
            publicClassVo.setBuyerStatus(1);
        }

        return publicClassVo;
    }

    @Override
    public int giveCollect(int id, int userId) {
        //查询数据库是否存在该条数据
        Collect collect = dryGoodsCollectMapper.selectCountWhether(2,userId,id);
        if(collect == null){
            int i = dryGoodsCollectMapper.giveCollect(2,id, userId, System.currentTimeMillis() / 1000 + "");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = publicClassMapper.updatePublicClassCollect(id,"+");
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
            j = publicClassMapper.updatePublicClassCollect(id,"-");
        }
        //如果当前状态是0 那就改为1 为收藏状态
        if(collect.getGiveCancel()==0){
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 1);
            j = publicClassMapper.updatePublicClassCollect(id,"+");
        }
        if(i<=0 || j<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return j;
    }

    @Override
    public ResultUtil buyerClass(ClassOrder classOrder) {
        String time = System.currentTimeMillis() / 1000 + "";
        classOrder.setCreateAt(time);

        //根据帖子id查询发帖人
        Integer userId = publicClassMapper.queryPublicClassUserIdById(classOrder.getTId());

        //计算用户余额,根据用户id查询自己所有的金币
        UserGoldCoins userGoldCoins = goldMapper.queryUserGoldNumber(classOrder.getUId());
        //优先消耗不可提现金币余额,如果不可提现金币余额不足,再消耗可提现部分
        if(userGoldCoins.getMayNotWithdrawGoldCoins() < classOrder.getPrice()){
            int balance = classOrder.getPrice() - userGoldCoins.getMayNotWithdrawGoldCoins();
            if (userGoldCoins.getCanWithdrawGoldCoins() < balance){
                return ResultUtil.success(null,"金币余额不足",403);
            }
            //先扣除购买人余额不可提现金币
            int n = goldMapper.updateUserGold("may_not_withdraw_gold_coins = 0", classOrder.getUId());
            //剩余不足部分在可提现金币余额中扣除
            int c = goldMapper.updateUserGold("can_withdraw_gold_coins = can_withdraw_gold_coins - " + balance, classOrder.getUId());
            if(n <= 0 || c <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"修改购买人金币失败");
            }
            //被购买人的金币数量增加
            int g = goldMapper.updateUserGold("can_withdraw_gold_coins = can_withdraw_gold_coins + " + classOrder.getPrice(),userId);
            if(g <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"修改被购买人金币失败");
            }
            //增加购买记录
            int i = publicClassMapper.addBuyClassRecording(classOrder);
            if (i <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"增加购买记录失败");
            }
            //增加购买人金币支出记录
            GoldCoinChange coinChange = new GoldCoinChange();
            coinChange.setUserId(classOrder.getUId());
            coinChange.setSourceGoldCoin("公开课购买");
            coinChange.setPositiveNegativeGoldCoins("-" + classOrder.getPrice());
            coinChange.setCreateAt(time);
            int j = orderMapper.addGoldCoinChange(coinChange);
            if (j <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"增加金币变化记录失败");
            }
            //增加发帖人金币收入记录
            GoldCoinChange coinChange2 = new GoldCoinChange();
            coinChange2.setUserId(userId);
            coinChange2.setSourceGoldCoin("公开课售出收入");
            coinChange2.setPositiveNegativeGoldCoins("+" + classOrder.getPrice());
            coinChange2.setCreateAt(time);
            int k = orderMapper.addGoldCoinChange(coinChange2);
            if (k <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"增加金币变化记录失败");
            }
            //公开课购买人数+1
            int b = publicClassMapper.updatePublicClassBuyerNum(classOrder.getTId());
            if (b <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"增加公开课购买人数失败");
            }
            //收入记录增加成功后给发帖人推送消息

            return ResultUtil.success(b,"成功",200);
        }
        //先扣除购买人余额不可提现金币
        int n = goldMapper.updateUserGold("may_not_withdraw_gold_coins = may_not_withdraw_gold_coins - " + classOrder.getPrice(), classOrder.getUId());
        if(n <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改购买人金币失败");
        }
        //被购买人的金币数量增加
        int g = goldMapper.updateUserGold("can_withdraw_gold_coins = can_withdraw_gold_coins + " + classOrder.getPrice(),userId);
        if(g <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改被购买人金币失败");
        }
        //增加购买记录
        int i = publicClassMapper.addBuyClassRecording(classOrder);
        if (i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加购买记录失败");
        }
        //增加购买人金币支出记录
        GoldCoinChange coinChange = new GoldCoinChange();
        coinChange.setUserId(classOrder.getUId());
        coinChange.setSourceGoldCoin("公开课购买");
        coinChange.setPositiveNegativeGoldCoins("-" + classOrder.getPrice());
        coinChange.setCreateAt(time);
        int j = orderMapper.addGoldCoinChange(coinChange);
        if (j <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加金币变化记录失败");
        }
        //增加发帖人金币收入记录
        GoldCoinChange coinChange2 = new GoldCoinChange();
        coinChange2.setUserId(userId);
        coinChange2.setSourceGoldCoin("公开课售出收入");
        coinChange2.setPositiveNegativeGoldCoins("+" + classOrder.getPrice());
        coinChange2.setCreateAt(time);
        int k = orderMapper.addGoldCoinChange(coinChange2);
        if (k <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加金币变化记录失败");
        }
        //公开课购买人数+1
        int b = publicClassMapper.updatePublicClassBuyerNum(classOrder.getTId());
        if (b <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"增加公开课购买人数失败");
        }
        //收入记录增加成功后给发帖人推送消息

        return ResultUtil.success(b,"成功",200);
    }

    @Override
    public ClassPersonalVo queryClassPersonal(int userId, int otherId, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pag="limit "+page+","+paging.getLimit()+"";

        ClassPersonalVo classPersonalVo = new ClassPersonalVo();
        //查询用户基本信息
        PersonalCenterUserVo personalCenterUserVo = userMapper.queryUserById(otherId);
        classPersonalVo.setPersonalCenterUserVo(personalCenterUserVo);
        //查询用户发的干货帖子
        List<PublicClassTagVo> publicClassTagVos = publicClassMapper.queryPublicClassListByUser(otherId, pag);
        classPersonalVo.setPublicClassTagVos(publicClassTagVos);

        if (userId == 0){
            classPersonalVo.setIsMe(0);
            return classPersonalVo;
        }
        if (userId == otherId){
            classPersonalVo.setIsMe(1);
            return classPersonalVo;
        }
        classPersonalVo.setIsMe(0);
        return classPersonalVo;
    }
}
