package com.example.babacirclecommunity.personalCenter.service;

import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.circle.vo.CircleVo;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.personalCenter.vo.PersonalVo;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/20 16:36
 */
public interface IPersonalCenterService {

    /**
     * 查询个人中心 By userId
     * @param userId
     * @param otherId
     * @return
     */
    PersonalVo queryPersonalCenter(int userId,int otherId);

    /**
     * 查询个人中心里圈子相关的动态接口
     * @param userId
     * @param otherId
     * @param type
     * @param paging
     * @return
     */
    Object queryPersonalCircle(int userId, int otherId,int type, Paging paging);

    /**
     * 根据id查询创建的圈子
     * @param otherId
     * @param type
     * @param paging
     * @return
     */
    List<CircleVo> queryCircleByUSerId(int otherId, int type, Paging paging);
}
