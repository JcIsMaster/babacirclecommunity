package com.example.babacirclecommunity.personalCenter.vo;

import com.example.babacirclecommunity.learn.vo.DryGoodsVo;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/28 15:18
 */
@Data
public class DryGoodsPersonalVo {

    /**
     * 用户信息
     */
    private PersonalCenterUserVo personalCenterUserVo;

    /**
     * 查看人是否为自己
     */
    private int isMe;

    /**
     * 用户的干货帖
     */
    private List<DryGoodsVo> dryGoodsVos;
}

