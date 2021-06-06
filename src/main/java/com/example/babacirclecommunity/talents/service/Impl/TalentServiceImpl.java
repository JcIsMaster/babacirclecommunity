package com.example.babacirclecommunity.talents.service.Impl;

import com.example.babacirclecommunity.circle.dao.AttentionMapper;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.talents.dao.TalentsMapper;
import com.example.babacirclecommunity.talents.entity.Talents;
import com.example.babacirclecommunity.talents.service.ITalentService;
import com.example.babacirclecommunity.talents.vo.TalentsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author JC
 * @date 2021/6/4 14:24
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TalentServiceImpl implements ITalentService {

    @Autowired
    private TalentsMapper talentsMapper;

    @Autowired
    private AttentionMapper attentionMapper;

    @Override
    public List<Talents> queryTalentsList(int userId,String content, String city, Paging paging) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";

        if (content.equals("undefined") || content.equals("") || content == null){
            content = null;
        }

        if (city.equals("undefined") || city.equals("") || city == null){
            city = null;
        }

        List<Talents> talents = talentsMapper.queryTalentsList(content, city, sql);
        if(userId != 0){
            for (Talents talent : talents) {
                //查看我是否关注了此人
                int i = attentionMapper.queryWhetherAttention(userId, talent.getId());
                if (i > 0){
                    talent.setWhetherAttention(1);
                }
            }
        }
        return talents;
    }

    @Override
    public TalentsVo queryTalentById(int userId) {
        if (userId == 0){
            return null;
        }
        TalentsVo talentsVo = talentsMapper.queryTalentById(userId);
        return talentsVo;
    }

    @Override
    public int updatePersonalTalent(Talents talents) {
        TalentsVo talentsVo = talentsMapper.queryTalentById(talents.getId());
        int i = 0;
        //名片数据不为空则修改名片
        if (talentsVo != null){
            i = talentsMapper.updatePersonalTalent(talents);
        }
        //反之新增名片
        else {
            talents.setCreateAt(System.currentTimeMillis() / 1000 + "");
            i = talentsMapper.addPersonalTalent(talents);
        }
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return i;
    }
}
