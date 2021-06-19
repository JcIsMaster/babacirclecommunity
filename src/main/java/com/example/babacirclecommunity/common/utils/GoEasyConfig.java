package com.example.babacirclecommunity.common.utils;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import io.goeasy.GoEasy;
import io.goeasy.publish.GoEasyError;
import io.goeasy.publish.PublishListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author MQ
 * @date 2021/6/17 15:08
 */
@Slf4j
public class  GoEasyConfig {

    /**
     * GoEasy的appkey
     */
    public static final  String appkey="BC-1f464ed03b514029aa7e541bd6d572a1";

    /**
     * GoEasy访问地址
     */
    public static final  String regionHost="http://rest-hangzhou.goeasy.io";


    /**
     * 发送信息
     * @param channel 通道名称
     * @param content 发送内容
     */
    public static void goEasy(String channel,String content){
        GoEasy goEasy = new GoEasy(GoEasyConfig.regionHost,GoEasyConfig.appkey);
        goEasy.publish(channel, content,new PublishListener(){
            @Override
            public void onSuccess() {
                log.info("消息推送成功！");
            }

            @Override
            public void onFailed(GoEasyError error) {
                log.info("消息推送失败！"+ error.getCode()+","+error.getContent());
            }
        });
    }
}
