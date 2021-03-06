package com.example.babacirclecommunity.circle.service.impl;

import com.example.babacirclecommunity.circle.dao.*;
import com.example.babacirclecommunity.circle.entity.*;
import com.example.babacirclecommunity.circle.service.ICircleService;
import com.example.babacirclecommunity.circle.vo.*;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.constanct.HonoredLevel;
import com.example.babacirclecommunity.common.constanct.PointsType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.*;
import com.example.babacirclecommunity.home.entity.Community;
import com.example.babacirclecommunity.my.dao.MyMapper;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.sameCity.dao.SameCityMapper;
import com.example.babacirclecommunity.tags.dao.TagMapper;
import com.example.babacirclecommunity.tags.entity.Tag;
import com.example.babacirclecommunity.user.vo.UserRankVo;
import com.example.babacirclecommunity.user.vo.UserVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.io.IOException;
import java.security.Key;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MQ
 * @date 2021/5/20 19:39
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "circleCache")
@Transactional(rollbackFor = Exception.class)
public class CircleServiceImpl implements ICircleService {

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private CircleGiveMapper circleGiveMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private BrowseMapper browseMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private CommunityMapper communityMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisConfig redisConfig;

    @Resource
    private SameCityMapper sameCityMapper;


    public String getPaging(Paging paging) {
        int page = (paging.getPage() - 1) * paging.getLimit();
        return "limit " + page + "," + paging.getLimit();
    }


    @Override
    public List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging) {

        //查询我关注的人发的帖子
        List<CircleClassificationVo> circleClassificationVos = circleMapper.queryAttentionPerson(userId, getPaging(paging));
        for (int i = 0; i < circleClassificationVos.size(); i++) {
            //得到图片组
            String[] strings = circleMapper.selectImgByPostId(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setImg(strings);

            //得到点过赞人的头像
            String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setGiveAvatar(strings1);

            //等于0在用户没有到登录的情况下 直接设置没有点赞
            if (userId == 0) {
                circleClassificationVos.get(i).setWhetherGive(0);
                circleClassificationVos.get(i).setWhetherAttention(0);
            } else {

                //查询是否对帖子点了赞   0没有 1有
                Integer integer = circleGiveMapper.whetherGive(userId, circleClassificationVos.get(i).getId());
                if (integer > 0) {
                    circleClassificationVos.get(i).setWhetherGive(1);
                }
            }

            //将所有关注状态为1关注状态
            circleClassificationVos.get(i).setWhetherAttention(1);

            //将时间戳转换为多少天或者多少个小时和多少年
            String time = DateUtils.getTime(circleClassificationVos.get(i).getCreateAt());
            circleClassificationVos.get(i).setCreateAt(time);
        }

        return circleClassificationVos;
    }

    @Override
    public List<CircleClassificationVo> queryImagesOrVideos(int type, Paging paging, int userId) {
        List<CircleClassificationVo> circles = circleMapper.queryImagesOrVideos(type, getPaging(paging));
        for (int i = 0; i < circles.size(); i++) {
            //得到图片组
            String[] strings = circleMapper.selectImgByPostId(circles.get(i).getId());
            circles.get(i).setImg(strings);

            //得到看过这个帖子人的头像
            String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
            circles.get(i).setGiveAvatar(strings1);

            //等于0在用户没有到登录的情况下 直接设置没有点赞
            if (userId != 0) {
                //查看我是否关注了此人
                int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                if (i1 > 0) {
                    circles.get(i).setWhetherAttention(1);
                }

                //查询是否对帖子点了赞   0没有 1有
                Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                if (integer > 0) {
                    circles.get(i).setWhetherGive(1);
                }
            }

            //将时间戳转换为多少天或者多少个小时和多少年
            String time = DateUtils.getTime(circles.get(i).getCreateAt());
            circles.get(i).setCreateAt(time);
        }

        return circles;
    }

    @Override
    public List<CircleClassificationVo> queryCircleOfVideos(int id,Paging paging, int userId) throws ParseException {
        //List存储数据顺序与插入数据顺序一致，存在先进先出的概念。
        List<CircleClassificationVo> classificationVoList = new ArrayList<>();
        //查询单个圈子
        CircleClassificationVo circleClassificationVo = circleMapper.selectSingleCircle(id);
        if (circleClassificationVo == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该帖子不存在");
        }
        //等于0在用户没有到登录的情况下 直接设置没有点赞
        if (userId == 0) {
            circleClassificationVo.setWhetherGive(0);
        } else {
            //查看我是否关注了此人
            int i1 = attentionMapper.queryWhetherAttention(userId, circleClassificationVo.getUId());
            if (i1 > 0) {
                circleClassificationVo.setWhetherAttention(1);
            }

            //查询是否对帖子点了赞   0没有 1有
            Integer integer = circleGiveMapper.whetherGive(userId, circleClassificationVo.getId());
            if (integer == 0) {
                circleClassificationVo.setWhetherGive(0);
            } else {
                circleClassificationVo.setWhetherGive(1);
            }
        }

        //将时间戳转换为多少天或者多少个小时和多少年
        String time = DateUtils.getTime(circleClassificationVo.getCreateAt());
        circleClassificationVo.setCreateAt(time);

        classificationVoList.add(circleClassificationVo);

        //查询所有视频帖子
        List<CircleClassificationVo> classificationVos1 = circleMapper.queryImagesOrVideos(1, getPaging(paging));
        //去除一样的
        List<CircleClassificationVo> classificationVos = classificationVos1.stream().filter(u -> u.getId() != circleClassificationVo.getId()).collect(Collectors.toList());
        for (int i = 0; i < classificationVos.size(); i++) {
            //在用户登录的情况下 增加帖子浏览记录
            if (userId != 0) {
                //得到上一次观看帖子的时间
                Browse browse = new Browse();
                String s = browseMapper.selectCreateAt(id, userId);
                if (s == null) {
                    //增加浏览记录
                    browse.setCreateAt(System.currentTimeMillis() / 1000 + "");
                    browse.setUId(userId);
                    browse.setZqId(id);
                    browse.setType(1);
                    //增加浏览记录
                    int j = browseMapper.addBrowse(browse);
                    if (j <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR, "增加浏览记录错误");
                    }

                    //修改帖子浏览数量
                    int i1 = circleMapper.updateBrowse(id);
                    if (i1 <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                } else {
                    //得到过去时间和现在的时间是否相隔1440分钟 如果相隔了 就添加新的浏览记录
                    long minutesApart = TimeUtil.getMinutesApart(s);
                    if (minutesApart >= 1440) {
                        //修改帖子浏览数量
                        int i1 = circleMapper.updateBrowse(id);
                        if (i1 <= 0) {
                            throw new ApplicationException(CodeType.SERVICE_ERROR);
                        }

                    }
                }

            }

            //等于0在用户没有到登录的情况下 直接设置没有点赞
            if (userId == 0) {
                circleClassificationVo.setWhetherGive(0);
            } else {
                //查看我是否关注了此人
                int i1 = attentionMapper.queryWhetherAttention(userId, circleClassificationVo.getUId());
                if (i1 > 0) {
                    circleClassificationVo.setWhetherAttention(1);
                }

                //查询是否对帖子点了赞   0没有 1有
                Integer integer = circleGiveMapper.whetherGive(userId, circleClassificationVo.getId());
                if (integer == 0) {
                    circleClassificationVo.setWhetherGive(0);
                } else {
                    circleClassificationVo.setWhetherGive(1);
                }
            }

            classificationVoList.add(classificationVos.get(i));

        }

        return classificationVoList;
    }

    @Override
    public CircleClassificationVo querySingleCircle(int id, int userId,Paging paging) throws ParseException {
        //查询单个圈子
        CircleClassificationVo circleClassificationVo = circleMapper.selectSingleCircle(id);
        if (circleClassificationVo == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "该圈子的帖子不存在");
        }

        //在用户登录的情况下 增加帖子浏览记录
        if (userId != 0) {
            //得到上一次观看帖子的时间
            Browse browse = new Browse();
            String s = browseMapper.selectCreateAt(id, userId);
            if (s == null) {
                //增加浏览记录
                browse.setCreateAt(System.currentTimeMillis() / 1000 + "");
                browse.setUId(userId);
                browse.setZqId(id);
                browse.setType(1);
                //增加浏览记录
                int i = browseMapper.addBrowse(browse);
                if (i <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "增加浏览记录错误");
                }

                //修改帖子浏览数量
                int i1 = circleMapper.updateBrowse(id);
                if (i1 <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            } else {
                //得到过去时间和现在的时间是否相隔1440分钟 如果相隔了 就添加新的浏览记录
                long minutesApart = TimeUtil.getMinutesApart(s);
                if (minutesApart >= 1440) {
                    //修改帖子浏览数量
                    int i1 = circleMapper.updateBrowse(id);
                    if (i1 <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }

                }
            }

        }

        //得到图片组
        String[] strings = circleMapper.selectImgByPostId(circleClassificationVo.getId());
        circleClassificationVo.setImg(strings);

        //得到看过帖子人的头像
        String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circleClassificationVo.getId());
        circleClassificationVo.setGiveAvatar(strings1);

        //等于0在用户没有到登录的情况下 直接设置没有点赞
        if (userId == 0) {
            circleClassificationVo.setWhetherGive(0);
        } else {
            //查看我是否关注了此人
            int i1 = attentionMapper.queryWhetherAttention(userId, circleClassificationVo.getUId());
            if (i1 > 0) {
                circleClassificationVo.setWhetherAttention(1);
            }

            //查询是否对帖子点了赞   0没有 1有
            Integer integer = circleGiveMapper.whetherGive(userId, circleClassificationVo.getId());
            if (integer == 0) {
                circleClassificationVo.setWhetherGive(0);
            } else {
                circleClassificationVo.setWhetherGive(1);
            }
        }

        //将时间戳转换为多少天或者多少个小时和多少年
        String time = DateUtils.getTime(circleClassificationVo.getCreateAt());
        circleClassificationVo.setCreateAt(time);

        //如果为图文贴，继续推荐相关帖子
        if (circleClassificationVo.getType() == 0) {
            //根据圈子分类和点赞推荐，先后排序
            //查询当前帖子所在圈子的话题分类
            int topic = communityMapper.queryCommunityFromTopic(circleClassificationVo.getTagId());
            //查询该分类下的帖子根据点赞数排序
            //排除查看的当前帖子
            List<CircleClassificationVo> recommends = circleMapper.queryCirclesByTopic(circleClassificationVo.getId(),topic,getPaging(paging));
            for (int i = 0; i < recommends.size(); i++) {
                //得到图片组
                String[] img = circleMapper.selectImgByPostId(recommends.get(i).getId());
                recommends.get(i).setImg(img);
                if (userId != 0) {
                    //查看我是否关注了此人
                    int i2 = attentionMapper.queryWhetherAttention(userId, recommends.get(i).getUId());
                    if (i2 > 0) {
                        recommends.get(i).setWhetherAttention(1);
                    }

                    //查询是否对帖子点了赞   0没有 1有
                    Integer give = circleGiveMapper.whetherGive(userId, recommends.get(i).getId());
                    if (give == 0) {
                        recommends.get(i).setWhetherGive(0);
                    } else {
                        recommends.get(i).setWhetherGive(1);
                    }
                }
            }

            circleClassificationVo.setRecommends(recommends);
        }


        return circleClassificationVo;
    }

    @Override
    public List<CircleClassificationVo> queryReferenceCircles(int userId, Paging paging) {
        /**
         * 1.未登录状态默认推荐热门
         * 2.登录状态，未匹配：推荐近期浏览并点赞的分类 权重以分类为主
         * 3.登录状态，已匹配：权重以匹配为主，浏览点赞为辅推荐
         * 4.进入帖子详情：权重以分类为主，浏览点赞为辅推荐
         */
        List<CircleClassificationVo> circles = null;
        //登录状态
        if(userId != 0) {
            //查询是否参与了匹配
            String needs = sameCityMapper.queryMatchingNeedsByUserId(userId);
            //未匹配(推荐近期浏览并点赞的分类 权重以分类为主)
            if (needs == null) {
                circles = circleMapper.queryReferenceLoggedAndNotMatch(userId,getPaging(paging));
            }else {//已匹配(权重以匹配为主，浏览点赞为辅推荐)
                switch (needs.replace("\"","")) {
                    case "找货源":
                        circles = circleMapper.queryReferenceLoggedAndMatch("供货",getPaging(paging));
                        break;
                    case "找人才":
                        circles = circleMapper.queryReferenceLoggedAndMatch("实习",getPaging(paging));
                        break;
                    case "认识人脉":
                        circles = circleMapper.queryReferenceLoggedAndMatch("找人才",getPaging(paging));
                        break;
                    case "找合作":
                        circles = circleMapper.queryReferenceLoggedAndMatch("找流量",getPaging(paging));
                        break;
                    case "实习":
                        circles = circleMapper.queryReferenceLoggedAndMatch("找人才",getPaging(paging));
                        break;
                    case "供货":
                        circles = circleMapper.queryReferenceLoggedAndMatch("找货源",getPaging(paging));
                        break;
                    case "学电商":
                        circles = circleMapper.queryReferenceCircles(getPaging(paging));
                        break;
                    case "找流量":
                        circles = circleMapper.queryReferenceLoggedAndMatch("找合作",getPaging(paging));
                        break;
                    default:
                        throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
                }
            }
        }else {//未登录默认推荐热门
            circles = circleMapper.queryReferenceCircles(getPaging(paging));
        }
        for (int i = 0; i < circles.size(); i++) {

            //得到图片组
            String[] strings = circleMapper.selectImgByPostId(circles.get(i).getId());
            circles.get(i).setImg(strings);

            //得到点过赞人的头像
            String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
            circles.get(i).setGiveAvatar(strings1);


            if (userId != 0) {
                //查看我是否关注了此人
                int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                if (i1 > 0) {
                    circles.get(i).setWhetherAttention(1);
                }

                //查询是否对帖子点了赞   0没有 1有
                Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                if (integer > 0) {
                    circles.get(i).setWhetherGive(1);
                }
            }

            //将时间戳转换为多少天或者多少个小时和多少年
            String time = DateUtils.getTime(circles.get(i).getCreateAt());
            circles.get(i).setCreateAt(time);
        }

        //混乱的意思
        Collections.shuffle(circles);

        return circles;
    }

    @Override
    public Map<String,Object> queryHotTopic(int userId) {
        Map<String, Object> map = new HashMap<>();
        //热门话题
        List<HotTopicVo> hotTopicVos = circleMapper.queryHotTopic();

        //我创建的圈子数量
        int myCircleCount = 0;
        //我加入的圈子数量
        int joinedCircleCount = 0;
        if (userId != 0) {
            myCircleCount = circleMapper.myCircleCount(userId);
            joinedCircleCount = circleMapper.circleJoinedCount(userId);
        }

        map.put("hotTopicVos",hotTopicVos);
        map.put("myCircleCount",myCircleCount);
        map.put("joinedCircleCount",joinedCircleCount);

        return map;
    }

    @Override
    public List<CommunityTopic> queryAllTopic() {
        return circleMapper.queryAllTopic();
    }

    @Override
    public List<CircleVo> queryCommunityByTopic(int userId, int topicId, Paging paging) {
        List<CircleVo> circleVos = circleMapper.queryCommunityByTopic(topicId,getPaging(paging));
        for (CircleVo circleVo : circleVos) {
            //查询圈子人数
            circleVo.setMemberCount(circleMapper.countCircleJoined(circleVo.getId()));

            if (userId != 0) {
                //查询用户是否加入了圈子
                int isJoined = circleMapper.queryWhetherJoinedCircle(circleVo.getId(),userId);
                if (isJoined != 0){
                    circleVo.setWhetherJoined(1);
                }
            }else {
                circleVo.setWhetherJoined(0);
            }

        }
        return circleVos;
    }

    @Override
    public void addCircle(Community community,int honoredLevel) throws ParseException {
        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(community.getCommunityName(), token);
        if ("87014".equals(identifyTextContent)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "内容违规");
        }

        String identifyTextContent1 = ConstantUtil.identifyText(community.getIntroduce(), token);
        if ("87014".equals(identifyTextContent1)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "内容违规");
        }

        String identifyTextContent2 = ConstantUtil.identifyText(community.getAnnouncement(), token);
        if ("87014".equals(identifyTextContent2)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "内容违规");
        }

        //查询用户已创建圈子数量
        Integer circleCount = circleMapper.myCircleCount(community.getUserId());
        if (honoredLevel == 0) {
            if (circleCount >= HonoredLevel.HONORED_LEVEL_TONG.getCircleNum()) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"当前等级创建圈子数量已达到上限");
            }
        }else if (honoredLevel == 1) {
            if (circleCount >= HonoredLevel.HONORED_LEVEL_YIN.getCircleNum()) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"当前等级创建圈子数量已达到上限");
            }
        }else if (honoredLevel == 2) {
            if (circleCount >= HonoredLevel.HONORED_LEVEL_JIN.getCircleNum()) {
                throw new ApplicationException(CodeType.SERVICE_ERROR,"当前等级创建圈子数量已达到上限");
            }
        }else {
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        //查找是否有同名圈子
        int nameExist = circleMapper.querySameNameExist(community.getCommunityName());
        if (nameExist > 0){
            throw new ApplicationException(CodeType.RESOURCES_EXISTING, "该圈子名称已存在");
        }

        String key = "MyCirclesSquare::" + community.getUserId();
        if (redisTemplate.hasKey(key)) {
            redisConfig.remove(key);
        }

        Tag tag = new Tag();
        tag.setTId(0);
        tag.setTagName(community.getCommunityName());
        tag.setCreateAt(System.currentTimeMillis() / 1000 + "");
        tag.setType(1);

        community.setCreateAt(System.currentTimeMillis() / 1000 + "");
        int i = tagMapper.addTag(tag);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "添加失败");
        }

        community.setTagId(tag.getId());
        int i1 = circleMapper.addCommunity(community);
        if (i1 <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "添加圈子信息失败");
        }

        //添加圈子人数 （创建人）
        CommunityUser communityUser = new CommunityUser();
        communityUser.setCreateAt(System.currentTimeMillis() / 1000 + "");
        communityUser.setCommunityId(community.getId());
        communityUser.setUserId(community.getUserId());
        int i4 = circleMapper.addCommunityUser(communityUser);
        if (i4 <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "添加圈子人错误");
        }

        int i2 = tagMapper.addTagHaplont(tag.getId(), 1);
        if (i2 <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        int i3 = tagMapper.addTagHaplont(tag.getId(), 2);
        if (i3 <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
    }

    @Override
    public void publishingCircles(Circle circle, String imgUrl) throws Exception {

        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(circle.getContent(), token);
        if ("87014".equals(identifyTextContent)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "内容违规");
        }

        String time = String.valueOf(System.currentTimeMillis() / 1000);
        circle.setCreateAt(time);

        //如果状态等于1说明发布的是视频
        if (circle.getType() == 1) {
            String videoCover = FfmpegUtil.getVideoCover(circle.getVideo());
            circle.setCover(videoCover);
        } else {
            circle.setCover(imgUrl.split(",")[0]);
        }

        //添加圈子
        int i = circleMapper.addCirclePost(circle);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        //如果状态等于0说明发布的是图文
        if (circle.getType() == 0) {
            String[] split = imgUrl.split(",");
            if (split.length > 9) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "最多只能上传9张图片！");
            }

            int addImg = circleMapper.addImg(circle.getId(), split, time, 1);
            if (addImg <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
        }

        //为用户添加荣誉积分
        HonoredPointsUtil.addHonoredPoints(circle.getUserId(), PointsType.HONORED_POINTS_POST,0,time);

    }

    @Override
    public CommunityVo selectCommunityCategoryId(int id, int userId) {
        //查询圈子信息
        CommunityVo communityVo = communityMapper.selectCommunityCategoryId(id);
        if (communityVo == null) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "没有该圈子");
        }

        //得到圈子总人数
        int i = communityMapper.selectTotalNumberCirclesById(communityVo.getId());
        communityVo.setTotalNumberCircles(i);

        //查询圈子的用户头像
        String[] strings = communityMapper.selectCirclesAvatars(communityVo.getId());
        communityVo.setAvatar(strings);

        List<CommunityUser> communities = communityMapper.queryCommunityById(communityVo.getId());
        //匹配是否存在这个圈子
        communities.stream().filter(u -> u.getUserId() == userId).forEach(u -> {
            communityVo.setWhetherThere(1);
        });
        //如果排行榜开启，返回统计后的参与排行用户的头像（最多展示8条）
        if (communityVo.getRankingSwitch() == 1) {
            communityVo.setUserRankVos(communityMapper.queryCircleRanking(id));
        }

        //得到单元体导航栏
        List<Haplont> haplonts = communityMapper.selectHaplontByTagId(id);
        communityVo.setHaplontList(haplonts);
        return communityVo;
    }


    @Override
    public int joinCircle(CommunityUser communityUser) {
        communityUser.setCreateAt(System.currentTimeMillis() / 1000 + "");

        //查询是否存在圈子里面 如果存在在调用接口就是退出圈子
        int i1 = communityMapper.queryWhetherThereCircle(communityUser.getCommunityId(), communityUser.getUserId());
        if (i1 > 0) {
            //退出圈子
            int i = communityMapper.exitGroupChat(communityUser.getCommunityId(), communityUser.getUserId());
            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "退出圈子失败");
            }
            return i;
        }

        int i = communityMapper.joinCircle(communityUser);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "加入圈子失败!");
        }
        return i;
    }

    @Override
    public List<CircleClassificationVo> queryClickUnitNavigationBar(int typeId, int userId, int tagId, Paging paging) {
        List<CircleClassificationVo> circles = null;

        String str = "";

        //查询最新的数据
        if (typeId == 1) {
            str = "order by a.create_at desc " + getPaging(paging);
            circles = circleMapper.selectPostsBasedTagIdCircleTwo(tagId, str);
        }

        //查询最热的数据
        if (typeId == 2) {
            str = "order by a.browse desc " + getPaging(paging);
            circles = circleMapper.selectPostsBasedTagIdCircleTwo(tagId, str);
        }

        //其他导航栏点击
        if (typeId > 2) {
            circles = circleMapper.queryPostByHaplontType(typeId, getPaging(paging), tagId);
        }

        if (circles.size() != 0) {
            for (int i = 0; i < circles.size(); i++) {
                //得到图片组
                String[] strings = circleMapper.selectImgByPostId(circles.get(i).getId());
                circles.get(i).setImg(strings);

                //得到点过赞人的头像
                String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
                circles.get(i).setGiveAvatar(strings1);

                //得到点赞数量
                Integer integer1 = circleGiveMapper.selectGiveNumber(circles.get(i).getId());
                circles.get(i).setGiveNumber(integer1);

                //等于0在用户没有到登录的情况下 直接设置没有点赞
                if (userId == 0) {
                    circles.get(i).setWhetherGive(0);
                    circles.get(i).setWhetherAttention(0);
                } else {
                    //查看我是否关注了此人
                    int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                    if (i1 > 0) {
                        circles.get(i).setWhetherAttention(1);
                    }

                    //查询是否对帖子点了赞   0没有 1有
                    Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                    if (integer > 0) {
                        circles.get(i).setWhetherGive(1);
                    }
                }

                //得到帖子评论数量
                Integer integer2 = commentMapper.selectCommentNumber(circles.get(i).getId());
                circles.get(i).setNumberPosts(integer2);

                //将时间戳转换为多少天或者多少个小时和多少年
                circles.get(i).setCreateAt(DateUtils.getTime(circles.get(i).getCreateAt()));
            }
        }


        return circles;
    }


    @Override
    public Map<String, Object> fundCircles(int userId, Paging paging) {
        Map<String, Object> map = new HashMap<>();
        //查询热门的圈子
        List<CircleVo> circleVos = circleMapper.queryPopularCircles("limit 0,3");
        for (CircleVo circleVo : circleVos) {
            //根据圈子对应的标签id查询封面和id
            //List<CircleImgIdVo> circleVos1 = circleMapper.queryCoveId(circleVo.getTagId());
            //circleVo.setCircleVoList(circleVos1);
            circleVo.setMemberCount(circleMapper.countCircleJoined(circleVo.getId()));
            //查询用户是否加入了圈子
            int isJoined = circleMapper.queryWhetherJoinedCircle(circleVo.getId(),userId);
            if (isJoined != 0){
                circleVo.setWhetherJoined(1);
            }
        }
        //查询精选帖子
        List<CircleClassificationVo> circles = circleMapper.queryFeatured(getPaging(paging));
        if (circles != null){
            for (int i = 0; i < circles.size(); i++) {
                //得到图片组
                String[] strings = circleMapper.selectImgByPostId(circles.get(i).getId());
                circles.get(i).setImg(strings);

                //得到点过赞人的头像
                String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
                circles.get(i).setGiveAvatar(strings1);

                //得到点赞数量
                Integer integer1 = circleGiveMapper.selectGiveNumber(circles.get(i).getId());
                circles.get(i).setGiveNumber(integer1);

                //等于0在用户没有到登录的情况下 直接设置没有点赞
                if (userId == 0) {
                    circles.get(i).setWhetherGive(0);
                    circles.get(i).setWhetherAttention(0);
                } else {
                    //查看我是否关注了此人
                    int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                    if (i1 > 0) {
                        circles.get(i).setWhetherAttention(1);
                    }

                    //查询是否对帖子点了赞   0没有 1有
                    Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                    if (integer > 0) {
                        circles.get(i).setWhetherGive(1);
                    }
                }

                //得到帖子评论数量
                circles.get(i).setNumberPosts(commentMapper.selectCommentNumber(circles.get(i).getId()));

                //将时间戳转换为多少天或者多少个小时和多少年
                circles.get(i).setCreateAt(DateUtils.getTime(circles.get(i).getCreateAt()));
            }
        }
        map.put("hotCircleVos",circleVos);
        map.put("featuredCircle",circles);
        return map;
    }

    @Override
    public Map<String, Object> myCircles(int userId, Paging paging) {
        //创建的圈子
        Map<String, Object> map = new HashMap<>();
        //查看缓存是否存在 如果存在就查询缓存中的数据，否则查询数据库加入缓存
//        if (redisTemplate.hasKey("MyCirclesSquare::" + userId)) {
//            int pages = (paging.getPage() - 1) * paging.getLimit();
//            //查询缓存数据
//            List range = redisConfig.getList("MyCirclesSquare::" + userId, pages, paging.getPage() * paging.getLimit() - 1);
//            int createSize = (int) redisConfig.getString("myCreateCircleCount::" + userId);
//            int joinSize = (int) redisConfig.getString("joinedCircleCount::" + userId);
//            map.put("circle",range);
//            map.put("createSize",createSize);
//            map.put("joinSize",joinSize);
//        } else {
            //查询我创建的圈子
            List<CircleVo> createCircleVos = circleMapper.myCircleAndCircleJoined(userId, getPaging(paging));
            for (CircleVo createCircleVo : createCircleVos) {
                //根据圈子对应的标签id查询封面和id
                List<CircleImgIdVo> circleVos1 = circleMapper.queryCoveId(createCircleVo.getTagId());
                createCircleVo.setCircleVoList(circleVos1);
                createCircleVo.setMemberCount(circleMapper.countCircleJoined(createCircleVo.getId()));
            }

//            //存入redis缓存
//            if (createCircleVos.size() != 0) {
//                redisTemplate.opsForList().rightPushAll("MyCirclesSquare::" + userId, createCircleVos);
//                redisConfig.setString("myCreateCircleCount::" + userId, myCircleCount);
//                redisConfig.setString("joinedCircleCount::" + userId, joinedCircleCount);
//            }

            map.put("circle",createCircleVos);
//        }

        return map;
    }

    @Override
    public Map<String, Object> joinedCircles(int userId, Paging paging) {
        Map<String, Object> map = new HashMap<>();
        //查看缓存是否存在 如果存在就查询缓存中的数据，否则查询数据库加入缓存
//        if (redisTemplate.hasKey("JoinedCircles::" + userId)) {
//            int pages = (paging.getPage() - 1) * paging.getLimit();
//            //查询缓存数据
//            List range = redisConfig.getList("JoinedCircles::" + userId, pages, paging.getPage() * paging.getLimit() - 1);
//            map.put("joinedCircle",range);
//        } else {
        //查询我加入的圈子
        List<CircleVo> joinedCircleVos = circleMapper.circleJoined(userId, getPaging(paging));
//        for (CircleVo createCircleVo : joinedCircleVos) {
//            //根据圈子对应的标签id查询封面和id
//            List<CircleImgIdVo> circleVos1 = circleMapper.queryCoveId(createCircleVo.getTagId());
//            createCircleVo.setCircleVoList(circleVos1);
//            createCircleVo.setMemberCount(circleMapper.countCircleJoined(createCircleVo.getId()));
//        }
        Iterator<CircleVo> it = joinedCircleVos.iterator();
        while (it.hasNext()){
            CircleVo circleVo = it.next();
            //根据圈子对应的标签id查询封面和id
            List<CircleImgIdVo> circleVos1 = circleMapper.queryCoveId(circleVo.getTagId());
            circleVo.setCircleVoList(circleVos1);
            circleVo.setMemberCount(circleMapper.countCircleJoined(circleVo.getId()));
            if (circleVo.getUserId() == userId){
                it.remove();
            }
        }
            //存入redis缓存
//            if (joinedCircleVos.size() != 0) {
//                redisTemplate.opsForList().rightPushAll("JoinedCircles::" + userId, joinedCircleVos);
//            }

//      joinedCircleVos.stream().filter(j -> j.getUserId() != userId).collect(Collectors.toList());
        map.put("joinedCircle",joinedCircleVos);
//          }
        return map;
    }

    @Override
    public List<Community> queryOfficialCircleList(Paging paging) {
        return circleMapper.queryOfficialCircleList(getPaging(paging));
    }

    @Override
    public void updateCircle(Community community) {
        String key = "MyCirclesSquare::" + community.getUserId();

        //查看该缓存是否存在
        if (redisTemplate.hasKey(key)) {
            //删除缓存
            redisConfig.remove(key);
        }
//        else{
//            throw new ApplicationException(CodeType.SERVICE_ERROR,"redis缓存异常！");
//        }

        int i = communityMapper.updateCircle(community);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
        }
        //修改圈子对应的标签名
        int j = tagMapper.updateTagNameForCircle(community.getCommunityName(), community.getTagId());
        if (j <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改失败");
        }
    }

    @Override
    public void memberManagement(int communityId, int userId) {
        int i = communityMapper.exitGroupChat(communityId, userId);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "踢出失败！");
        }
    }

    @Override
    public List<UserVo> queryCircleMembers(int communityId,int userId) {
        List<UserVo> userVos = circleMapper.queryCircleMembers(communityId);

        //排除出来的用户id不等userId的数据
        List<UserVo> collect = userVos.stream().filter(u -> u.getId() != userId).collect(Collectors.toList());

        return collect;
    }

    @Override
    public void addTagHaplont(int tagId, String hName) {

        Haplont haplont = new Haplont();
        haplont.setCreateAt(System.currentTimeMillis() / 1000 + "");
        haplont.setHName(hName);
        int i = circleMapper.addHaplont(haplont);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        int i1 = circleMapper.addTagHaplont(tagId, haplont.getId());
        if (i1 <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "添加单元体错误");
        }
    }

    @Override
    public void deletePosts(int id,int tagId) {
        int i = communityMapper.deleteCircle(id);
        if(i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除圈子失败");
        }
        List<CircleClassificationVo> circleClassificationVos = circleMapper.selectPostsBasedTagIdCircleTwo(tagId, "limit 0,10");
        if (circleClassificationVos.size() != 0){
            int j = circleMapper.deletePosts(tagId);
            if(j <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"删除圈子内帖子失败");
            }
        }
    }

    @Override
    public void deleteCircles(int id) {
        int i = circleMapper.deleteCircles(id);
        if(i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"删除圈子帖子失败");
        }
    }

    @Override
    public void TopPosts(int id,int userId) {
        //先取消所有用户创建圈子的置顶
        int i = communityMapper.cancelTopCircle(userId);
        if(i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"取消置顶圈子失败");
        }
        //置顶用户创建的圈子
        int j = communityMapper.topCircle(id);
        if(j <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"置顶圈子失败");
        }
    }

    @Override
    public List<CommunitySearchVo> queryCirclesByName(String communityName) {
        return circleMapper.queryCirclesByName(communityName);
    }

    @Override
    public List<CircleVo> queryHotCircleList(int userId,Paging paging) {
        //查询热门的圈子
        List<CircleVo> circleVos = circleMapper.queryPopularCircles(getPaging(paging));
        for (CircleVo circleVo : circleVos) {
            circleVo.setMemberCount(circleMapper.countCircleJoined(circleVo.getId()));
            //查询用户是否加入了圈子
            int isJoined = circleMapper.queryWhetherJoinedCircle(circleVo.getId(),userId);
            if (isJoined != 0){
                circleVo.setWhetherJoined(1);
            }
        }
        return circleVos;
    }

    @Override
    public ResultUtil setCircleRanking(int rankingSwitch, String rankingRules, int id) {
        int i = communityMapper.setCircleRanking(rankingSwitch,rankingRules,id);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR,"圈子排行设置失败");
        }
        return ResultUtil.success(i);
    }

    @Override
    public List<UserRankVo> queryCircleRanking(int communityId) {
        //根据圈子id查询该圈子内排行榜单
        return communityMapper.queryCircleRanking(communityId);
    }


}
