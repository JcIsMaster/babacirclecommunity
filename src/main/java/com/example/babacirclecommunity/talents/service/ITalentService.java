package com.example.babacirclecommunity.talents.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.talents.entity.Talents;
import com.example.babacirclecommunity.talents.vo.TalentsPersonalVo;
import com.example.babacirclecommunity.talents.vo.TalentsVo;

import java.util.List;

/**
 * @author JC
 * @date 2021/6/4 14:23
 */
public interface ITalentService {

    /**
     * 查询人才列表
     * @param content
     * @param city
     * @param paging
     * @return
     */
    List<TalentsVo> queryTalentsList(String content, String city, Paging paging);

    /**
     * 根据id查询人才名片
     * @param otherId
     * @return
     */
    TalentsPersonalVo queryTalentById(int otherId);

    /**
     * 修改个人名片
     * @param talents
     * @return
     */
    ResultUtil updatePersonalTalent(Talents talents);

}
