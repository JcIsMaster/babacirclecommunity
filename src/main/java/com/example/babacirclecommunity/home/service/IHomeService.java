package com.example.babacirclecommunity.home.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;

import java.util.Map;

/**
 * @author JC
 * @date 2021/5/20 13:32
 */
public interface IHomeService {

    /**
     * 搜索数据接口
     * @param strata 状态
     * @param postingName 内容
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    Object selectAllSearch(int strata,String postingName,int userId, Paging paging);

    /**
     * 查询搜索记录和其他相关信息
     * @param userId 用户id
     * @return
     */
    Map<String,Object> querySearchRecords(int userId);

    /**
     * 删除搜索历史记录
     * @param userId
     * @return
     */
    ResultUtil deleteSearchHistory(int userId);
}
