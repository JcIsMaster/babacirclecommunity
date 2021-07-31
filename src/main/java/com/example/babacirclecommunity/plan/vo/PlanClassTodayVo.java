package com.example.babacirclecommunity.plan.vo;

import com.example.babacirclecommunity.plan.entity.PlanClass;
import lombok.Data;

/**
 * @author JC
 * @date 2021/7/27 10:20
 */
@Data
public class PlanClassTodayVo extends PlanClass {

    /**
     * 继续播放视频id
     */
    private int recentlyPlayedVideoId;

    /**
     * 继续播放视频时间
     */
    private double recentlyPlayedVideoTime;

    /**
     * 继续播放视频进度
     */
    private int recentlyPlayedVideoProgress;
}
