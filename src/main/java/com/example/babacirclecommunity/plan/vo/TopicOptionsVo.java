package com.example.babacirclecommunity.plan.vo;

import com.example.babacirclecommunity.plan.entity.PlanOptions;
import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/7/12 11:24
 */
@Data
public class TopicOptionsVo {

    /**
     * 唯一标识 主键
     */
    private int id;

    /**
     * 题目
     */
    private String topic;

    /**
     * 题目备注（小字）
     */
    private String remark;

    /**
     * 题目选项
     */
    private List<PlanOptions> planOptionsList;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 是否有效（0 有效  1 无效）
     */
    private int isDelete;
}
