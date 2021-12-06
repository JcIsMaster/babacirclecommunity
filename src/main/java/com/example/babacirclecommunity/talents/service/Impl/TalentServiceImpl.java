package com.example.babacirclecommunity.talents.service.Impl;

import com.example.babacirclecommunity.circle.dao.AttentionMapper;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.constanct.PointsType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.HonoredPointsUtil;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.talents.dao.TalentsMapper;
import com.example.babacirclecommunity.talents.entity.Talents;
import com.example.babacirclecommunity.talents.service.ITalentService;
import com.example.babacirclecommunity.talents.vo.TalentsPersonalVo;
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

    @Override
    public List<TalentsVo> queryTalentsList(String content, String city, Paging paging) {

        int page = (paging.getPage() - 1) * paging.getLimit();
        String sql = "limit " + page + "," + paging.getLimit();

        if ("undefined".equals(content) || "".equals(content) || content == null) {
            content = null;
        }

        if ("undefined".equals(city) || "".equals(city) || city == null) {
            city = null;
        }

        List<TalentsVo> talents = talentsMapper.queryTalentsList(content, city, sql);

        return talents;
    }

    @Override
    public TalentsPersonalVo queryTalentById(int otherId) {
        if (otherId == 0) {
            return null;
        }
        return talentsMapper.queryTalentByUserId(otherId);
    }

    @Override
    public ResultUtil updatePersonalTalent(Talents talents) {
        Talents talent = talentsMapper.queryTalentById(talents.getUserId());
        int i;
        //名片数据不为空则修改名片
        if (talent != null) {
            i = talentsMapper.updatePersonalTalent(talents);
            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"修改人才失败");
            }
            return ResultUtil.success(i,0);
        }
        //反之新增名片
        else {
            String s = String.valueOf(System.currentTimeMillis() / 1000);
            talents.setCreateAt(s);
            i = talentsMapper.addPersonalTalent(talents);
            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"添加人才失败");
            }
            //为用户添加荣誉积分
            HonoredPointsUtil.addHonoredPoints(talents.getUserId(), PointsType.HONORED_POINTS_Talents,0, s);
        }

        return ResultUtil.success(i,1);
    }
}
