package com.example.babacirclecommunity.weChatPay.service.impl;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.ConstantUtil;
import com.example.babacirclecommunity.common.utils.IdGenerator;
import com.example.babacirclecommunity.gold.dao.GoldMapper;
import com.example.babacirclecommunity.gold.entity.GoldCoinChange;

import com.example.babacirclecommunity.user.dao.UserMapper;
import com.example.babacirclecommunity.weChatPay.dao.OrderMapper;
import com.example.babacirclecommunity.weChatPay.entity.GoldCoinOrders;
import com.example.babacirclecommunity.weChatPay.service.IOrdersService;
import com.example.babacirclecommunity.weChatPay.util.PayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/3/18 16:50
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrdersServiceImpl implements IOrdersService {


    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private GoldMapper goldMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public Map<String, Object> orders(String openid, HttpServletRequest request, BigDecimal price, String body, int userId) throws Exception {
        System.out.println("=================" + openid);
        // 支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败
        String payment = "" + ((price.multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_UP).intValue()));
        System.out.println(payment);

        // 拼接统一下单地址参数
        Map<String, String> paraMap = new HashMap<String, String>();
        // 获取请求ip地址
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.indexOf(",") != -1) {
            String[] ips = ip.split(",");
            ip = ips[0].trim();
        }


        String nonceStr = idGenerator.getUuid();

        //获取订单号
        String orderId = idGenerator.getOrderCode();

        // 商家平台ID
        paraMap.put("appid", ConstantUtil.appid);

        // 商家名称-销售商品类目、String(128)
        paraMap.put("body", body);

        // 商户ID
        paraMap.put("mch_id", ConstantUtil.MCHID);

        // UUID
        paraMap.put("nonce_str", nonceStr);

        //openid
        paraMap.put("openid", openid);

        // 订单号,每次都不同
        paraMap.put("out_trade_no", orderId);

        //请求ip地址
        paraMap.put("spbill_create_ip", ip);

        // 支付金额，单位分
        paraMap.put("total_fee", payment);

        // 支付类型
        paraMap.put("trade_type", ConstantUtil.TRADETYPE);

        // 此路径是微信服务器调用支付结果通知路径随意写
        paraMap.put("notify_url", ConstantUtil.notifyUrl);

        // 除去数组中的空值和签名参数
        paraMap = PayUtil.paraFilter(paraMap);

        // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String prestr = PayUtil.createLinkString(paraMap);

        // MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
        String mysign = PayUtil.sign(prestr, ConstantUtil.PATERNERKEY, "utf-8").toUpperCase();
        System.out.println("第一次签名+=" + mysign);

        // 拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
        String xml = "<xml>" + "<appid>" + ConstantUtil.appid + "</appid>" + "<body><![CDATA[" + body + "]]></body>"
                + "<mch_id>" + ConstantUtil.MCHID + "</mch_id>" + "<nonce_str>" + nonceStr + "</nonce_str>"
                + "<notify_url>" + ConstantUtil.notifyUrl + "</notify_url>" + "<openid>" + openid + "</openid>"
                + "<out_trade_no>" + orderId + "</out_trade_no>" + "<spbill_create_ip>" + ip
                + "</spbill_create_ip>" + "<total_fee>" + payment + "</total_fee>" + "<trade_type>"
                + ConstantUtil.TRADETYPE + "</trade_type>" + "<sign>" + mysign + "</sign>" + "</xml>";


        // 调用统一下单接口，并接受返回的结果
        String result = PayUtil.httpRequest(ConstantUtil.payUrl, "POST", xml);
        System.out.println("======" + result);


        // 将解析结果存储在HashMap中
        Map map = PayUtil.doXMLParse(result);

        // 返回状态码
        String returnCode = (String) map.get("return_code");

        // 返回给移动端需要的参数
        Map<String, Object> response = new HashMap<String, Object>();
        if (returnCode == "SUCCESS" || returnCode.equals(returnCode)) {
            // 业务结果
            // 返回的预支付id
            String prepayId = (String) map.get("prepay_id");

            response.put("nonceStr", nonceStr);
            response.put("package", "prepay_id=" + prepayId);

            String timeStamp = System.currentTimeMillis() / 1000 + "";

            // 这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
            response.put("timeStamp", timeStamp);

            String stringSignTemp = "appId=" + ConstantUtil.appid + "&nonceStr=" + nonceStr + "&package=prepay_id="
                    + prepayId + "&signType=" + ConstantUtil.SIGNTYPE + "&timeStamp=" + timeStamp;

            //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
            String paySign = PayUtil.sign(stringSignTemp, ConstantUtil.PATERNERKEY, "utf-8").toUpperCase();
            log.info("=======================第二次签名：" + paySign + "=====================");

            response.put("paySign", paySign);
            //添加订单
            GoldCoinOrders goldCoinOrders = new GoldCoinOrders();
            goldCoinOrders.setCreateAt(System.currentTimeMillis() / 1000 + "");
            goldCoinOrders.setOrderNumber(paraMap.get("out_trade_no"));
            goldCoinOrders.setGoodsNo(0);
            goldCoinOrders.setOrderStatus(0);
            //分转元
            String s = fenToYuan(payment);
            BigDecimal bd = new BigDecimal(s);
            goldCoinOrders.setMoney(bd);
            goldCoinOrders.setUserId(userId);
            int i = orderMapper.addOrder(goldCoinOrders);
            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "订单生成失败");
            }


            //amqpTemplate.convertAndSend("ex.order","order",paraMap.get("out_trade_no"));
        //业务逻辑代码
        }

        return response;
    }


    @Override
    public void weChatNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        // sb为微信返回的xml
        String notityXml = sb.toString();

        //将解析结果存储在HashMap中
        Map map = PayUtil.doXMLParse(notityXml);

        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确
            //回调验签时需要去除sign和空值参数
            Map<String, String> validParams = PayUtil.paraFilter(map);

            //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
            String validStr = PayUtil.createLinkString(validParams);

            //拼装生成服务器端验证的签名
            String sign = PayUtil.sign(validStr, ConstantUtil.PATERNERKEY, "utf-8").toUpperCase();

            // 验证签名是否正确
            if (sign.equals(map.get("sign"))) {
                /** 此处添加自己的业务逻辑代码start **/

                //微信支付交易号
                //String transaction_id = map.get("transaction_id").toString();

                //订单号
                String outTradeNo = map.get("out_trade_no").toString();
                String totalFee = map.get("total_fee").toString();

                //分转元
                String s = fenToYuan(totalFee);

                int integer = Integer.parseInt(s);

                //得到算过后得到的金币数量
                int gold = integer * 10;

                //修改订单状态为 已支付
                int i = orderMapper.updateOrderStatus(1, outTradeNo);
                if (i <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "订单修改失败");
                }
                //得到openid
                String openid = map.get("openid").toString();

                //得到用户id
                Integer userId = userMapper.queryUserIdByOpenId(openid);
                if ("null".equals(userId) || "0".equals(userId) || "".equals(userId)) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }

                //修改不可提现金币数量
                int i2 = goldMapper.updateUserGold("may_not_withdraw_gold_coins=may_not_withdraw_gold_coins+" + gold, userId);
                if (i2 <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "修改金币失败");
                }

                //添加金币变化数据
                GoldCoinChange goldCoinChange = new GoldCoinChange();
                goldCoinChange.setCreateAt(System.currentTimeMillis() / 1000 + "");
                goldCoinChange.setUserId(userId);
                goldCoinChange.setSourceGoldCoin("充值");
                goldCoinChange.setPositiveNegativeGoldCoins(gold);
                goldCoinChange.setSourceGoldCoinType(0);
                goldCoinChange.setExpenditureOrIncome(1);
                int i1 = orderMapper.addGoldCoinChange(goldCoinChange);
                if (i1 <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "金币充值失败");
                }

                /** 此处添加自己的业务逻辑代码end **/
                // 通知微信服务器已经支付成功
                response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
                response.getWriter().flush();
                response.getWriter().close();
            } else {
                response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");
                response.getWriter().flush();
                response.getWriter().close();
            }
        } else {
            response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");
            response.getWriter().flush();
            response.getWriter().close();
        }
    }


    /**
     * 分转元
     *
     * @param amount 分
     * @return
     */
    private String fenToYuan(String amount) {
        NumberFormat format = NumberFormat.getInstance();
        try {
            Number number = format.parse(amount);
            double temp = number.doubleValue() / 100.0;
            format.setGroupingUsed(false);
            // 设置返回的小数部分所允许的最大位数
            format.setMaximumFractionDigits(2);
            amount = format.format(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }


}
