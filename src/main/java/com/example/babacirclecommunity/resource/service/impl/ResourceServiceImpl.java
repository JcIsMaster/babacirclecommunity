package com.example.babacirclecommunity.resource.service.impl;

import com.example.babacirclecommunity.circle.dao.BrowseMapper;
import com.example.babacirclecommunity.circle.entity.Browse;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.DateUtils;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.TimeUtil;
import com.example.babacirclecommunity.resource.dao.ResourceMapper;
import com.example.babacirclecommunity.resource.service.IResourceService;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.resource.vo.ResourcesVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
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

    @Autowired
    private BrowseMapper browseMapper;

    @Override
    public List<ResourceClassificationVo> queryResource(Paging paging, int orderRule, int tagId, String title) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";
        System.out.println(tagId);
        System.out.println(title);
        if(title==null || title.equals("") || title.equals("undefined")){
            title="";
        }
        return resourceMapper.queryResource(sql,orderRule,title,tagId);
    }

    @Override
    public ResourcesVo selectSingleResourcePost(int id, int userId) throws ParseException {
        ResourcesVo resourcesVo = resourceMapper.selectSingleResourcePost(id);

        //在用户登录的情况下 增加帖子浏览记录
        if(userId!=0){
            //查看是否收藏
            int selectWhetherCollection = resourceMapper.selectWhetherCollection(userId, id);
            if(selectWhetherCollection>0){
                resourcesVo.setWhetherCollection(1);
            }

            //得到上一次观看帖子的时间
            Browse browse = new Browse();
            String s = browseMapper.selectCreateAt(id, userId);
            if(s==null){
                //增加浏览记录
                browse.setCreateAt(System.currentTimeMillis()/1000+"");
                browse.setUId(userId);
                browse.setZqId(id);
                browse.setType(0);

                //增加浏览记录
                int i = browseMapper.addBrowse(browse);
                if(i<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
                }

                //修改帖子浏览数量
                int i1 = resourceMapper.updateBrowse(id);
                if(i1<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            }else{
                //得到过去时间和现在的时间是否相隔1440分钟 如果相隔了 就添加新的浏览记录
                long minutesApart = TimeUtil.getMinutesApart(s);
                if(minutesApart>=1440){
                    //增加浏览记录
                    browse.setCreateAt(System.currentTimeMillis()/1000+"");
                    browse.setUId(userId);
                    browse.setZqId(id);
                    browse.setType(0);

                    //增加浏览记录
                    int i = browseMapper.addBrowse(browse);
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
                    }

                    //修改帖子浏览数量
                    int i1 = resourceMapper.updateBrowse(id);
                    if(i1<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }

                }
            }
        }

        //得到当前时间戳和过去时间戳比较相隔多少分钟或者多少小时或者都少天或者多少年
        String time = DateUtils.getTime(resourcesVo.getCreateAt());

        //根据帖子id查询出当前帖子图片
        String[] strings = resourceMapper.selectImgByPostId(resourcesVo.getId());
        resourcesVo.setImg(strings);
        resourcesVo.setCreateAt(String.valueOf(time));

        //得到收藏数量
        int i = resourceMapper.selectCollectNumber(id);
        resourcesVo.setCollect(i);

        //得到浏览过人的头像
        String[] strings1 = resourceMapper.selectBrowseAvatar(id);
        resourcesVo.setBrowseAvatar(strings1);

        //得到这个帖子的观看数量
        int browse = resourceMapper.countPostNum(id);
        resourcesVo.setBrowse(browse);

        return resourcesVo;
    }

    @Override
    public List<ResourceClassificationVo> queryHavePostedPosts(int othersId,Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pag="limit "+page+","+paging.getLimit()+"";

        List<ResourceClassificationVo> homeClassificationVos = resourceMapper.queryHavePostedPosts(othersId,pag);
        return homeClassificationVos;
    }
}
