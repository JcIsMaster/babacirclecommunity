package com.example.babacirclecommunity.my.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.my.vo.PeopleCareAboutVo;

import java.util.List;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/6/8 11:15
 */
public interface IMyService {

    /**
     * 查询我关注的人
     * @param paging 分页
     * @param userId 用户id
     * @return
     */
    Map<String,Object> queryPeopleCareAbout(Paging paging, int userId);

    /**
     * 查询我的粉丝
     * @param paging 分页
     * @param userId 用户id
     * @return
     */
    Map<String,Object> queryFan(Paging paging, int userId);
}
