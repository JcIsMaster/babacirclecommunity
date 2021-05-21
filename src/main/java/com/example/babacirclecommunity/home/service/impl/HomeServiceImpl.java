package com.example.babacirclecommunity.home.service.impl;

import com.example.babacirclecommunity.circle.dao.AttentionMapper;
import com.example.babacirclecommunity.circle.dao.CircleMapper;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
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
    private AttentionMapper attentionMapper;

    @Autowired
    private TagMapper tagMapper;

    static <T> Predicate<T> distinctByKey1(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public Object selectAllSearch(int strata, String postingName, int userId, Paging paging) {
        if(postingName.equals("undefined")){
            return null;
        }

        if(userId != 0 && !postingName.equals("")){
            //增加搜索记录
            int i = searchRecordMapper.addSearchRecord(postingName, System.currentTimeMillis() / 1000 + "",userId);
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"增加历史记录错误");
            }
        }

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";

        //查用户
        if(strata==0){
            List<PersonalUserVo> personalUserVos = userMapper.queryUserLike(postingName,sql);
            for (int i = 0;i < personalUserVos.size();i++){
                //查看我是否关注了此人
                int i1 = attentionMapper.queryWhetherAttention(userId, personalUserVos.get(i).getId());
                if (i1 > 0){
                    personalUserVos.get(i).setWhetherAttention(1);
                }
            }
            return personalUserVos;
        }

        //查圈子
        if(strata==1){
            List<CircleClassificationVo> classificationVos = circleMapper.queryFuzzyCircle(postingName,sql);
            return classificationVos;
        }
        return null;
    }

    @Override
    public Map<String, Object> querySearchRecords(int userId) {

        Map<String,Object> map = new HashMap<>(15);

        List<TagVo> tagVoList = new ArrayList<>();

        //根据用户id查询历史记录
        List<SearchHistory> searchHistories = searchRecordMapper.selectSearchRecordByUserId(userId);

        //使用stream流根据字段去重
        List<SearchHistory> collect = searchHistories.stream().filter(distinctByKey1(s -> s.getHistoricalContent())).collect(Collectors.toList());

        /**
         * 查询前三名的圈子
         */
        //根据一级标签id查询二级标签信息
//        List<Tag> tags = tagMapper.queryCorrespondingSecondaryLabel(1);
//        for (int i = 0;i < tags.size();i++){
//            TagVo tagVo = new TagVo();
//            //根据二级标签id去帖子表查询统计每个标签有多个帖子
//            int q = circleMapper.countPostsBasedTagIdCircle(tags.get(i).getId());
//            tagVo.setId(tags.get(i).getId());
//            tagVo.setTagName(tags.get(i).getTagName());
//            tagVo.setNum(q);
//            tagVoList.add(tagVo);
//        }

        //前三个
//        List<TagVo> tagVoList1 = new ArrayList<>();

        //根据集合里面的每个标签帖子数量排序，只取前三个
//        List<TagVo> collect1 = tagVoList.stream().sorted(Comparator.comparing(TagVo::getNum).reversed()).collect(Collectors.toList());
//        for (int i = 0;i < collect1.size();i++){
//            int a = i+1;
//            collect1.get(i).setRanking("Top."+a);
//            //取前三个
//            tagVoList1.add(collect1.get(i));
//            if(i == 2){
//                break;
//            }
//        }

        map.put("searchHistories",collect);
//        map.put("collect1",tagVoList1);
        return map;

    }
}
