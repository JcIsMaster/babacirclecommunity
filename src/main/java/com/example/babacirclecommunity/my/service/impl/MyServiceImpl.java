package com.example.babacirclecommunity.my.service.impl;

import com.example.babacirclecommunity.circle.dao.AttentionMapper;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.ConstantUtil;
import com.example.babacirclecommunity.common.utils.DateUtils;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.my.dao.MyMapper;
import com.example.babacirclecommunity.my.entity.ComplaintsSuggestions;
import com.example.babacirclecommunity.my.service.IMyService;
import com.example.babacirclecommunity.my.vo.PeopleCareAboutVo;
import com.example.babacirclecommunity.user.dao.UserMapper;
import com.example.babacirclecommunity.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private UserMapper userMapper;
    /**
     * 得到分页
     * @param paging
     * @return
     */
    public String getPaging(Paging paging){
        int page=(paging.getPage()-1)*paging.getLimit();
        String sql="limit "+page+","+paging.getLimit()+"";
        return sql;
    }


    @Override
    public Map<String,Object> queryPeopleCareAbout(Paging paging, int userId) {


        Map<String,Object> map=null;

        //查询我关注人
        List<PeopleCareAboutVo> peopleCareAboutVos = myMapper.queryPeopleCareAbout(userId, getPaging(paging));
        if(peopleCareAboutVos!=null && peopleCareAboutVos.size()!=0){
            map=new HashMap<>(2);
            map.put("peopleCareAboutVos",peopleCareAboutVos);
            map.put("count",peopleCareAboutVos.size());
            return map;
        }

        return null;
    }

    @Override
    public Map<String, Object> queryFan(Paging paging, int userId) {

        Map<String,Object> map=null;

        //查询我的粉丝
        List<PeopleCareAboutVo> queryFan = myMapper.queryFan(userId, getPaging(paging));
        for (int i=0;i<queryFan.size();i++){
            int i1 = attentionMapper.queryWhetherAttention(userId, queryFan.get(i).getId());
            if(i1>0){
                queryFan.get(i).setWhetherFocus(i1);
            }

        }
        if(queryFan!=null && queryFan.size()!=0){
            map=new HashMap<>(2);
            map.put("queryFan",queryFan);
            map.put("count",queryFan.size());
            return map;
        }

        return null;
    }

    @Override
    public int addComplaintsSuggestions(ComplaintsSuggestions complaintsSuggestions) {
        complaintsSuggestions.setCreateAt(System.currentTimeMillis()/1000+"");
        int i = myMapper.addComplaintsSuggestions(complaintsSuggestions);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"投诉错误");
        }
        return i;
    }

    @Override
    public void ClickInterfaceHeadImageEnter(int bUserId, int userId) {
        //如果是自己观看自己则不添加观看记录数据
        if(bUserId!=userId){
            if(userId!=0){
                int i = myMapper.addViewingRecord(bUserId, userId, System.currentTimeMillis() / 1000 + "");
                if(i<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            }
        }

    }

    @Override
    public Map<String,Object> queryPeopleWhoHaveSeenMe(int userId, Paging paging) {

        Map<String,Object> map=new HashMap<>(2);

        //查询看过我的用户信息
        List<User> users = myMapper.queryPeopleWhoHaveSeenMe(userId, getPaging(paging));
        users.stream().forEach(u->{
            //得到当前时间戳和过去时间戳比较相隔多少分钟或者多少小时或者都少天或者多少年
            String time = DateUtils.getTime(u.getCreateAt());
            u.setCreateAt(time);
        });

        //查询今天看过我的人数
        Integer integer = myMapper.queryPeopleWhoHaveSeenMeAvatar(userId);

        map.put("users",users);
        map.put("integer",integer);

        return map;
    }

    @Override
    public int updateUserInformation(User user) throws ParseException {
        //获取token
        String token1 = ConstantUtil.getToken();
        String identifyTextContent1 = ConstantUtil.identifyText(user.getUserName(), token1);
        if(identifyTextContent1.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        //获取token
        String token2 = ConstantUtil.getToken();
        String identifyTextContent2 = ConstantUtil.identifyText(user.getIntroduce(), token2);
        if(identifyTextContent2.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        return myMapper.updateUserMessage(user);
    }


}
