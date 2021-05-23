package com.example.babacirclecommunity.personalCenter.vo;

import com.example.babacirclecommunity.circle.vo.CircleVo;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/20 16:29
 */
@Data
public class PersonalVo {

    /**
     * 用户信息
     */
    private PersonalCenterUserVo personalCenterUserVo;

    /**
     * 是否关注 0未关注 1已关注 2本人不显示
     */
    private int whetherAttention;

    /**
     * 创建的圈子
     */
    private List<CircleVo> circleVos;

    /**
     * 加入的圈子
     */
    private List<CircleVo> joinedCircleVos;

    /**
     * 用户动态帖数量
     */
    private int postedCircleNum;

    /**
     * 用户点赞帖数量
     */
    private int greatCircleNum;

    /**
     * 用户关注帖数量
     */
    private int attentionNum;
}
