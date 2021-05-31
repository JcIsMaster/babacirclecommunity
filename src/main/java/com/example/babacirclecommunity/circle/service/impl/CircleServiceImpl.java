package com.example.babacirclecommunity.circle.service.impl;

import com.example.babacirclecommunity.circle.dao.*;
import com.example.babacirclecommunity.circle.entity.Browse;
import com.example.babacirclecommunity.circle.entity.Circle;
import com.example.babacirclecommunity.circle.entity.CommunityUser;
import com.example.babacirclecommunity.circle.entity.Haplont;
import com.example.babacirclecommunity.circle.service.ICircleService;
import com.example.babacirclecommunity.circle.vo.*;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.*;
import com.example.babacirclecommunity.home.entity.Community;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.tags.dao.TagMapper;
import com.example.babacirclecommunity.tags.entity.Tag;
import com.example.babacirclecommunity.user.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author MQ
 * @date 2021/5/20 19:39
 */
@Service
@Slf4j
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

    @Override
    public List<CircleClassificationVo> queryPostsPeopleFollow(int userId, Paging paging) {
        Integer page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";
        //查询我关注的人发的帖子
        List<CircleClassificationVo> circleClassificationVos = circleMapper.queryAttentionPerson(userId,sql);
        for (int i=0;i<circleClassificationVos.size();i++){

            //得到图片组
            String[] strings = circleMapper.selectImgByPostId(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setImg(strings);

            //得到点过赞人的头像
            String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circleClassificationVos.get(i).getId());
            circleClassificationVos.get(i).setGiveAvatar(strings1);



            //等于0在用户没有到登录的情况下 直接设置没有点赞
            if(userId==0){
                circleClassificationVos.get(i).setWhetherGive(0);
                circleClassificationVos.get(i).setWhetherAttention(0);
            }else{

                //查询是否对帖子点了赞   0没有 1有
                Integer integer = circleGiveMapper.whetherGive(userId, circleClassificationVos.get(i).getId());
                if(integer>0){
                    circleClassificationVos.get(i).setWhetherGive(1);
                }
            }


            //将所有关注状态为1关注状态
            circleClassificationVos.get(i).setWhetherAttention(1);
        }

        return circleClassificationVos;
    }

    @Override
    public List<CircleClassificationVo> queryImagesOrVideos(int type, Paging paging, int userId) {
        Integer pages=(paging.getPage()-1)*paging.getLimit();
        String pagings=" limit "+pages+","+paging.getLimit()+"";

        List<CircleClassificationVo> circles = circleMapper.queryImagesOrVideos(type, pagings);
        for (int i=0;i< circles.size();i++){

            //得到图片组
            String[] strings = circleMapper.selectImgByPostId(circles.get(i).getId());
            circles.get(i).setImg(strings);

            //得到看过这个帖子人的头像
            String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
            circles.get(i).setGiveAvatar(strings1);


            //等于0在用户没有到登录的情况下 直接设置没有点赞
            if(userId!=0){
                //查看我是否关注了此人
                int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                if(i1>0){
                    circles.get(i).setWhetherAttention(1);
                }

                //查询是否对帖子点了赞   0没有 1有
                Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                if(integer>0){
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
    public CircleClassificationVo querySingleCircle(int id, int userId) throws ParseException {
        //查询单个圈子
        CircleClassificationVo circleClassificationVo = circleMapper.querySingleCircle(id);
        if(circleClassificationVo==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"该圈子的帖子不存在");
        }
        //在用户登录的情况下 增加帖子浏览记录
        if(userId!=0){
            //得到上一次观看帖子的时间
            Browse browse = new Browse();
            String s = browseMapper.selectCreateAt(id, userId);
            if(s==null){
                //增加浏览记录
                browse.setCreateAt(System.currentTimeMillis()/1000+"");
                browse.setUId(userId);
                browse.setZqId(id);
                browse.setType(1);
                //增加浏览记录
                int i = browseMapper.addBrowse(browse);
                if(i<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
                }

                //修改帖子浏览数量
                int i1 = circleMapper.updateBrowse(id);
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
                    browse.setType(1);
                    //增加浏览记录
                    int i = browseMapper.addBrowse(browse);
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR,"增加浏览记录错误");
                    }

                    //修改帖子浏览数量
                    int i1 = circleMapper.updateBrowse(id);
                    if(i1<=0){
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
        if(userId==0){
            circleClassificationVo.setWhetherGive(0);
        }else{
            //查看我是否关注了此人
            int i1 = attentionMapper.queryWhetherAttention(userId, circleClassificationVo.getUId());
            if(i1>0){
                circleClassificationVo.setWhetherAttention(1);
            }

            //查询是否对帖子点了赞   0没有 1有
            Integer integer = circleGiveMapper.whetherGive(userId, circleClassificationVo.getId());
            if(integer==0){
                circleClassificationVo.setWhetherGive(0);
            }else{
                circleClassificationVo.setWhetherGive(1);
            }
        }

        //将时间戳转换为多少天或者多少个小时和多少年
        String time = DateUtils.getTime(circleClassificationVo.getCreateAt());
        circleClassificationVo.setCreateAt(time);


        return circleClassificationVo;
    }

    @Override
    public List<CircleClassificationVo> queryReferenceCircles(int userId, Paging paging) {
        Integer pages=(paging.getPage()-1)*paging.getLimit();
        String pagings=" limit "+pages+","+paging.getLimit()+"";

        List<CircleClassificationVo> circles = circleMapper.queryReferenceCircles(pagings);
        for (int i=0;i<circles.size();i++){

            //得到图片组
            String[] strings = circleMapper.selectImgByPostId(circles.get(i).getId());
            circles.get(i).setImg(strings);

            //得到点过赞人的头像
            String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
            circles.get(i).setGiveAvatar(strings1);


            if(userId!=0){
                //查看我是否关注了此人
                int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                if(i1>0){
                    circles.get(i).setWhetherAttention(1);
                }

                //查询是否对帖子点了赞   0没有 1有
                Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                if(integer>0){
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
    public List<CircleVo> queryCheckMyCirclesSquare(int userId,String communityName, Paging paging) {
        Integer pages=(paging.getPage()-1)*paging.getLimit();
        String pagings=" limit "+pages+","+paging.getLimit()+"";

        //如果communityName不等于空 就根据communityName查询圈子
        if(communityName!=null && !communityName.equals("") && !"undefined".equals(communityName)){
            List<CircleVo> circleVos = circleMapper.searchFundCircle(userId, communityName, pagings);
            for (int i=0;i<circleVos.size();i++){
                List<CircleImgIdVo> circleVos1 = circleMapper.queryCoveId(circleVos.get(i).getTagId());
                circleVos.get(i).setCircleVoList(circleVos1);
                //得到单元体导航栏
                List<Haplont> haplonts = communityMapper.selectHaplontByTagId(circleVos.get(i).getTagId());
                circleVos.get(i).setHaplonts(haplonts);

            }
            return circleVos;
        }


        //查询我创建的圈子
        List<CircleVo> circleVos = circleMapper.myCircleAndCircleJoined(userId, pagings);
        for (int i=0;i<circleVos.size();i++){
            List<CircleImgIdVo> circleVos1 = circleMapper.queryCoveId(circleVos.get(i).getTagId());
            circleVos.get(i).setCircleVoList(circleVos1);
            //得到单元体导航栏
            List<Haplont> haplonts = communityMapper.selectHaplontByTagId(circleVos.get(i).getTagId());
            circleVos.get(i).setHaplonts(haplonts);
        }

        return circleVos;
    }

    @Override
    public void addCircle(Community community) throws ParseException {
        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(community.getCommunityName(), token);
        if(identifyTextContent.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        //获取token
        String token1 = ConstantUtil.getToken();
        String identifyTextContent1 = ConstantUtil.identifyText(community.getIntroduce(), token1);
        if(identifyTextContent1.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        //获取token
        String token2 = ConstantUtil.getToken();
        String identifyTextContent2 = ConstantUtil.identifyText(community.getAnnouncement(), token2);
        if(identifyTextContent2.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        Tag tag=new Tag();
        tag.setImgUrl(community.getPosters());
        tag.setTagName(community.getCommunityName());
        tag.setCreateAt(System.currentTimeMillis()/1000+"");
        tag.setType(1);

        community.setCreateAt(System.currentTimeMillis()/1000+"");
        int i = tagMapper.addTag(tag);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加失败");
        }

        community.setTagId(tag.getId());
        int i1 = circleMapper.addCommunity(community);
        if(i1<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加圈子信息失败");
        }

        //添加圈子人数 （创建人）
        CommunityUser communityUser=new CommunityUser();
        communityUser.setCreateAt(System.currentTimeMillis()/1000+"");
        communityUser.setCommunityId(community.getId());
        communityUser.setUserId(community.getUserId());
        int i4 = circleMapper.addCommunityUser(communityUser);
        if(i4<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加圈子人错误");
        }


        int i2 = tagMapper.addTagHaplont(tag.getId(), 1);
        if(i2<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        int i3 = tagMapper.addTagHaplont(tag.getId(), 2);
        if(i3<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
    }

    @Override
    public void publishingCircles(Circle circle, String imgUrl) throws ParseException, IOException {
        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(circle.getContent(), token);
        if(identifyTextContent.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        circle.setCreateAt(System.currentTimeMillis()/1000+"");

        //如果状态等于1说明发布的是视频
        if(circle.getType()==1){
            String videoCover = FfmpegUtil.getVideoCover(circle.getVideo());
            circle.setCover(videoCover);
        }else{
            circle.setCover(imgUrl.split(",")[0]);
        }


        //添加圈子
        int i = circleMapper.addCirclePost(circle);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        //如果状态等于0说明发布的是图文
        if(circle.getType()==0){
            String[] split =imgUrl.split(",");
            int addImg = circleMapper.addImg(circle.getId(), split, System.currentTimeMillis() / 1000 + "", 1);
            if(addImg<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
        }

    }

    @Override
    public CommunityVo selectCommunityCategoryId(int id, int userId) {
        //查询圈子信息
        CommunityVo communityVo = communityMapper.selectCommunityCategoryId(id);
        if(communityVo==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"没有该圈子");
        }
        //得到圈子总人数
        int i = communityMapper.selectTotalNumberCirclesById(communityVo.getId());
        communityVo.setTotalNumberCircles(i);

        //查询圈子的用户头像
        String[] strings = communityMapper.selectCirclesAvatars(communityVo.getId());
        communityVo.setAvatar(strings);


        List<CommunityUser> communities = communityMapper.queryCommunityById(communityVo.getId());
        //匹配是否存在这个圈子
        communities.stream().filter(u->u.getUserId()==userId).forEach(u->{
            communityVo.setWhetherThere(1);
        });

        //得到单元体导航栏
        List<Haplont> haplonts = communityMapper.selectHaplontByTagId(id);
        communityVo.setHaplontList(haplonts);
        return communityVo;
    }


    @Override
    public int joinCircle(CommunityUser communityUser) {
        communityUser.setCreateAt(System.currentTimeMillis()/1000+"");

        //查询是否存在圈子里面 如果存在在调用接口就是退出圈子
        int i1 = communityMapper.queryWhetherThereCircle(communityUser.getCommunityId(), communityUser.getUserId());
        if(i1>0){
            //退出圈子
            int i = communityMapper.exitGroupChat(communityUser.getCommunityId(),communityUser.getUserId());
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"退出圈子失败");
            }
            return i;
        }

        int i = communityMapper.joinCircle(communityUser);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"加入圈子失败!");
        }
        return i;
    }

    @Override
    public List<CircleClassificationVo> queryClickUnitNavigationBar(int typeId, int userId, int tagId, Paging paging) {
        List<CircleClassificationVo> circles=null;

        String str="";
        int page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";


        //查询最新的数据
        if(typeId==0){
            str = "order by a.create_at desc " + sql;
            circles = circleMapper.selectPostsBasedTagIdCircleTwo(tagId,str);
        }

        //查询最热的数据
        if(typeId==1){
            str="order by a.favour desc " + sql;
            circles = circleMapper.selectPostsBasedTagIdCircleTwo(tagId,str);
        }

        //其他导航栏点击
        if(typeId>1){
            circles = circleMapper.queryPostByHaplontType(typeId, sql, tagId);
        }



        for (int i=0;i<circles.size();i++){
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
            if(userId==0){
                circles.get(i).setWhetherGive(0);
                circles.get(i).setWhetherAttention(0);
            }else{
                //查看我是否关注了此人
                int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                if(i1>0){
                    circles.get(i).setWhetherAttention(1);
                }

                //查询是否对帖子点了赞   0没有 1有
                Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                if(integer>0){
                    circles.get(i).setWhetherGive(1);
                }
            }


            //得到帖子评论数量
            Integer integer2 = commentMapper.selectCommentNumber(circles.get(i).getId());
            circles.get(i).setNumberPosts(integer2);


        }

        return circles;
    }

    @Override
    public Map<String,Object> fundCircle(int userId, String communityName, Paging paging) {
        int page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";

        Map<String,Object> map=new HashMap<>();

        //查询官方圈子
        List<CircleImgIdVo> circleImgIdVos = circleMapper.queryOfficialCircle();

        //取前三条数据
        List<CircleImgIdVo> collect = circleImgIdVos.stream().limit(3).collect(Collectors.toList());

        //如果communityName不等于空就查询圈子
        if(communityName!=null && !communityName.equals("") && !"undefined".equals(communityName)){
            //查询圈子
            List<CircleVo> circleVos = circleMapper.queryCircles(communityName);
            for (int i=0;i<circleVos.size();i++){
                List<CircleImgIdVo> circleVos1 = circleMapper.queryCoveId(circleVos.get(i).getTagId());
                circleVos.get(i).setCircleVoList(circleVos1);
            }

            map.put("circleVos",circleVos);
            map.put("circleImgIdVos",circleImgIdVos);
            return map;
        }

        //查询热门的圈子
        List<CircleVo> circleVos = circleMapper.queryPopularCircles(sql);
        for (int i=0;i<circleVos.size();i++){
            if(circleVos.get(i).getWhetherPublic()==0){
                if(circleVos.get(i).getUserId()!=userId){
                    circleVos.remove(i);
                }
            }
            List<CircleImgIdVo> circleVos1 = circleMapper.queryCoveId(circleVos.get(i).getTagId());
            circleVos.get(i).setCircleVoList(circleVos1);
        }


        map.put("circleVos",circleVos);
        map.put("circleImgIdVos",collect);

        return map;
    }

    @Override
    public void updateCircle(Community community) {
        int i = communityMapper.updateCircle(community);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"修改失败");
        }
    }

    @Override
    public void memberManagement(int communityId, int userId) {
        int i = communityMapper.exitGroupChat(communityId, userId);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"踢出失败！");
        }
    }

    @Override
    public List<UserVo> queryCircleMembers(int communityId) {
        return circleMapper.queryCircleMembers(communityId);
    }

    @Override
    public void addTagHaplont(int tagId, String hName) {

        Haplont haplont=new Haplont();
        haplont.setCreateAt(System.currentTimeMillis()/1000+"");
        haplont.setHName(hName);
        int i = circleMapper.addHaplont(haplont);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        int i1 = circleMapper.addTagHaplont(tagId, haplont.getId());
        if(i1<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加单元体错误");
        }
    }


}
