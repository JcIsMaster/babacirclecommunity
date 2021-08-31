package com.example.babacirclecommunity.my.service.impl;

import com.example.babacirclecommunity.circle.dao.AttentionMapper;
import com.example.babacirclecommunity.circle.dao.CircleGiveMapper;
import com.example.babacirclecommunity.circle.dao.CircleMapper;
import com.example.babacirclecommunity.circle.dao.CommentMapper;
import com.example.babacirclecommunity.circle.vo.*;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.ConstantUtil;
import com.example.babacirclecommunity.common.utils.DateUtils;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.TimeUtil;
import com.example.babacirclecommunity.home.entity.SearchHistory;
import com.example.babacirclecommunity.learn.vo.DryGoodsVo;
import com.example.babacirclecommunity.learn.vo.PublicClassTagVo;
import com.example.babacirclecommunity.learn.vo.QuestionVo;
import com.example.babacirclecommunity.my.dao.MyMapper;
import com.example.babacirclecommunity.my.entity.ComplaintsSuggestions;
import com.example.babacirclecommunity.my.service.IMyService;
import com.example.babacirclecommunity.my.vo.CommentsDifferentVo;
import com.example.babacirclecommunity.my.vo.GreatDifferentVo;
import com.example.babacirclecommunity.my.vo.PeopleCareAboutVo;
import com.example.babacirclecommunity.resource.dao.ResourceMapper;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.user.dao.UserMapper;
import com.example.babacirclecommunity.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private CircleGiveMapper circleGiveMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    /**
     * 得到分页
     * @param paging 分页
     * @return
     */
    public String getPaging(Paging paging) {
        int page = (paging.getPage() - 1) * paging.getLimit();
        return "limit " + page + "," + paging.getLimit() + "";
    }


    @Override
    public List<PeopleCareAboutVo> queryPeopleCareAbout(Paging paging, int userId) {

        //查询我关注人
        List<PeopleCareAboutVo> peopleCareAboutVos = myMapper.queryPeopleCareAbout(userId, getPaging(paging));
        for (PeopleCareAboutVo peopleCareAboutVo : peopleCareAboutVos) {
            int i1 = attentionMapper.queryWhetherAttention(peopleCareAboutVo.getId(), userId);
            if (i1 > 0) {
                peopleCareAboutVo.setWhetherFocus(2);
            }
            else {
                peopleCareAboutVo.setWhetherFocus(1);
            }
        }

        return peopleCareAboutVos;
    }

    @Override
    public List<PeopleCareAboutVo> queryFan(Paging paging, int userId) {

        //查询我的粉丝
        List<PeopleCareAboutVo> queryFan = myMapper.queryFan(userId, getPaging(paging));
        for (PeopleCareAboutVo peopleCareAboutVo : queryFan) {
            int i1 = attentionMapper.queryWhetherAttention(userId, peopleCareAboutVo.getId());
            if (i1 > 0) {
                peopleCareAboutVo.setWhetherFocus(2);
            }
            else {
                peopleCareAboutVo.setWhetherFocus(0);
            }

        }

        return queryFan;
    }

    @Override
    public int addComplaintsSuggestions(ComplaintsSuggestions complaintsSuggestions) {
        complaintsSuggestions.setCreateAt(System.currentTimeMillis() / 1000 + "");
        int i = myMapper.addComplaintsSuggestions(complaintsSuggestions);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "投诉错误");
        }
        return i;
    }

    @Override
    public void clickInterfaceHeadImageEnter(int bUserId, int userId) {
        //得到上次访问的时间进行比较
        String[] strings = myMapper.queryCreateAt(userId, bUserId);
        if (strings.length !=0) {
            //判断两个时间是否同一天
            boolean sameDate = TimeUtil.isSameDate(System.currentTimeMillis() / 1000, Long.parseLong(strings[0]));
            if (!sameDate) {
                //如果是自己观看自己则不添加观看记录数据
                if (bUserId != userId) {
                    if (userId != 0) {
                        int i = myMapper.addViewingRecord(bUserId, userId, System.currentTimeMillis() / 1000 + "");
                        if (i <= 0) {
                            throw new ApplicationException(CodeType.SERVICE_ERROR);
                        }
                    }
                }
            }
        } else {

            //如果是自己观看自己则不添加观看记录数据
            if (bUserId != userId) {
                if (userId != 0) {
                    int i = myMapper.addViewingRecord(bUserId, userId, System.currentTimeMillis() / 1000 + "");
                    if (i <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                }
            }
        }


    }

    @Override
    public Map<String, Object> queryPeopleWhoHaveSeenMe(int userId, Paging paging) {

        Map<String, Object> map = new HashMap<>(2);

        //查询看过我的用户信息
        List<User> users = myMapper.queryPeopleWhoHaveSeenMe(userId, getPaging(paging));
        users.forEach(u -> {
            //得到当前时间戳和过去时间戳比较相隔多少分钟或者多少小时或者都少天或者多少年
            String time = DateUtils.getTime(u.getCreateAt());
            u.setCreateAt(time);
        });
        //查询今天看过我的人数
        int integer = myMapper.queryPeopleWhoHaveSeenMeAvatar(userId);
        map.put("users", users);
        map.put("integer", integer);

        return map;
    }

    @Override
    public int updateUserInformation(User user) throws ParseException {
        //获取token
        String token1 = ConstantUtil.getToken();
        String identifyTextContent1 = ConstantUtil.identifyText(user.getUserName(), token1);
        if ("87014".equals(identifyTextContent1)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "内容违规");
        }

        //获取token
        String token2 = ConstantUtil.getToken();
        String identifyTextContent2 = ConstantUtil.identifyText(user.getIntroduce(), token2);
        if ("87014".equals(identifyTextContent2)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "内容违规");
        }

        return myMapper.updateUserMessage(user);
    }

    static <T> Predicate<T> distinctByKey1(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public List<CommentsDifferentVo> queryCommentsDifferentModulesBasedStatus(Paging paging, Integer userId) {

        //查询评论过的圈子信息
        List<CommentsDifferentVo> commentsDifferentVos = myMapper.queryCommentsDifferentCircle(userId, getPaging(paging));


        //查询评论过的干货信息
        List<CommentsDifferentVo> commentsDifferentVos1 = myMapper.queryCommentsDifferentDryGoods(userId, getPaging(paging));


        //查询评论过的提问信息
        List<CommentsDifferentVo> commentsDifferentVos2 = myMapper.queryCommentsDifferentQuestion(userId, getPaging(paging));

        //将三个集合合并为一个集合响应给前端
        return Stream.of(commentsDifferentVos, commentsDifferentVos1, commentsDifferentVos2).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public List<GreatDifferentVo> queryGreatDifferentModulesBasedStatus(Paging paging, Integer userId) {

        //查询点赞过的圈子信息
        List<GreatDifferentVo> greatDifferentVos = myMapper.queryGreatDifferentCircle(userId, getPaging(paging));

        //查询点赞过的提问信息
        List<GreatDifferentVo> greatDifferentVos1 = myMapper.queryGreatDifferentQuestion(userId, getPaging(paging));

        //将两个集合合并为一个集合响应给前端
        return Stream.of(greatDifferentVos, greatDifferentVos1).flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public Object queryFavoritesDifferentModulesAccordingStatus(Paging paging, Integer status, Integer userId) {

        //货源
        if (status == 0) {
            return myMapper.queryFavoritePosts(userId, 0, getPaging(paging));
        }

        //合作
        if (status == 1) {
            return myMapper.queryFavoritePostsCollaborate(userId, 1, getPaging(paging));
        }

        //干货
        if (status == 2) {
            List<DryGoodsVo> dryGoodsVos = myMapper.queryCollectDry(userId, getPaging(paging));
            for (DryGoodsVo s : dryGoodsVos) {
                //将时间戳转换为多少天或者多少个小时和多少年
                String time = DateUtils.getTime(s.getCreateAt());
                s.setCreateAt(time);
            }
            return dryGoodsVos;
        }

        //提问
        if (status == 3) {
            return myMapper.queryCollectQuestion(userId, getPaging(paging));
        }

        //公开课
        if (status == 4) {
            return myMapper.queryCollectPublicClass(userId, getPaging(paging));
        }

        return null;
    }

    @Override
    public List<CircleClassificationVo> queryCheckPostsBeenReadingPastMonth(int userId, int tagsOne, Paging paging) {

        //货源
        if (tagsOne == 1) {
            List<CircleClassificationVo> circleClassificationVos = myMapper.queryCheckPostsBeenReadingPastMonthResource(userId, 12, getPaging(paging));
            for (CircleClassificationVo s : circleClassificationVos) {
                //将时间戳转换为多少天或者多少个小时和多少年
                String time = DateUtils.getTime(s.getCreateAt());
                s.setCreateAt(time);
            }

            return circleClassificationVos;
        }

        //合作
        if (tagsOne == 2) {
            List<CircleClassificationVo> circleClassificationVos = myMapper.queryCheckPostsBeenReadingPastMonthResource(userId, 13, getPaging(paging));
            for (CircleClassificationVo s : circleClassificationVos) {
                //将时间戳转换为多少天或者多少个小时和多少年
                String time = DateUtils.getTime(s.getCreateAt());
                s.setCreateAt(time);
            }
            return circleClassificationVos;
        }

        //圈子
        if (tagsOne == 0) {
            List<CircleClassificationVo> circles = myMapper.queryCheckPostsBeenReadingPastMonth(userId, getPaging(paging));
            for (CircleClassificationVo s : circles) {
                //得到图片组
                String[] strings = circleMapper.selectImgByPostId(s.getId());
                s.setImg(strings);
                //将时间戳转换为多少天或者多少个小时和多少年
                String time = DateUtils.getTime(s.getCreateAt());
                s.setCreateAt(time);
            }
            return circles;
        }
        return null;
    }

    @Override
    public List<CircleClassificationVo> queryMyCirclePost(int userId, Paging paging) {

        List<CircleClassificationVo> circles = circleMapper.queryHavePostedCirclePosts(userId,getPaging(paging));
        for (int i = 0; i < circles.size(); i++) {
            //得到图片组
            String[] strings = circleMapper.selectImgByPostId(circles.get(i).getId());
            circles.get(i).setImg(strings);

            //得到点赞数量
            Integer integer1 = circleGiveMapper.selectGiveNumber(circles.get(i).getId());
            circles.get(i).setGiveNumber(integer1);

            //查询是否对帖子点了赞   0没有 1有
            Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
            if (integer > 0) {
                circles.get(i).setWhetherGive(1);
            }

            //得到帖子评论数量
            Integer integer2 = commentMapper.selectCommentNumber(circles.get(i).getId());
            circles.get(i).setNumberPosts(integer2);
        }
        return circles;
    }

    @Override
    public Map<String, Object> myCircles(int userId, Paging paging) {
        //创建的圈子
        Map<String, Object> map = new HashMap<>();
        //查询我创建的圈子
        List<CircleVo> createCircleVos = circleMapper.myCircleAndCircleJoined(userId, getPaging(paging));
        for (CircleVo createCircleVo : createCircleVos) {
            //根据圈子对应的标签id查询封面和id
            List<CircleImgIdVo> circleVos1 = circleMapper.queryCoveId(createCircleVo.getTagId());
            createCircleVo.setCircleVoList(circleVos1);
            createCircleVo.setMemberCount(circleMapper.countCircleJoined(createCircleVo.getId()));
        }
        //查询我常逛的三个圈子
        List<CircleMostViewVo> circleMostViewVos = circleMapper.queryMostViewedCircle(userId);
        map.put("circleMostView",circleMostViewVos);
        map.put("myCreateCircle",createCircleVos);
        return map;
    }

    @Override
    public List<ResourceClassificationVo> queryMyPostedPosts(int userId,int tagId,Paging paging) {
        //查询我发布的货源
        return resourceMapper.queryMyPostedPosts(userId,12,tagId,getPaging(paging));
    }


}
