package com.example.babacirclecommunity.personalCenter.service.impl;

import com.example.babacirclecommunity.circle.dao.*;
import com.example.babacirclecommunity.circle.entity.Haplont;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.circle.vo.CircleVo;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.my.dao.MyMapper;
import com.example.babacirclecommunity.personalCenter.service.IPersonalCenterService;
import com.example.babacirclecommunity.personalCenter.vo.PersonalVo;
import com.example.babacirclecommunity.resource.dao.ResourceMapper;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
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

    @Override
    public PersonalVo queryPersonalCenter(int userId,int otherId) {

        PersonalVo personalVo = new PersonalVo();

        //查询用户基本信息
        PersonalCenterUserVo personalCenterUserVo = userMapper.queryUserById(otherId);

        //获取当前年份
        int year = Calendar.getInstance().get(Calendar.YEAR);
        //设置用户年龄
        if(personalCenterUserVo.getBirthday() != null && !personalCenterUserVo.getBirthday().equals("") && !personalCenterUserVo.getBirthday().equals("null")){
            personalCenterUserVo.setAge(String.valueOf(year - Integer.parseInt(personalCenterUserVo.getBirthday().substring(0,4))));
        }
        else {
            personalCenterUserVo.setAge("未知");
        }

        //查询用户粉丝数
        personalCenterUserVo.setFansNum(myMapper.queryFanCountByUserId(otherId));

        //查询用户关注数
        personalCenterUserVo.setAttentionNum(myMapper.queryCareAboutCountByUserId(otherId));

        //查询我是否关注了他
        int whetherAttention = attentionMapper.queryWhetherAttention(userId, otherId);

        personalVo.setPersonalCenterUserVo(personalCenterUserVo);

        //查询用户动态帖数量
        int postedCircleNum = circleMapper.queryHavePostedCircleNum(otherId);
        personalVo.setPostedCircleNum(postedCircleNum);

        //查询用户合作帖数量
        personalVo.setCollaborateNum(resourceMapper.queryMyPostedPostsCount(otherId,13));

        //查询用户货源帖数量
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

        //查询个人中心里“动态”圈子
        if (type == 0) {
            List<CircleClassificationVo> circles = circleMapper.queryHavePostedCirclePosts(otherId,pag);

            for (int i = 0; i < circles.size(); i++) {
                //得到图片组
                String[] strings = circleMapper.selectImgByPostId(circles.get(i).getId());
                circles.get(i).setImg(strings);

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
            }
            return circles;
        }

        //查询个人中心里“合作”
        if (type == 1) {
            return resourceMapper.queryMyPostedPosts(otherId, 13,130, pag);
        }
        //查询个人中心里“货源”
        if (type == 2) {
            return resourceMapper.queryMyPostedPosts(otherId, 12,130, pag);
        }
        return null;
    }

    @Override
    public List<CircleVo> queryCircleByUSerId(int otherId,int type,Paging paging) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";

        //查询Ta创建的圈子
        if (type == 0){
            List<CircleVo> circleVos = circleMapper.myCircleAndCircleJoined(otherId, sql);
            for (int i=0;i<circleVos.size();i++){
                //得到单元体导航栏
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
        //查询加入的圈子
        if (type == 1){
            List<CircleVo> circleVos = circleMapper.circleJoined(otherId, sql);
            for (int i=0;i<circleVos.size();i++){
                //得到单元体导航栏
                List<Haplont> haplonts = communityMapper.selectHaplontByTagId(circleVos.get(i).getTagId());
                circleVos.get(i).setHaplonts(haplonts);
            }

            List<CircleVo> collect = circleVos.stream().filter(u -> u.getUserId() != otherId).collect(Collectors.toList());
            return collect;
        }
        return null;
    }
}
