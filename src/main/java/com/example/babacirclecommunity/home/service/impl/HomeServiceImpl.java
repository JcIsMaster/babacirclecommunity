package com.example.babacirclecommunity.home.service.impl;

import com.example.babacirclecommunity.circle.dao.AttentionMapper;
import com.example.babacirclecommunity.circle.dao.CircleGiveMapper;
import com.example.babacirclecommunity.circle.dao.CircleMapper;
import com.example.babacirclecommunity.circle.dao.CommentMapper;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.circle.vo.CircleVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.home.dao.SearchRecordMapper;
import com.example.babacirclecommunity.home.entity.SearchHistory;
import com.example.babacirclecommunity.home.service.IHomeService;
import com.example.babacirclecommunity.home.vo.TagVo;
import com.example.babacirclecommunity.tags.dao.TagMapper;
import com.example.babacirclecommunity.tags.entity.Tag;
import com.example.babacirclecommunity.user.dao.UserMapper;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import com.example.babacirclecommunity.user.vo.PersonalUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author JC
 * @date 2021/5/20 13:34
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class HomeServiceImpl implements IHomeService {

    @Autowired
    private SearchRecordMapper searchRecordMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private CircleGiveMapper circleGiveMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private TagMapper tagMapper;

    static <T> Predicate<T> distinctByKey1(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public Object selectAllSearch(int strata, String postingName, int userId, Paging paging) {
        if (postingName.equals("undefined") || postingName == null) {
            return null;
        }

        if (userId != 0 && !postingName.equals("")) {
            //??????????????????
            int i = searchRecordMapper.addSearchRecord(postingName, System.currentTimeMillis() / 1000 + "", userId);
            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "????????????????????????");
            }
        }

        Integer page = (paging.getPage() - 1) * paging.getLimit();
        String sql = "limit " + page + "," + paging.getLimit() + "";

        //?????????
        if (strata == 0) {
            List<PersonalUserVo> personalUserVos = userMapper.queryUserLike(postingName, sql);
            for (int i = 0; i < personalUserVos.size(); i++) {
                //??????????????????????????????
                int i1 = attentionMapper.queryWhetherAttention(userId, personalUserVos.get(i).getId());
                if (i1 > 0) {
                    personalUserVos.get(i).setWhetherAttention(1);
                }
            }
            return personalUserVos;
        }

        //?????????
        if (strata == 1) {
            List<CircleClassificationVo> circles = circleMapper.queryFuzzyCircle(postingName, sql);
            for (int i = 0; i < circles.size(); i++) {
                //???????????????
                String[] strings = circleMapper.selectImgByPostId(circles.get(i).getId());
                circles.get(i).setImg(strings);

                //???????????????????????????
//                String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
//                circles.get(i).setGiveAvatar(strings1);

                //??????????????????
                Integer integer1 = circleGiveMapper.selectGiveNumber(circles.get(i).getId());
                circles.get(i).setGiveNumber(integer1);

                //??????0???????????????????????????????????? ????????????????????????
                if (userId == 0) {
                    circles.get(i).setWhetherGive(0);
                    circles.get(i).setWhetherAttention(0);
                } else {
                    //??????????????????????????????
                    int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                    if (i1 > 0) {
                        circles.get(i).setWhetherAttention(1);
                    }

                    //??????????????????????????????   0?????? 1???
                    Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                    if (integer > 0) {
                        circles.get(i).setWhetherGive(1);
                    }
                }

                //????????????????????????
                Integer integer2 = commentMapper.selectCommentNumber(circles.get(i).getId());
                circles.get(i).setNumberPosts(integer2);
            }
            return circles;
        }
        //????????????
        throw new ApplicationException(CodeType.SERVICE_ERROR);
    }

    @Override
    public Map<String, Object> querySearchRecords(int userId) {

        Map<String, Object> map = new HashMap<>(15);

//        List<TagVo> tagVoList = new ArrayList<>();

        //????????????id??????????????????
        List<SearchHistory> searchHistories = searchRecordMapper.selectSearchRecordByUserId(userId);

        //??????stream?????????????????????
        List<SearchHistory> collect = searchHistories.stream().filter(distinctByKey1(s -> s.getHistoricalContent())).collect(Collectors.toList());

        //???????????? top5
        List<String> hotSearchHistorical = searchRecordMapper.queryHotSearchHistorical();

        //?????????????????? top5
        List<CircleVo> circleVos = circleMapper.queryPopularCircles("limit 0,5");
        /**
         * ????????????????????????
         */
        //??????????????????id????????????????????????
//        List<Tag> tags = tagMapper.queryCorrespondingSecondaryLabel(1);
//        for (int i = 0;i < tags.size();i++){
//            TagVo tagVo = new TagVo();
//            //??????????????????id???????????????????????????????????????????????????
//            int q = circleMapper.countPostsBasedTagIdCircle(tags.get(i).getId());
//            tagVo.setId(tags.get(i).getId());
//            tagVo.setTagName(tags.get(i).getTagName());
//            tagVo.setNum(q);
//            tagVoList.add(tagVo);
//        }

        //?????????
//        List<TagVo> tagVoList1 = new ArrayList<>();

        //?????????????????????????????????????????????????????????????????????
//        List<TagVo> collect1 = tagVoList.stream().sorted(Comparator.comparing(TagVo::getNum).reversed()).collect(Collectors.toList());
//        for (int i = 0;i < collect1.size();i++){
//            int a = i+1;
//            collect1.get(i).setRanking("Top."+a);
//            //????????????
//            tagVoList1.add(collect1.get(i));
//            if(i == 2){
//                break;
//            }
//        }

        map.put("searchHistories", collect);
//        map.put("collect1",tagVoList1);
        map.put("hotSearchHistories",hotSearchHistorical);
        map.put("hotCircle",circleVos);
        return map;

    }

    @Override
    public ResultUtil deleteSearchHistory(int userId) {
        int i = searchRecordMapper.deleteHistorySearch(userId);
        if(i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"??????????????????????????????");
        }
        return ResultUtil.success(i);
    }
}
