package com.example.babacirclecommunity.personalCenter.service.impl;

import com.example.babacirclecommunity.personalCenter.service.IPersonalCenterService;
import com.example.babacirclecommunity.personalCenter.vo.PersonalVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author JC
 * @date 2021/5/20 16:38
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PersonalCenterServiceImpl implements IPersonalCenterService {

    @Override
    public PersonalVo queryPersonalCenter(int userId) {

        return null;
    }
}
