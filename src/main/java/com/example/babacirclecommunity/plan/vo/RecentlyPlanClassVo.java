package com.example.babacirclecommunity.plan.vo;

import com.example.babacirclecommunity.plan.entity.PlanClass;
import lombok.Data;

/**
 * @author JC
 * @date 2021/7/22 16:27
 * 最近学习的课程
 */
@Data
public class RecentlyPlanClassVo extends PlanClass {

    /**
     * 课程观看总进度
     */
    private int videoViewingProgress;
}
