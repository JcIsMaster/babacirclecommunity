package com.example.babacirclecommunity.resource.service.impl;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.resource.dao.ResourceMapper;
import com.example.babacirclecommunity.resource.service.IResourceService;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MQ
 * @date 2021/5/27 17:56
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ResourceServiceImpl implements IResourceService {


    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public List<ResourceClassificationVo> queryResource(Paging paging, int orderRule, int tagId, String title) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";
        System.out.println(tagId);
        System.out.println(title);
        return resourceMapper.queryResource(sql,orderRule,title,tagId);
    }
}
