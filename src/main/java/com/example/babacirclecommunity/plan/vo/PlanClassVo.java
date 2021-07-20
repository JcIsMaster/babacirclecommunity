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

    private List<ClassVideo> classVideos;
}
