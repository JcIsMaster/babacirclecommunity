package com.example.babacirclecommunity.my.service.impl;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.my.dao.MyMapper;
import com.example.babacirclecommunity.my.service.IMyService;
import com.example.babacirclecommunity.my.vo.PeopleCareAboutVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MQ
 * @date 2021/6/8 11:15
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class MyServiceImpl implements IMyService {

    @Autowired
    private MyMapper myMapper;

    /**
     * 得到分页
     * @param paging
     * @return
     */
    public String getPaging(Paging paging){
        int page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";
        return sql;
    }


    @Override
    public Map<String,Object> queryPeopleCareAbout(Paging paging, int userId) {


        Map<String,Object> map=null;

        //查询我关注人
        List<PeopleCareAboutVo> peopleCareAboutVos = myMapper.queryPeopleCareAbout(userId, getPaging(paging));
        if(peopleCareAboutVos!=null && peopleCareAboutVos.size()!=0){
            map=new HashMap<>(2);
            map.put("peopleCareAboutVos",peopleCareAboutVos);
            map.put("count",peopleCareAboutVos.size());
            return map;
        }

        return null;
    }

    @Override
    public Map<String, Object> queryFan(Paging paging, int userId) {

        Map<String,Object> map=null;

        //查询我的粉丝
        List<PeopleCareAboutVo> queryFan = myMapper.queryFan(userId, getPaging(paging));
        if(queryFan!=null && queryFan.size()!=0){
            map=new HashMap<>(2);
            map.put("queryFan",queryFan);
            map.put("count",queryFan.size());
            return map;
        }

        return null;
    }
}
