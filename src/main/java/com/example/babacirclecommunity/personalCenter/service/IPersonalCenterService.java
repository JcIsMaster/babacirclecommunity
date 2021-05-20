package com.example.babacirclecommunity.personalCenter.service;

import com.example.babacirclecommunity.personalCenter.vo.PersonalVo;

/**
 * @author JC
 * @date 2021/5/20 16:36
 */
public interface IPersonalCenterService {

    /**
     * 查询个人中心 By userId
     * @param userId
     * @return
     */
    PersonalVo queryPersonalCenter(int userId);
}
