package com.example.babacirclecommunity.plan.vo;

import com.example.babacirclecommunity.plan.entity.ClassVideo;
import com.example.babacirclecommunity.plan.entity.PlanClass;
import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/7/14 16:06
 */
@Data
public class PlanClassVo extends PlanClass {

    /**
     * 关联圈子名称
     */
    private String communityName;

    /**
     * 课程下的视频列表
     */
    private List<ClassVideo> classVideos;
}
