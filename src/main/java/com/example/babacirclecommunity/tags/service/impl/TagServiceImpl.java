package com.example.babacirclecommunity.tags.service.impl;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.ConstantUtil;
import com.example.babacirclecommunity.home.entity.Community;
import com.example.babacirclecommunity.tags.dao.TagMapper;
import com.example.babacirclecommunity.tags.entity.Tag;
import com.example.babacirclecommunity.tags.service.ITagService;
import com.example.babacirclecommunity.tags.vo.AllTagVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

/**
 * @author JC
 * @date 2021/1/21 16:08
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TagServiceImpl implements ITagService {
    @Autowired
    private TagMapper tagMapper;



    @Override
    public List<Tag> selectResourcesAllTag(int type) {
        List<Tag> tags = tagMapper.selectResourcesAllTag(type);
        for (int i=0;i<tags.size();i++){
            if("人才".equals(tags.get(i).getTagName())){
                tags.remove(i);
            }
        }
        return tags;
    }

    @Override
    public List<Tag> selectResourcesAllTags(int tid) {
        //根据一级标签查询二级标签数据
        List<Tag> tags = tagMapper.selectResourcesAllTags(tid);

   /*     String str="";

        if(tags.get(0).getType()==0){
            str="tb_resources";
        }

        if(tags.get(0).getType()==1){
            str="tb_circles";
        }*/
       /* //将对象List中的某个字段放到新的List中
        List<Integer> stringList = tags.stream().map(Tag::getId).collect(Collectors.toList());*/
/*
       for (int i=0;i<tags.size();i++){
           //根据二级标签组查询每个标签发过多少个帖子
           int i1 = tagMapper.selectTagsNum(tags.get(i).getId(), str);
           tags.get(i).setNum(i1);
       }*/

        return tags;
    }

    @Override
    public List<AllTagVo> queryAllPrimaryAndSecondaryTags() {
        String str="";

        List<AllTagVo> tags = tagMapper.queryAllTag();
        for (int i=0;i<tags.size();i++){
            //根据一级标签查询所有二级标签下面的帖子数量
            if(tags.get(i).getType()==0){
                str="tb_resources";
            }

            if(tags.get(i).getType()==1){
                str="tb_circles";
            }
            //查询每个标签 下面有多少个帖子
            List<Tag> tags1 = tagMapper.queryHowManyPostsAreInEachCell(str, tags.get(i).getId());
            tags.get(i).setTag(tags1);
        }

        return tags;
    }




}
