package com.example.babacirclecommunity.circle.service.impl;

import com.example.babacirclecommunity.circle.dao.AttentionMapper;
import com.example.babacirclecommunity.circle.dao.CircleGiveMapper;
import com.example.babacirclecommunity.circle.dao.CircleMapper;
import com.example.babacirclecommunity.circle.dao.CommentMapper;
import com.example.babacirclecommunity.circle.entity.Give;
import com.example.babacirclecommunity.circle.service.ICircleGiveService;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.DateUtils;
import com.example.babacirclecommunity.common.utils.GoEasyConfig;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.inform.dao.InformMapper;
import com.example.babacirclecommunity.inform.entity.Inform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MQ
 * @date 2021/5/22 14:59
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CircleGiveServiceImpl implements ICircleGiveService {

    @Autowired
    private CircleGiveMapper circleGiveMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private InformMapper informMapper;

    @Override
    public List<CircleClassificationVo> queryGiveCircle(int userId, int otherId, Paging paging) {
        Integer pages=(paging.getPage()-1)*paging.getLimit();
        String pagings=" limit "+pages+","+paging.getLimit()+"";

        List<CircleClassificationVo> circles = circleGiveMapper.queryGiveCircle(otherId, pagings);
        for (int i=0;i<circles.size();i++){

            //???????????????
            String[] strings = circleMapper.selectImgByPostId(circles.get(i).getId());
            circles.get(i).setImg(strings);

            //???????????????????????????
            String[] strings1 = circleGiveMapper.selectCirclesGivePersonAvatar(circles.get(i).getId());
            circles.get(i).setGiveAvatar(strings1);

            //??????????????????
            Integer integer1 = circleGiveMapper.selectGiveNumber(circles.get(i).getId());
            circles.get(i).setGiveNumber(integer1);


            //??????0???????????????????????????????????? ????????????????????????
            if(userId==0){
                circles.get(i).setWhetherGive(0);
                circles.get(i).setWhetherAttention(0);
            }else{
                //??????????????????????????????
                int i1 = attentionMapper.queryWhetherAttention(userId, circles.get(i).getUId());
                if(i1>0){
                    circles.get(i).setWhetherAttention(1);
                }

                //??????????????????????????????   0?????? 1???
                Integer integer = circleGiveMapper.whetherGive(userId, circles.get(i).getId());
                if(integer>0){
                    circles.get(i).setWhetherGive(1);
                }
            }


            //????????????????????????
            Integer integer2 = commentMapper.selectCommentNumber(circles.get(i).getId());
            circles.get(i).setNumberPosts(integer2);


            //???????????????????????????????????????????????????????????????
            String time = DateUtils.getTime(circles.get(i).getCreateAt());
            circles.get(i).setCreateAt(time);
        }

        return circles;
    }

    @Override
    public int givePost(int id, int userId,int thumbUpId) {
        Give give = circleGiveMapper.selectCountWhether(userId, id);
        if(give==null){
            int i = circleGiveMapper.givePost(id, userId, System.currentTimeMillis() / 1000 + "");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }

            //????????????????????????????????????????????????
            if(userId!=thumbUpId){
                //????????????
                Inform inform=new Inform();
                inform.setContent(userId+"?????????"+thumbUpId+"?????????");
                inform.setCreateAt(System.currentTimeMillis()/1000+"");
                inform.setOneType(0);
                inform.setTId(id);
                inform.setInformType(1);
                inform.setNotifiedPartyId(thumbUpId);
                inform.setNotifierId(userId);

                //??????????????????
                int i1 = informMapper.addCommentInform(inform);
                if(i1<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"????????????");
                }

                //??????????????????
                GoEasyConfig.goEasy("channel"+thumbUpId,"1");
                log.info("{}","????????????????????????");
            }
            return i;
        }

        int i =0;
        //?????????????????????1 ????????????0 ????????????
        if(give.getGiveCancel()==1){
            i=circleGiveMapper.updateGiveStatus(give.getId(), 0);
        }

        //?????????????????????0 ????????????1 ???????????????
        if(give.getGiveCancel()==0){
            i = circleGiveMapper.updateGiveStatus(give.getId(), 1);
        }

        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }


        return i;
    }
}
