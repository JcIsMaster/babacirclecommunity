package com.example.babacirclecommunity.sameCity.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.sameCity.vo.SameCityUser;

import java.util.List;

/**
 * @author JC
 * @date 2021/10/23 14:35
 */
public interface SameCityService {

    /**
     * 查询同城用户列表
     * @param isShop
     * @param city
     * @param paging
     * @return
     */
    List<SameCityUser> queryFriendsInTheSameCity(int isShop, String city, Paging paging);
}
