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
    public void ClickInterfaceHeadImageEnter(int bUserId, int gUserId) {
        //如果是自己观看自己则不添加观看记录数据
        if(bUserId!=gUserId){
            if(gUserId!=0){
                int i = myMapper.addViewingRecord(bUserId, gUserId, System.currentTimeMillis() / 1000 + "");
                if(i<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            }
        }

    }

    @Override
    public List<User> queryPeopleWhoHaveSeenMe(int userId, Paging paging) {
        //查询看过我的用户信息
        List<User> users = myMapper.queryPeopleWhoHaveSeenMe(userId, getPaging(paging));
        users.stream().forEach(u->{
            //得到当前时间戳和过去时间戳比较相隔多少分钟或者多少小时或者都少天或者多少年
            String time = DateUtils.getTime(u.getCreateAt());
            u.setCreateAt(time);
        });

        return users;
    }

    @Override
    public int updateUserDataByIntroduction(String introduction, int id) throws ParseException {
        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(introduction, token);
        if(identifyTextContent=="87014" || identifyTextContent.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        String sql=" introduce='"+introduction+"'";

        int i=myMapper.updateUserMessage(sql,id);
        if(i<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return i;
    }

    @Override
    public int updateUserAddress(String domicileProvince, String domicileCity, String domicileCounty, int id) {
        String sql=" curr_province='"+domicileProvince+"',city='"+domicileCity+"',county='"+domicileCounty+"'";

        int n=myMapper.updateUserMessage(sql, id);
        if(n<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        return n;
    }

    @Override
    public int updateUserAvatar(String avatar, int id) {
        String sql=" avatar='"+avatar+"'";
        int n=myMapper.updateUserMessage(sql, id);
        if(n<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return n;
    }

    @Override
    public int updateUserBirthday(String birthday, int id) {

        String sql=" birthday='"+birthday+"'";

        int updateUserMessage = myMapper.updateUserMessage(sql, id);
        if(updateUserMessage<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return updateUserMessage;
    }

    @Override
    public int updateUserBackgroundPicture(String backgroundPicture, int id) {

        String sql=" picture='"+backgroundPicture+"'";

        int updateUserMessage = myMapper.updateUserMessage(sql, id);
        if(updateUserMessage<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return updateUserMessage;
    }

    @Override
    public int updateUserName(String name, int id) throws ParseException {
        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(name, token);
        if(identifyTextContent=="87014" || identifyTextContent.equals("87014")){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"内容违规");
        }

        String sql=" user_name='"+name+"'";

        int updateUserMessage = myMapper.updateUserMessage(sql, id);
        if(updateUserMessage<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return updateUserMessage;
    }

    @Override
    public int updateUserSex(String sex, int id) {
        String sql=" user_sex='"+sex+"'";

        int updateUserMessage = myMapper.updateUserMessage(sql, id);
        if(updateUserMessage<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return updateUserMessage;
    }
}
