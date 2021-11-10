package com.example.babacirclecommunity.resourceMatch.service.impl;

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
        return matchUserDao.insertParameter(userId, text);
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
