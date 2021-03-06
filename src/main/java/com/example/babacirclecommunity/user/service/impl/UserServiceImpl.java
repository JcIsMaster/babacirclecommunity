package com.example.babacirclecommunity.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.ConstantUtil;
import com.example.babacirclecommunity.gold.dao.GoldMapper;
import com.example.babacirclecommunity.honored.dao.HonoredMapper;
import com.example.babacirclecommunity.honored.entity.Honored;
import com.example.babacirclecommunity.my.dao.MyMapper;
import com.example.babacirclecommunity.user.dao.UserMapper;
import com.example.babacirclecommunity.user.entity.User;
import com.example.babacirclecommunity.user.service.IUserService;
import com.example.babacirclecommunity.user.vo.UserLoginVo;
import com.example.babacirclecommunity.user.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/5/21 13:25
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GoldMapper goldMapper;

    @Autowired
    private MyMapper myMapper;

    @Autowired
    private HonoredMapper honoredMapper;

    @Override
    public User wxLogin(String code, String userName, String avatar, String address, int sex) {
        //微信登录的code值
        String wxCode = code;

        //服务器端调用接口的url
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        //封装需要的参数信息
        Map<String,String> requestUrlParam = new HashMap<String,String>(16);
        //开发者设置中的appId
        requestUrlParam.put("appid", ConstantUtil.appid);
        //开发者设置中的appSecret
        requestUrlParam.put("secret",ConstantUtil.secret);
        //小程序调用wx.login返回的code
        requestUrlParam.put("js_code", wxCode);
        //默认参数
        requestUrlParam.put("grant_type", "authorization_code");
        //效验
        JSONObject jsonObject = JSON.parseObject(sendPost(requestUrl,requestUrlParam));

        //得到用户的唯一id
        String openid = jsonObject.getString("openid");
        if(openid==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"openid为空");
        }
        //根据openid查询数据库是否存在
        User user = userMapper.selectUserByOpenId(openid);
        if(user!=null){
            //如果用户是封号状态就返回null
            if(user.getIsDelete()==0){
                return null;
            }
            //算出金币总和
            user.setSumGoldNumber(user.getCanWithdrawGoldCoins()+user.getMayNotWithdrawGoldCoins());
            //算出今日金币收益
            int todayIncome = goldMapper.queryGoldTodayIncome(user.getId());
            user.setTodayIncome(todayIncome);
            return user;
        }else{
            //增加新用户信息
            User user1=new User();
            user1.setAvatar(avatar);
            user1.setOpenId(openid);
            user1.setUserName(userName);
            user1.setUserSex(sex);
            user1.setCreateAt(System.currentTimeMillis()/1000+"");
            user1.setCanWithdrawGoldCoins(0);
            user1.setTodayIncome(0);
            user1.setMayNotWithdrawGoldCoins(0);
            user1.setSumGoldNumber(0);
            int i1 = userMapper.selectMaxId()+1;
            user1.setMCode("gft"+i1);

            int i = userMapper.addUser(user1);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }

            //初始化金币
            int i2 = goldMapper.addUserGoldCoins(user1.getId());
            if(i2<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"初始化金币数据失败");
            }

            //初始化荣誉等级
            int i3 = honoredMapper.addHonored(user1.getId());
            if(i3<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"初始化荣誉等级数据失败");
            }
            return user1;
        }

    }

    @Override
    public UserLoginVo loginVo(int userId) {
        UserLoginVo userLoginVo = new UserLoginVo();
        //查询并设置历史浏览数量
        userLoginVo.setHistoryPageView(myMapper.queryCheckPostsBeenReadingPastMonthCount(userId));
        //查询并设置粉丝数量
        userLoginVo.setFansNum(myMapper.queryFanCountByUserId(userId));
        //查询并设置关注数量
        userLoginVo.setAttentionNum(myMapper.queryCareAboutCountByUserId(userId));
        return userLoginVo;
    }

    @Override
    public UserVo queryUserInformationBasedUserId(int id, int userId) {
        return userMapper.queryUserPartialInformation(id);
    }

    @Override
    public User queryUserById(int userId) {
        return userMapper.queryAllUser(userId);
    }

    @Override
    public User selectUserById(int userId) {
        User user = userMapper.selectUserById(userId);
        //得到可用金币和不可用金币和
        int i = user.getMayNotWithdrawGoldCoins() + user.getCanWithdrawGoldCoins();
        user.setSumGoldNumber(i);
        //算出今日金币收益
        int todayIncome = goldMapper.queryGoldTodayIncome(user.getId());
        user.setTodayIncome(todayIncome);
        return user;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public String sendPost(String url, Map<String, ?> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        String param = "";

        for (String key : paramMap.keySet()) {
            param += key + "=" + paramMap.get(key) + "&";
        }

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

}
