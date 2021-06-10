package com.example.babacirclecommunity.learn.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.learn.entity.ClassOrder;
import com.example.babacirclecommunity.learn.vo.PublicClassVo;
import com.example.babacirclecommunity.personalCenter.vo.ClassPersonalVo;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/6 9:45
 */
public interface IPublicClassService {

    /**
     * 根据id查询公开课详情
     * @param id 公开课id
     * @param userId 用户id
     * @return
     */
    PublicClassVo queryPublicClassById(int id,int userId);

    /**
     * 公开课收藏
     * @param id
     * @param userId
     * @return
     */
    int giveCollect(int id,int userId);

    /**
     * 公开课购买
     * @param classOrder 课程订单对象
     * @return
     */
    ResultUtil buyerClass(ClassOrder classOrder);

    /**
     * 公开课个人中心
     * @param userId
     * @param otherId
     * @param paging
     * @return
     */
    ClassPersonalVo queryClassPersonal(int userId, int otherId, Paging paging);

    /**
     * 得到公开课海报
     * @param id 帖子id
     * @param pageUrl 二维码指向的地址
     * @return
     */
    List<String> getPublicClass(String id, String pageUrl);
}
