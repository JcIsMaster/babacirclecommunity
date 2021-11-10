package com.example.babacirclecommunity.activity.vo;

import com.example.babacirclecommunity.activity.entity.ActivityOnline;
import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/10/7 13:51
 */
@Data
public class ActivityOnlineVo extends ActivityOnline {

    /**
     * 活动参与人数
     */
    private int participateNum;

    /**
     * 活动参与者列表（最多显示6条）
     */
    private List<ActivityOnlineParticipateVo> activityOnlineParticipateVos;
}
