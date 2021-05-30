package com.example.babacirclecommunity.resource.service;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;

import java.util.List;

/**
 * @author MQ
 * @date 2021/5/27 17:56
 */
public interface IResourceService {

    /**
     * 查询资源数据
     * @param paging 分页
     * @param orderRule 0 推荐 1 最新 2最热
     * @param tagId 标签id
     * @param title 标题
     * @return
     */
    List<ResourceClassificationVo> queryResource(Paging paging,int orderRule,  int tagId, String title);
}
