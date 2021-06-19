package com.example.babacirclecommunity.collaborate.service.impl;

import com.example.babacirclecommunity.circle.dao.BrowseMapper;
import com.example.babacirclecommunity.circle.entity.Browse;
import com.example.babacirclecommunity.collaborate.dao.CollaborateMapper;
import com.example.babacirclecommunity.collaborate.service.ICollaborateService;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.DateUtils;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.TimeUtil;
import com.example.babacirclecommunity.resource.dao.CollectionMapper;
import com.example.babacirclecommunity.resource.entity.Collection;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.resource.vo.ResourcesVo;
import com.example.babacirclecommunity.user.dao.UserMapper;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author MQ
 * @date 2021/5/31 10:47
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CollaborateServiceImpl implements ICollaborateService {

    @Autowired
    private CollaborateMapper collaborateMapper;

    @Autowired
    private BrowseMapper browseMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Override
    public List<ResourceClassificationVo> queryCollaborate(Paging paging, int orderRule, int tagId, String title) {
        Integer page = (paging.getPage() - 1) * paging.getLimit();
        String sql = "limit " + page + "," + paging.getLimit() + "";
        if (title == null || title.equals("") || title.equals("undefined")) {
            title = "";
        }
        return collaborateMapper.queryCollaborate(sql, orderRule, title, tagId);
    }

    @Override
    public ResourcesVo querySingleCollaboratePost(int id, int userId) throws ParseException {
        ResourcesVo resourcesVo = collaborateMapper.querySingleCollaboratePost(id);

        //在用户登录的情况下 增加帖子浏览记录
        if (userId != 0) {
            //查看是否收藏
            int selectWhetherCollection = collaborateMapper.selectWhetherCollection(userId, id);
            if (selectWhetherCollection > 0) {
                resourcesVo.setWhetherCollection(1);
            }

            //得到上一次观看帖子的时间
            Browse browse = new Browse();
            String s = collaborateMapper.selectCreateAt(id, userId);
            if (s == null) {
                //增加浏览记录
                browse.setCreateAt(System.currentTimeMillis() / 1000 + "");
                browse.setUId(userId);
                browse.setZqId(id);
                browse.setType(0);

                //增加浏览记录
                int i = browseMapper.addBrowse(browse);
                if (i <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "增加浏览记录错误");
                }

                //修改帖子浏览数量
                int i1 = collaborateMapper.updateBrowse(id);
                if (i1 <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            } else {
                //得到过去时间和现在的时间是否相隔1440分钟 如果相隔了 就添加新的浏览记录
                long minutesApart = TimeUtil.getMinutesApart(s);
                if (minutesApart >= 1440) {
                    //修改帖子浏览数量
                    int i1 = collaborateMapper.updateBrowse(id);
                    if (i1 <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }

                }
            }
        }

        //得到当前时间戳和过去时间戳比较相隔多少分钟或者多少小时或者都少天或者多少年
        String time = DateUtils.getTime(resourcesVo.getCreateAt());

        //根据帖子id查询出当前帖子图片
        String[] strings = collaborateMapper.selectImgByPostId(resourcesVo.getId());
        resourcesVo.setImg(strings);
        resourcesVo.setCreateAt(String.valueOf(time));

        //得到收藏数量
        int i = collaborateMapper.selectCollectNumber(id);
        resourcesVo.setCollect(i);

        //得到浏览过人的头像
        String[] strings1 = collaborateMapper.selectBrowseAvatar(id);
        resourcesVo.setBrowseAvatar(strings1);

        //得到这个帖子的观看数量
        int browse = collaborateMapper.countPostNum(id);
        resourcesVo.setBrowse(browse);

        return resourcesVo;
    }

    @Override
    public Map<String, Object> queryHaveCollaboratePostedPosts(int userId, int othersId, Paging paging) {
        Integer page = (paging.getPage() - 1) * paging.getLimit();
        String pag = "limit " + page + "," + paging.getLimit() + "";

        //插叙资源帖子信息
        List<ResourceClassificationVo> homeClassificationVos = collaborateMapper.queryHavePostedPosts(othersId, pag);

        //根据用户id查询出用户信息
        PersonalCenterUserVo personalCenterUserVo = userMapper.queryUserById(othersId);

        Map<String, Object> map = new HashMap<>(5);

        map.put("homeClassificationVos", homeClassificationVos);
        map.put("user", personalCenterUserVo);

        if (userId == 0) {
            map.put("isMe", 0);
            return map;
        }
        if (userId == othersId) {
            map.put("isMe", 1);
            return map;
        }
        map.put("isMe", 0);

        return map;
    }

    @Override
    public List<ResourceClassificationVo> queryRecommendedSecondaryTagId(int id, int userId, int tid) {
        List<ResourceClassificationVo> homeClassificationVos = collaborateMapper.queryRecommendedSecondaryTagId(id);

        //筛选掉等于当前用户id的数据
        //筛选掉当前点进来的帖子是一样的就干掉
        List<ResourceClassificationVo> collect = homeClassificationVos.stream().filter(u -> u.getUId() != userId).filter(a -> a.getId() != tid).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<ResourcesVo> queryAllCollaborateVideosPrimaryTagId(int id, Paging paging, int userId) throws ParseException {
        Integer page = (paging.getPage() - 1) * paging.getLimit();
        String pagings = "limit " + page + "," + paging.getLimit() + "";

        //是否收藏
        int selectWhetherCollection = 0;

        //List存储数据顺序与插入数据顺序一致，存在先进先出的概念。
        List<ResourcesVo> resourcesVoa = new ArrayList<>();

        //根据id查询单个帖子
        ResourcesVo resourcesVo = collaborateMapper.querySingleResourcePost(id);
        //得到这个帖子的观看数量
        int countPostNum = collaborateMapper.countPostNum(resourcesVo.getId());
        resourcesVo.setBrowse(countPostNum);

        //得到收藏数量
        int selectCollectNumber = collaborateMapper.selectCollectNumber(resourcesVo.getId());
        resourcesVo.setCollect(selectCollectNumber);

        //查看是否收藏
        selectWhetherCollection = collaborateMapper.selectWhetherCollection(userId, resourcesVo.getId());
        if (selectWhetherCollection > 0) {
            resourcesVo.setWhetherCollection(1);
        }
        resourcesVoa.add(resourcesVo);

        //根据一级标签id查询所有视频
        List<ResourcesVo> resourcesVos1 = collaborateMapper.queryAllVideosPrimaryTagId(resourcesVo.getTagsOne(), pagings);

        //去除一样的
        List<ResourcesVo> resourcesVos = resourcesVos1.stream().filter(u -> u.getId() != resourcesVo.getId()).collect(Collectors.toList());
        for (int i = 0; i < resourcesVos.size(); i++) {

            //查看是否收藏
            selectWhetherCollection = collaborateMapper.selectWhetherCollection(userId, resourcesVos.get(i).getId());
            if (selectWhetherCollection > 0) {
                resourcesVos.get(i).setWhetherCollection(1);
            }

            //得到当前时间戳和过去时间戳比较相隔多少分钟或者多少小时或者都少天或者多少年
            String time = DateUtils.getTime(resourcesVos.get(i).getCreateAt());
            resourcesVos.get(i).setCreateAt(time);

            //得到上一次观看帖子的时间
            Browse browse = new Browse();
            String s = browseMapper.selectCreateAt(resourcesVos.get(i).getId(), userId);
            if (s == null) {
                //增加浏览记录
                browse.setCreateAt(System.currentTimeMillis() / 1000 + "");
                browse.setUId(userId);
                browse.setZqId(resourcesVos.get(i).getId());
                browse.setType(0);

                //增加浏览记录
                int iq = browseMapper.addBrowse(browse);
                if (iq <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "增加浏览记录错误");
                }

                //修改帖子浏览数量
                int i1 = collaborateMapper.updateBrowse(resourcesVos.get(i).getId());
                if (i1 <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            } else {
                //得到过去时间和现在的时间是否相隔1440分钟 如果相隔了 就添加新的浏览记录
                long minutesApart = TimeUtil.getMinutesApart(s);
                if (minutesApart >= 1440) {
                    //增加浏览记录
                    browse.setCreateAt(System.currentTimeMillis() / 1000 + "");
                    browse.setUId(userId);
                    browse.setZqId(resourcesVos.get(i).getId());
                    browse.setType(0);

                    //增加浏览记录
                    int ie = browseMapper.addBrowse(browse);
                    if (ie <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR, "增加浏览记录错误");
                    }

                    //修改帖子浏览数量
                    int i1 = collaborateMapper.updateBrowse(resourcesVos.get(i).getId());
                    if (i1 <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }

                }
            }

            //得到这个帖子的观看数量
            int i2 = collaborateMapper.countPostNum(resourcesVos.get(i).getId());
            resourcesVos.get(i).setBrowse(i2);

            //得到收藏数量
            int i1 = collaborateMapper.selectCollectNumber(resourcesVos.get(i).getId());
            resourcesVos.get(i).setCollect(i1);

            //将查询出来的帖子视屏存放打list中
            resourcesVoa.add(resourcesVos.get(i));
        }

        return resourcesVoa;
    }

    @Override
    public int collectionCollaboratePost(Collection collection) {
        collection.setCreateAt(System.currentTimeMillis() / 1000 + "");

        //查看是否有数据存在
        Collection collection1 = collectionMapper.selectCountWhether(collection.getUserId(), collection.getTId(), 1);

        //如果不存在
        if (collection1 == null) {
            //添加收藏信息
            int addCollection = collectionMapper.addCollectionPost(collection.getUserId(), collection.getTId(), collection.getCreateAt(), collection.getRemarks(), 1);
            if (addCollection <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "添加收藏信息错误");
            }
            return addCollection;
        }

        int i = 0;
        //如果当前状态是1 那就改为0 取消收藏
        if (collection1.getIsDelete() == 1) {
            i = collectionMapper.updateCollectionStatus(collection1.getId(), 0, 1);
        }

        //如果当前状态是0 那就改为1 为收藏状态
        if (collection1.getIsDelete() == 0) {
            i = collectionMapper.updateCollectionStatus(collection1.getId(), 1, 1);
        }

        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        return i;
    }
}
