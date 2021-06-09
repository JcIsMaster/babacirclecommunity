package com.example.babacirclecommunity.my.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.my.entity.ComplaintsSuggestions;
import com.example.babacirclecommunity.my.vo.PeopleCareAboutVo;
import com.example.babacirclecommunity.user.entity.User;

import java.text.ParseException;
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

    /**
     * 建议
     * @param complaintsSuggestions
     * @return
     */
    int addComplaintsSuggestions(ComplaintsSuggestions complaintsSuggestions);

    /**
     * 点击头像进入的接口
     * @param bUserId 被观看人id
     * @param userId 观看人id
     * @return
     */
    void ClickInterfaceHeadImageEnter(int bUserId, int userId);

    /**
     * 查询看过我的人
     * @param userId 登录人id
     * @param paging 分页
     * @return
     */
    List<User> queryPeopleWhoHaveSeenMe(int userId,Paging paging);

    /**
     * 修改用户介绍
     * @param introduction 内容
     * @param userId 用户id
     * @return
     */
    int updateUserDataByIntroduction(String introduction,int userId) throws ParseException;

    /**
     * 修改用户地址
     * @param domicileProvince 省
     * @param domicileCity 市
     * @param domicileCounty 县
     * @param userId 用户id
     * @return
     */
    int updateUserAddress(String domicileProvince,String domicileCity,String domicileCounty,int userId);

    /**
     * 修改用户头像
     * @param avatar 地址
     * @param userId 用户id
     * @return
     */
    int updateUserAvatar(String avatar, int userId);

    /**
     * 修改用户生日
     * @param birthday 内容
     * @param userId 用户id
     * @return
     */
    int updateUserBirthday(String birthday, int userId);

    /**
     * 修改背景图
     * @param backgroundPicture 地址
     * @param userId 用户id
     * @return
     */
    int updateUserBackgroundPicture(String backgroundPicture, int userId);

    /**
     * 修改用户名称
     * @param name 名称
     * @param userId 用户id
     * @return
     */
    int updateUserName(String name, int userId) throws ParseException;

    /**
     * 修改性别
     * @param sex 用户性别（1男，0女）
     * @param userId 用户id
     * @return
     */
    int updateUserSex(String sex, int userId);
}
