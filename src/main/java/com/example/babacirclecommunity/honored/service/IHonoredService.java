package com.example.babacirclecommunity.honored.service;

import com.example.babacirclecommunity.honored.vo.UserHonoredCenterVo;

/**
 * @author JC
 * @date 2021/11/10 11:44
 */
public interface IHonoredService {

    /**
     * 查询用户荣誉权益中心
     * @param userId
     * @return
     */
    UserHonoredCenterVo queryUserHonoredCenter(int userId);

}
