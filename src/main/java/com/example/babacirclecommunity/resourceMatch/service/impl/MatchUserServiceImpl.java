package com.example.babacirclecommunity.resourceMatch.service.impl;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.constanct.PointsType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.HonoredPointsUtil;
import com.example.babacirclecommunity.resourceMatch.dao.MatchUserDao;
import com.example.babacirclecommunity.resourceMatch.entity.Parameter;
import com.example.babacirclecommunity.resourceMatch.service.MatchUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service
public class MatchUserServiceImpl implements MatchUserService {

    @Autowired
    private MatchUserDao matchUserDao;


    @Override
    public int count(String value, int userId, String sql, String str) {
        return matchUserDao.count(value, userId, sql, str);
    }


    @Override
    public int insertParameter(int userId, String text) {
        int i = matchUserDao.insertParameter(userId, text);
        if (i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        //为用户添加荣誉积分
        HonoredPointsUtil.addHonoredPoints(userId, PointsType.HONORED_POINTS_MATCH,0,String.valueOf(System.currentTimeMillis()/1000));
        return i;
    }

    @Override
    public Parameter ParameterById(int userId) {
        return matchUserDao.ParameterById(userId);
    }

    @Override
    public int updateParameter(int userId, String text) {
        return matchUserDao.updateParameter(userId, text);
    }

    @Override
    public List<Parameter> Parameter(String value, int userId, String sql,String str) {
        return matchUserDao.Parameter(value, userId, sql,str);
    }

    @Override
    public Parameter matchingByUserId(int userId) {
        return matchUserDao.ParameterById(userId);
    }

}
