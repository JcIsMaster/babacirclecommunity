package com.example.babacirclecommunity.personalCenter.vo;

import com.example.babacirclecommunity.learn.vo.QuestionVo;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import lombok.Data;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/27 19:59
 */
@Data
public class QuestionPersonalVo {

    /**
     * 用户信息
     */
    private PersonalCenterUserVo personalCenterUserVo;

    /**
     * 查看人是否为自己
     */
    private int isMe;

    /**
     * 用户的提问帖
     */
    private List<QuestionVo> questionVos;
}
