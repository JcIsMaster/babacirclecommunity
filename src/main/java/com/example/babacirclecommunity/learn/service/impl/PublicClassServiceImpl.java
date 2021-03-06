package com.example.babacirclecommunity.learn.service.impl;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.ConstantUtil;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.common.utils.WxPoster;
import com.example.babacirclecommunity.gold.dao.GoldMapper;
import com.example.babacirclecommunity.gold.entity.GoldCoinChange;
import com.example.babacirclecommunity.gold.entity.UserGoldCoins;
import com.example.babacirclecommunity.learn.dao.DryGoodsCollectMapper;
import com.example.babacirclecommunity.learn.dao.PublicClassMapper;
import com.example.babacirclecommunity.learn.entity.ClassOrder;
import com.example.babacirclecommunity.learn.entity.Collect;
import com.example.babacirclecommunity.learn.service.IPublicClassService;
import com.example.babacirclecommunity.learn.vo.DryGoodsPostersVo;
import com.example.babacirclecommunity.learn.vo.PublicClassTagVo;
import com.example.babacirclecommunity.learn.vo.PublicClassVo;
import com.example.babacirclecommunity.learn.vo.QuestionTagVo;
import com.example.babacirclecommunity.personalCenter.vo.ClassPersonalVo;
import com.example.babacirclecommunity.user.dao.UserMapper;
import com.example.babacirclecommunity.user.entity.User;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import com.example.babacirclecommunity.weChatPay.dao.OrderMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<PublicClassTagVo> queryPublicClassList(Paging paging) {
        int page = (paging.getPage() - 1) * paging.getLimit();
        String sql = "limit " + page + "," + paging.getLimit();
        return publicClassMapper.queryPublicClassList(sql);
    }

    @Override
    public PublicClassVo queryPublicClassById(int id, int userId) {
        PublicClassVo publicClassVo = publicClassMapper.queryPublicClassPosters(id);
        //????????????
//        List<ClassList> cs = JSONArray.parseArray(publicClassVo.getClassList(), ClassList.class);
//        publicClassVo.setClassLists(cs);
        //??????userId???0??????????????????????????????????????????????????????
        if (userId == 0) {
            publicClassVo.setBuyerStatus(0);
            return publicClassVo;
        }
        //??????????????????????????????
        //Integer collectStatus = dryGoodsCollectMapper.whetherCollect(2, userId, publicClassVo.getId());

        //???????????????????????????
        int buyerStatus = publicClassMapper.queryBuyerStatus(id, userId);
        if (buyerStatus == 0) {
            publicClassVo.setBuyerStatus(0);
        } else {
            publicClassVo.setBuyerStatus(1);
        }

        return publicClassVo;
    }

    @Override
    public int giveCollect(int id, int userId) {
        //???????????????????????????????????????
        Collect collect = dryGoodsCollectMapper.selectCountWhether(2, userId, id);
        if (collect == null) {
            int i = dryGoodsCollectMapper.giveCollect(2, id, userId, System.currentTimeMillis() / 1000 + "");
            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = publicClassMapper.updatePublicClassCollect(id, "+");
            if (j <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return j;
        }
        int i = 0;
        int j = 0;
        //?????????????????????1 ????????????0 ????????????
        if (collect.getGiveCancel() == 1) {
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 0);
            j = publicClassMapper.updatePublicClassCollect(id, "-");
        }
        //?????????????????????0 ????????????1 ???????????????
        if (collect.getGiveCancel() == 0) {
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 1);
            j = publicClassMapper.updatePublicClassCollect(id, "+");
        }
        if (i <= 0 || j <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return j;
    }

    @Override
    public ResultUtil buyerClass(ClassOrder classOrder) {
        String time = System.currentTimeMillis() / 1000 + "";
        classOrder.setCreateAt(time);

        //????????????id???????????????
        Integer userId = publicClassMapper.queryPublicClassUserIdById(classOrder.getTId());

        //??????????????????,????????????id???????????????????????????
        UserGoldCoins userGoldCoins = goldMapper.queryUserGoldNumber(classOrder.getUId());
        //????????????????????????????????????,????????????????????????????????????,????????????????????????
        if (userGoldCoins.getMayNotWithdrawGoldCoins() < classOrder.getPrice()) {
            int balance = classOrder.getPrice() - userGoldCoins.getMayNotWithdrawGoldCoins();
            if (userGoldCoins.getCanWithdrawGoldCoins() < balance) {
                return ResultUtil.success(null, "??????????????????", 403);
            }
            //??????????????????????????????????????????
            int n = goldMapper.updateUserGold("may_not_withdraw_gold_coins = 0", classOrder.getUId());
            //???????????????????????????????????????????????????
            int c = goldMapper.updateUserGold("can_withdraw_gold_coins = can_withdraw_gold_coins - " + balance, classOrder.getUId());
            if (n <= 0 || c <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "???????????????????????????");
            }
            //?????????????????????????????????
            int g = goldMapper.updateUserGold("can_withdraw_gold_coins = can_withdraw_gold_coins + " + classOrder.getPrice(), userId);
            if (g <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "??????????????????????????????");
            }
            //??????????????????
            int i = publicClassMapper.addBuyClassRecording(classOrder);
            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "????????????????????????");
            }
            //?????????????????????????????????
            GoldCoinChange coinChange = new GoldCoinChange();
            coinChange.setUserId(classOrder.getUId());
            coinChange.setSourceGoldCoin("???????????????");
            coinChange.setPositiveNegativeGoldCoins(classOrder.getPrice());
            coinChange.setCreateAt(time);
            coinChange.setSourceGoldCoinType(3);
            coinChange.setExpenditureOrIncome(0);
            int j = orderMapper.addGoldCoinChange(coinChange);
            if (j <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "??????????????????????????????");
            }
            //?????????????????????????????????
            GoldCoinChange coinChange2 = new GoldCoinChange();
            coinChange2.setUserId(userId);
            coinChange2.setSourceGoldCoin("?????????????????????");
            coinChange2.setPositiveNegativeGoldCoins(classOrder.getPrice());
            coinChange2.setCreateAt(time);
            coinChange2.setSourceGoldCoinType(3);
            coinChange2.setExpenditureOrIncome(1);
            int k = orderMapper.addGoldCoinChange(coinChange2);
            if (k <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "??????????????????????????????");
            }
            //?????????????????????+1
            int b = publicClassMapper.updatePublicClassBuyerNum(classOrder.getTId());
            if (b <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "?????????????????????????????????");
            }
            //???????????????????????????????????????????????????

            return ResultUtil.success(b, "??????", 200);
        }
        //??????????????????????????????????????????
        int n = goldMapper.updateUserGold("may_not_withdraw_gold_coins = may_not_withdraw_gold_coins - " + classOrder.getPrice(), classOrder.getUId());
        if (n <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "???????????????????????????");
        }
        //?????????????????????????????????
        int g = goldMapper.updateUserGold("can_withdraw_gold_coins = can_withdraw_gold_coins + " + classOrder.getPrice(), userId);
        if (g <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "??????????????????????????????");
        }
        //??????????????????
        int i = publicClassMapper.addBuyClassRecording(classOrder);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "????????????????????????");
        }
        //?????????????????????????????????
        GoldCoinChange coinChange = new GoldCoinChange();
        coinChange.setUserId(classOrder.getUId());
        coinChange.setSourceGoldCoin("???????????????");
        coinChange.setPositiveNegativeGoldCoins(classOrder.getPrice());
        coinChange.setCreateAt(time);
        coinChange.setSourceGoldCoinType(3);
        coinChange.setExpenditureOrIncome(0);
        int j = orderMapper.addGoldCoinChange(coinChange);
        if (j <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "??????????????????????????????");
        }
        //?????????????????????????????????
        GoldCoinChange coinChange2 = new GoldCoinChange();
        coinChange2.setUserId(userId);
        coinChange2.setSourceGoldCoin("?????????????????????");
        coinChange2.setPositiveNegativeGoldCoins(classOrder.getPrice());
        coinChange2.setCreateAt(time);
        coinChange2.setSourceGoldCoinType(3);
        coinChange2.setExpenditureOrIncome(1);
        int k = orderMapper.addGoldCoinChange(coinChange2);
        if (k <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "??????????????????????????????");
        }
        //?????????????????????+1
        int b = publicClassMapper.updatePublicClassBuyerNum(classOrder.getTId());
        if (b <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "?????????????????????????????????");
        }
        //???????????????????????????????????????????????????

        return ResultUtil.success(b, "??????", 200);
    }

    @Override
    public ClassPersonalVo queryClassPersonal(int userId, int otherId, Paging paging) {
        Integer page = (paging.getPage() - 1) * paging.getLimit();
        String pag = "limit " + page + "," + paging.getLimit() + "";

        ClassPersonalVo classPersonalVo = new ClassPersonalVo();
        //????????????????????????
        PersonalCenterUserVo personalCenterUserVo = userMapper.queryUserById(otherId);
        classPersonalVo.setPersonalCenterUserVo(personalCenterUserVo);
        //????????????????????????
        List<PublicClassTagVo> publicClassTagVos = publicClassMapper.queryPublicClassListByUser(otherId, pag);
        classPersonalVo.setPublicClassTagVos(publicClassTagVos);

        if (userId == 0) {
            classPersonalVo.setIsMe(0);
            return classPersonalVo;
        }
        if (userId == otherId) {
            classPersonalVo.setIsMe(1);
            return classPersonalVo;
        }
        classPersonalVo.setIsMe(0);
        return classPersonalVo;
    }

    @Override
    public List<String> getPublicClass(int id, String pageUrl) {
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;

        PublicClassVo publicClassVo = publicClassMapper.queryPublicClassPosters(id);
        String time = "";

        List<String> posterList = new ArrayList<>();

        //??????token
        String token = ConstantUtil.getToken();

        try {
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token;

            Map<String, Object> param = new HashMap<>(15);
            //??????
            param.put("scene", id);
            //????????????????????????
            param.put("page", pageUrl);
            param.put("width", 430);
            param.put("auto_color", false);
            //?????????????????????
            param.put("is_hyaline", true);
            Map<String, Object> lineColor = new HashMap<>(10);
            lineColor.put("r", 0);
            lineColor.put("g", 0);
            lineColor.put("b", 0);
            param.put("line_color", lineColor);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
            // ????????????
            List<String> list = new ArrayList<String>();
            list.add("Content-Type");
            list.add("application/json");
            headers.put("header", list);

            @SuppressWarnings("unchecked")
            HttpEntity requestEntity = new HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class);

            byte[] result = entity.getBody();

            inputStream = new ByteArrayInputStream(result);

            File file = new File("e:/file/img/" + System.currentTimeMillis() + ".png");

            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
            outputStream.close();

            time = System.currentTimeMillis() / 1000 + 13 + "";


            WxPoster wxPoster = new WxPoster();
            //????????????5
            String posterUrlGreatMaster = wxPoster.getPosterUrlGreatMasterPublicClass("e:/file/img/2021515.jpg", file.getPath(), "e:/file/img/" + time + ".png", publicClassVo.getUAvatar(), publicClassVo.getCoverImg(), publicClassVo.getUName(), publicClassVo.getTitle());
            String newGreat = posterUrlGreatMaster.replace("e:/file/img/", "https://www.gofatoo.com/img/");
            /*if(newGreat!=null){
                if(circleFriendsVo.getType()==0){
                    //????????????????????????
                    int i = circleMapper.updateForwardingNumber(circleFriendsVo.getId());
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR,"????????????");
                    }
                }
            }*/
            posterList.add(newGreat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return posterList;
    }

    @Override
    public List<PublicClassTagVo> queryClassByUserId(int userId, Paging paging) {
        if (userId == 0) {
            return null;
        }
        Integer page = (paging.getPage() - 1) * paging.getLimit();
        String pag = "limit " + page + "," + paging.getLimit();
        return publicClassMapper.queryClassByUserId(userId,pag);
    }
}
