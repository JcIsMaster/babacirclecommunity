package com.example.babacirclecommunity.sameCity.service.impl;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.sameCity.dao.SameCityMapper;
import com.example.babacirclecommunity.sameCity.service.SameCityService;
import com.example.babacirclecommunity.sameCity.vo.SameCityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JC
 * @date 2021/10/23 14:36
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ISameCityServiceImpl implements SameCityService {

    @Autowired
    private SameCityMapper sameCityMapper;

    @Override
    public List<SameCityUser> queryFriendsInTheSameCity(int isShop, String city, Paging paging) {
        if (isShop == 0) {
            return sameCityMapper.queryIsShopInTheSameCity(city,getPaging(paging));
        } else if (isShop == 1) {
            return sameCityMapper.queryNoShopInTheSameCity(city,getPaging(paging));
        } else {
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"参数错误");
        }
    }

    /**
     * 分页获取
     * @param paging
     * @return
     */
    public String getPaging(Paging paging) {
        int page = (paging.getPage() - 1) * paging.getLimit();
        return "limit " + page + "," + paging.getLimit();
    }
}
