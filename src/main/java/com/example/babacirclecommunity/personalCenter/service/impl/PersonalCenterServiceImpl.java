package com.example.babacirclecommunity.personalCenter.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.babacirclecommunity.circle.dao.*;
import com.example.babacirclecommunity.circle.entity.Haplont;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.circle.vo.CircleVo;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.learn.dao.DryGoodsGiveMapper;
import com.example.babacirclecommunity.learn.dao.QuestionMapper;
import com.example.babacirclecommunity.learn.vo.QuestionTagVo;
import com.example.babacirclecommunity.my.dao.MyMapper;
import com.example.babacirclecommunity.personalCenter.service.IPersonalCenterService;
import com.example.babacirclecommunity.personalCenter.vo.PersonalVo;
import com.example.babacirclecommunity.resource.dao.ResourceMapper;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.sameCity.dao.SameCityMapper;
import com.example.babacirclecommunity.sameCity.entity.ParameterJson;
import com.example.babacirclecommunity.user.dao.UserMapper;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JC
 * @date 2021/5/20 16:38
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class PersonalCenterServiceImpl implements IPersonalCenterService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private CircleGiveMapper circleGiveMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommunityMapper communityMapper;

    @Autowired
    private MyMapper myMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private SameCityMapper sameCityMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private DryGoodsGiveMapper dryGoodsGiveMapper;

    @Override
    public PersonalVo queryPersonalCenter(int userId,int otherId) {

        PersonalVo personalVo = new PersonalVo();

        //????????????????????????
        PersonalCenterUserVo personalCenterUserVo = userMapper.queryUserById(otherId);

        //??????????????????
        int year = Calendar.getInstance().get(Calendar.YEAR);
        //??????????????????
        if(personalCenterUserVo.getBirthday() != null && !personalCenterUserVo.getBirthday().equals("") && !personalCenterUserVo.getBirthday().equals("null")){
            personalCenterUserVo.setAge(String.valueOf(year - Integer.parseInt(personalCenterUserVo.getBirthday().substring(0,4))));
        }
        else {
            personalCenterUserVo.setAge("??????");
        }

        //?????????????????????
        personalCenterUserVo.setFansNum(myMapper.queryFanCountByUserId(otherId));

        //?????????????????????
        personalCenterUserVo.setAttentionNum(myMapper.queryCareAboutCountByUserId(otherId));

        //???????????????????????????
        int whetherAttention = attentionMapper.queryWhetherAttention(userId, otherId);

        personalVo.setPersonalCenterUserVo(personalCenterUserVo);

        //??????????????????????????????
        String parameter = sameCityMapper.queryParameterByUserId(otherId);
        if (parameter != null) {
            personalVo.setParameterJson(JSONObject.parseObject(parameter, ParameterJson.class));
        }

        //???????????????????????????
        int postedCircleNum = circleMapper.queryHavePostedCircleNum(otherId);
        personalVo.setPostedCircleNum(postedCircleNum);

        //???????????????????????????
        personalVo.setCollaborateNum(resourceMapper.queryMyPostedPostsCount(otherId,13));

        //???????????????????????????
        personalVo.setResourceNum(resourceMapper.queryMyPostedPostsCount(otherId,12));

        if (userId == 0){
            personalVo.setWhetherAttention(0);
            return personalVo;
        }
        if (userId == otherId){
            personalVo.setWhetherAttention(2);
            return personalVo;
        }

        personalVo.setWhetherAttention(whetherAttention);

        return personalVo;
    }

    @Override
    public Object queryPersonalCircle(int userId, int otherId,int type, Paging paging) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pag="limit "+page+","+paging.getLimit()+"";

        //???????????????????????????????????????
        if (type == 0) {
            List<CircleClassificationVo> circles = circleMapper.queryHavePostedCirclePosts(otherId,pag);

            for (int i = 0; i < circles.size(); i++) {
                //???????????????
                String[] strings = circleMapper.selectImgByPostId(circles.get(i).getId());
                circles.get(i).setImg(strings);

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

        //?????????????????????????????????
        if (type == 1) {
            return resourceMapper.queryMyPostedPosts(otherId, 13,130, pag);
        }
        //?????????????????????????????????
        if (type == 2) {
            return resourceMapper.queryMyPostedPosts(otherId, 12,130, pag);
        }
        //?????????????????????????????????
        if (type == 3) {
            List<QuestionTagVo> questionTagVos = questionMapper.queryQuestionListByUser(otherId, pag);
            //????????????????????????
            if (userId != 0){
                for (QuestionTagVo vo : questionTagVos) {
                    Integer giveStatus = dryGoodsGiveMapper.whetherGive(0, userId, vo.getId());
                    if (giveStatus == 0) {
                        vo.setWhetherGive(0);
                    } else {
                        vo.setWhetherGive(1);
                    }
                }
            }
            return questionTagVos;
        }
        return null;
    }

    @Override
    public List<CircleVo> queryCircleByUSerId(int otherId,int type,Paging paging) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";

        //??????Ta???????????????
        if (type == 0){
            List<CircleVo> circleVos = circleMapper.myCircleAndCircleJoined(otherId, sql);
            for (int i=0;i<circleVos.size();i++){
                //????????????????????????
                List<Haplont> haplonts = communityMapper.selectHaplontByTagId(circleVos.get(i).getTagId());
                circleVos.get(i).setHaplonts(haplonts);

                if(circleVos.get(i).getWhetherPublic()==0){
                    if(circleVos.get(i).getUserId()!=otherId){
                        circleVos.remove(i);
                    }
                }
            }
            return circleVos;
        }
        //?????????????????????
        if (type == 1){
            List<CircleVo> circleVos = circleMapper.circleJoined(otherId, sql);
            for (int i=0;i<circleVos.size();i++){
                //????????????????????????
                List<Haplont> haplonts = communityMapper.selectHaplontByTagId(circleVos.get(i).getTagId());
                circleVos.get(i).setHaplonts(haplonts);
            }

            List<CircleVo> collect = circleVos.stream().filter(u -> u.getUserId() != otherId).collect(Collectors.toList());
            return collect;
        }
        return null;
    }
}
