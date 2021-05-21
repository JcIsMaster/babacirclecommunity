package com.example.babacirclecommunity.personalCenter.service.impl;

import com.example.babacirclecommunity.circle.dao.AttentionMapper;
import com.example.babacirclecommunity.circle.dao.CircleMapper;
import com.example.babacirclecommunity.circle.vo.CircleVo;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.personalCenter.service.IPersonalCenterService;
import com.example.babacirclecommunity.personalCenter.vo.PersonalVo;
import com.example.babacirclecommunity.user.dao.UserMapper;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/20 16:38
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PersonalCenterServiceImpl implements IPersonalCenterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Override
    public PersonalVo queryPersonalCenter(int userId,int otherId) {

        String sql="limit 0,4";
        PersonalVo personalVo = new PersonalVo();
        //查询用户基本信息
        PersonalCenterUserVo personalCenterUserVo = userMapper.queryUserById(otherId);
        //查询我是否关注了他
        int whetherAttention = attentionMapper.queryWhetherAttention(userId, otherId);
        //查询他创建的圈子
        List<CircleVo> circleVos = circleMapper.myCircleAndCircleJoined(otherId, sql);
        //查询他加入的圈子
        List<CircleVo> circleJoined = circleMapper.circleJoined(otherId, sql);
        personalVo.setPersonalCenterUserVo(personalCenterUserVo);
        personalVo.setCircleVos(circleVos);
        personalVo.setJoinedCircleVos(circleJoined);
        if (userId == 0){
            personalVo.setWhetherAttention(0);
            return personalVo;
        }
        if (userId == otherId){
            personalVo.setWhetherAttention(2);
            return personalVo;
        }
        personalVo.setWhetherAttention(whetherAttention);

        return personalVo;
    }

    @Override
    public List<CircleVo> queryCircleByUSerId(int otherId,int type,Paging paging) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";

        //查询Ta创建的圈子
        if (type == 0){
            List<CircleVo> circleVos = circleMapper.myCircleAndCircleJoined(otherId, sql);
            return circleVos;
        }
        //查询加入的圈子
        if (type == 1){
            List<CircleVo> circleVos = circleMapper.circleJoined(otherId, sql);
            for (int i = 0;i < circleVos.size();i++){
                //统计每个圈子的人数
                int i1 = circleMapper.countCircleJoined(circleVos.get(i).getId());
                circleVos.get(i).setCnt(i1);
            }
            return circleVos;
        }
        return null;
    }
}
