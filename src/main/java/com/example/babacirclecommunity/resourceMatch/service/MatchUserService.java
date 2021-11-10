package com.example.babacirclecommunity.resourceMatch.service;

import com.example.babacirclecommunity.resourceMatch.entity.Parameter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MatchUserService {

    /**
     * 计数
     * @param value
     * @param userId
     * @param sql
     * @param str
     * @return
     */
    int count(String value, int userId, String sql, String str);

    /**
     * 添加用户填写参数
     * @param userId
     * @param text
     * @return
     */
    int insertParameter(int userId, String text);

    /**
     * 根据用户id查询
     * @param userId
     * @return
     */
    Parameter ParameterById(int userId);

    /**
     * 根据用户id修改
     * @param userId
     * @param text
     * @return
     */
    int updateParameter (int userId,String text);

    /**
     * 模糊查询符合要求的用户信息
     * @param value
     * @param userId
     * @param sql
     * @param str
     * @return
     */
    List<Parameter> Parameter(String value,  int userId, String sql,String str);

    /**
     * 根据用户id匹配用户
     * @param userId
     * @return
     */
    Parameter matchingByUserId(int userId);
}
