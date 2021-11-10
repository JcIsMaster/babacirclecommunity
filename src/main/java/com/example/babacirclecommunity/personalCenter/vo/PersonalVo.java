package com.example.babacirclecommunity.personalCenter.vo;

import com.example.babacirclecommunity.sameCity.entity.ParameterJson;
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
     * 是否商家
     */
    private ParameterJson parameterJson;

    /**
     * 是否关注 0未关注 1已关注 2本人不显示
     */
    private int whetherAttention;

    /**
     * 用户动态帖数量
     */
    private int postedCircleNum;

    /**
     * 用户合作帖数量
     */
    private int collaborateNum;

    /**
     * 用户货源帖数量
     */
    private int resourceNum;
}
