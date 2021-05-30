package com.example.babacirclecommunity.personalCenter.vo;

import com.example.babacirclecommunity.learn.vo.PublicClassTagVo;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/30 10:50
 */
@Data
public class ClassPersonalVo {
    /**
     * 用户信息
     */
    private PersonalCenterUserVo personalCenterUserVo;

    /**
     * 查看人是否为自己
     */
    private int isMe;

    /**
     * 用户的公开课
     */
    private List<PublicClassTagVo> publicClassTagVos;
}
