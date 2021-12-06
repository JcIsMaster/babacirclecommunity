package com.example.babacirclecommunity.honored.vo;

import com.example.babacirclecommunity.honored.entity.Honored;
import lombok.Data;

/**
 * @author JC
 * @date 2021/11/10 11:43
 */
@Data
public class UserHonoredCenterVo {

    /**
     * 用户荣誉等级信息
     */
    private Honored honored;

    /**
     * 匹配任务完成状态
     */
    private int matchStatus = 0;

    /**
     * 发帖任务完成状态
     */
    private int postStatus = 0;

    /**
     * 人才任务完成状态
     */
    private int talentsStatus = 0;

    /**
     * 货源任务完成状态
     */
    private int resourceStatus = 0;

    /**
     * 合作任务完成状态
     */
    private int collaborateStatus = 0;
}
