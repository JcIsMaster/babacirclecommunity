package com.example.babacirclecommunity.weChatPay.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/3/18 16:50
 */
public interface IOrdersService {

    /**
     * 微信支付 （公众号支付(JSAPI)）
     * @param openId 微信openid
     * @param request
     * @param price 金额
     * @param body 商家名称-销售商品类目
     * @param userId 用户id
     * @return
     * @throws Exception
     */
    Map<String,Object> orders(String openId, HttpServletRequest request,BigDecimal price, String body,int userId,int goodsNo) throws Exception;

    /**
     * 支付成功回调
     * @param request
     * @param response
     * @throws Exception
     */
    void weChatNotify(HttpServletRequest request, HttpServletResponse response)throws Exception;

    /**
     * 扣除金币
     * @param openId
     * @param money
     */
    void deductGoldByOpenId(String openId,Integer money);
}
