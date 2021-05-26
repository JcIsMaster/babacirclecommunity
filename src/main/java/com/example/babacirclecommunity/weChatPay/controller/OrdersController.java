package com.example.babacirclecommunity.weChatPay.controller;


import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.CertHttpUtil;
import com.example.babacirclecommunity.common.utils.ConstantUtil;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.weChatPay.service.IOrdersService;
import com.github.wxpay.sdk.WXPayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author MQ
 * @date 2021/3/18 16:40
 */
@Api(tags = "支付API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/OrdersController")
public class OrdersController {

    @Autowired
    private IOrdersService iOrdersService;

    /**
     * 微信支付 （公众号支付(JSAPI)）
     * @return
     */
    @ApiOperation(value = "微信浏览器内微信支付/公众号支付(JSAPI)",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/orders")
    public Map<String,Object> orders(String openid,HttpServletRequest request,BigDecimal price,String body,int userId) throws Exception {
        if("undefined".equals(openid)){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        return iOrdersService.orders(openid,request,price,body,userId);
    }

    /**
     * 微信支付成功回调
     * @return
     */
    @ApiOperation(value = "微信支付成功回调",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/weChatNotify")
    public void weChatNotify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        iOrdersService.weChatNotify(request,response);
    }


    @ApiOperation(value = "企业转账到零钱", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(value = "*用户token", name = "token",defaultValue ="", dataType = "String",paramType="header"),
            @ApiImplicitParam(value = "金额", name = "money",defaultValue ="", dataType = "String",paramType="query")
    })
    @PostMapping("/transfer")
    public ResultUtil transfer(HttpServletRequest request, BigDecimal money, String openID) {
        if(openID.equals("null")){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"openId等于空");
        }

        // 拼接统一下单地址参数
        Map<String, String> paraMap = new HashMap<String, String>(15);

        // 支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败
        String payment =""+((money.multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_UP).intValue()));

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

        // 1.0 拼凑企业支付需要的参数
        // APP对应的微信的appid
        String appid = ConstantUtil.appid;

        // 商户号
        String mchId = ConstantUtil.MCHID;

        // 生成随机数
        String nonceStr = WXPayUtil.generateNonceStr();

        // 生成商户订单号
        String partnerTradeNo = WXPayUtil.generateNonceStr();

        // 收款用户openid
        String openId = openID;

        // 是否验证真实姓名呢
        String checkName = "NO_CHECK";

        // 收款用户姓名(非必须)
        String reUserName = "KOLO";

        // 企业付款金额，最少为100，单位为分
        String amount = payment;

        // 企业付款操作说明信息。必填。
        String desc = "恭喜你，完成了一个订单！";

        // 用户的ip地址
        String spbillCreateIp = ip;

        // 2.0 生成map集合
        SortedMap<String,String> packageParams = new TreeMap<String, String>();

        // 微信公众号的appid
        packageParams.put("mch_appid", appid);

        // 商务号
        packageParams.put("mchid", mchId);

        // 随机生成后数字，保证安全性
        packageParams.put("nonce_str", nonceStr);

        // 生成商户订单号
        packageParams.put("partner_trade_no", partnerTradeNo);

        // 支付给用户openid
        packageParams.put("openid", openId);

        // 是否验证真实姓名呢
        packageParams.put("check_name", checkName);

        // 收款用户姓名
        packageParams.put("re_user_name", reUserName);

        // 企业付款金额，单位为分
        packageParams.put("amount", amount);

        // 企业付款操作说明信息。必填。
        packageParams.put("desc", desc);

        // 调用接口的机器Ip地址
        packageParams.put("spbill_create_ip", spbillCreateIp);

        try {
            // 3.0 利用上面的参数，先去生成自己的签名 paternerkey
            String sign = WXPayUtil.generateSignature(packageParams, ConstantUtil.PATERNERKEY);

            // 4.0 将签名再放回map中，它也是一个参数
            packageParams.put("sign", sign);

            // 5.0将当前的map结合转化成xml格式
            String xml = WXPayUtil.mapToXml(packageParams);

            // 6.0获取需要发送的url地址
            // 获取退款的api接口
            String wxUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";


            System.out.println("发送前的xml为：" + xml);

            // 7,向微信发送请求转账请求
            String returnXml = CertHttpUtil.postData(wxUrl, xml, ConstantUtil.MCHID, "e:\\file\\apiclient_cert.p12");

            System.out.println("返回的returnXml为:" + returnXml);

            // 8，将微信返回的xml结果转成map格式
            Map<String, String> returnMap = WXPayUtil.xmlToMap(returnXml);

            if (returnMap.get("result_code").equals("SUCCESS")) {
                // 付款成功
                System.out.println("returnMap为:" + returnMap);
            }else {
                return ResultUtil.errorMsg(902,returnMap.get("err_code_des"));
            }
            return ResultUtil.success(returnMap.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return ResultUtil.errorMsg(901,"微信转账失败！");
        }
    }


}
