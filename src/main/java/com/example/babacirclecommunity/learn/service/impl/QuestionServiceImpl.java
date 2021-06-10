package com.example.babacirclecommunity.learn.service.impl;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.ConstantUtil;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.WxPoster;
import com.example.babacirclecommunity.learn.dao.DryGoodsCollectMapper;
import com.example.babacirclecommunity.learn.dao.DryGoodsGiveMapper;
import com.example.babacirclecommunity.learn.dao.QuestionMapper;
import com.example.babacirclecommunity.learn.entity.Collect;
import com.example.babacirclecommunity.learn.entity.Give;
import com.example.babacirclecommunity.learn.entity.Question;
import com.example.babacirclecommunity.learn.service.IQuestionService;
import com.example.babacirclecommunity.learn.vo.PublicClassVo;
import com.example.babacirclecommunity.learn.vo.QuestionTagVo;
import com.example.babacirclecommunity.learn.vo.QuestionVo;
import com.example.babacirclecommunity.personalCenter.vo.QuestionPersonalVo;
import com.example.babacirclecommunity.user.dao.UserMapper;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JC
 * @date 2021/4/29 10:46
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class QuestionServiceImpl implements IQuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DryGoodsGiveMapper dryGoodsGiveMapper;

    @Autowired
    private DryGoodsCollectMapper dryGoodsCollectMapper;

    @Override
    public int addQuestion(Question question) {
        question.setCreateAt(System.currentTimeMillis() / 1000 + "");
        return questionMapper.addQuestion(question);
    }

    @Override
    public QuestionTagVo queryQuestionById(int id, int userId) {
        QuestionTagVo questionTagVo = questionMapper.queryQuestionById(id);
        //获取发帖人名称,头像
        String uName = userMapper.selectUserById(questionTagVo.getUId()).getUserName();
        String avatar = userMapper.selectUserById(questionTagVo.getUId()).getAvatar();
        if(uName==null || avatar==null){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        questionTagVo.setUName(uName);
        questionTagVo.setAvatar(avatar);
        //如果userId为0，用户处于未登录状态，状态设为未点赞
        if (userId == 0){
            questionTagVo.setWhetherGive(0);
            questionTagVo.setWhetherCollect(0);
            return questionTagVo;
        }
        //我是否对该帖子点过赞
        Integer giveStatus = dryGoodsGiveMapper.whetherGive(0,userId, questionTagVo.getId());
        if (giveStatus == 0) {
            questionTagVo.setWhetherGive(0);
        } else {
            questionTagVo.setWhetherGive(1);
        }
        //我是否对该帖子收过藏
        Integer collectStatus = dryGoodsCollectMapper.whetherCollect(0,userId, questionTagVo.getId());
        if (collectStatus == 0) {
            questionTagVo.setWhetherCollect(0);
        } else {
            questionTagVo.setWhetherCollect(1);
        }
        return questionTagVo;
    }

    @Override
    public int giveLike(int id, int userId) {
        //查询数据库是否存在该条数据
        Give give = dryGoodsGiveMapper.selectCountWhether(0,userId,id);
        if(give == null){
            int i = dryGoodsGiveMapper.giveLike(0,id, userId, System.currentTimeMillis() / 1000 + "");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = questionMapper.updateQuestionGive(id,"+");
            if(j<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return j;
        }
        int i = 0;
        int j = 0;
        //如果当前状态是1 那就改为0 取消点赞
        if(give.getGiveCancel()==1){
            i = dryGoodsGiveMapper.updateGiveStatus(give.getId(), 0);
            j = questionMapper.updateQuestionGive(id,"-");
        }
        //如果当前状态是0 那就改为1 为点赞状态
        if(give.getGiveCancel()==0){
            i = dryGoodsGiveMapper.updateGiveStatus(give.getId(), 1);
            j = questionMapper.updateQuestionGive(id,"+");
        }
        if(i<=0 || j<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return j;
    }

    @Override
    public int giveCollect(int id, int userId) {
        //查询数据库是否存在该条数据
        Collect collect = dryGoodsCollectMapper.selectCountWhether(0,userId,id);
        if(collect == null){
            int i = dryGoodsCollectMapper.giveCollect(0,id, userId, System.currentTimeMillis() / 1000 + "");
            if(i<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = questionMapper.updateQuestionCollect(id,"+");
            if(j<=0){
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return j;
        }
        int i = 0;
        int j = 0;
        //如果当前状态是1 那就改为0 取消收藏
        if(collect.getGiveCancel()==1){
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 0);
            j = questionMapper.updateQuestionCollect(id,"-");
        }
        //如果当前状态是0 那就改为1 为收藏状态
        if(collect.getGiveCancel()==0){
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 1);
            j = questionMapper.updateQuestionCollect(id,"+");
        }
        if(i<=0 || j<=0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return j;
    }

    @Override
    public QuestionPersonalVo queryQuestionPersonal(int userId, int otherId,Paging paging) {

        Integer page=(paging.getPage()-1)*paging.getLimit();
        String pag="limit "+page+","+paging.getLimit()+"";

        QuestionPersonalVo questionPersonalVo = new QuestionPersonalVo();
        //查询用户基本信息
        PersonalCenterUserVo personalCenterUserVo = userMapper.queryUserById(otherId);
        questionPersonalVo.setPersonalCenterUserVo(personalCenterUserVo);
        //查询用户发的提问帖子
        List<QuestionVo> questionVos = questionMapper.queryQuestionListByUser(otherId, pag);
        questionPersonalVo.setQuestionVos(questionVos);
        if (userId == 0){
            questionPersonalVo.setIsMe(0);
            return questionPersonalVo;
        }
        if (userId == otherId){
            questionPersonalVo.setIsMe(1);
            return questionPersonalVo;
        }
        questionPersonalVo.setIsMe(0);
        return questionPersonalVo;
    }

    @Override
    public List<String> getQuestionPosters(int id, String pageUrl) {
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;

        System.out.println("提问=="+pageUrl);

        QuestionTagVo questionTagVo = questionMapper.queryQuestionPosters(id);
        if(questionTagVo.getAnonymous()==1){
            questionTagVo.setUName("匿名用户");
            questionTagVo.setAvatar("https://www.gofatoo.com/img/_20210603155752.png");
            if(questionTagVo.getCoverImg()==null || questionTagVo.getCoverImg().equals("")){
                questionTagVo.setCoverImg("https://www.gofatoo.com/img/_20210603155758.png");
            }
        }

        String time = "";

        List<String> posterList=new ArrayList<>();

        //获取token
        String token = ConstantUtil.getToken();

        try {
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+token;

            Map<String,Object> param = new HashMap<>(15);
            //秘钥
            param.put("scene", ConstantUtil.secret);
            //二维码指向的地址
            param.put("page", "pages/onlineRetailers/question/desc/desc");
            param.put("width", 430);
            param.put("auto_color", false);
            //去掉二维码底色
            param.put("is_hyaline", true);
            Map<String,Object> lineColor = new HashMap<>(10);
            lineColor.put("r", 0);
            lineColor.put("g", 0);
            lineColor.put("b", 0);
            param.put("line_color", lineColor);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<String,String>();
            // 头部信息
            List<String> list = new ArrayList<String>();
            list.add("Content-Type");
            list.add("application/json");
            headers.put("header", list);

            @SuppressWarnings("unchecked")
            HttpEntity requestEntity = new HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class);

            byte[] result = entity.getBody();

            inputStream = new ByteArrayInputStream(result);

            File file = new File("e:/file/img/"+System.currentTimeMillis()+".png");

            if (!file.exists()){
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
            outputStream.close();

            time=System.currentTimeMillis()/1000+13+"";


            WxPoster wxPoster=new WxPoster();
            //生成海报5
            String posterUrlGreatMaster = wxPoster.getPosterUrlGreatMasterQuestion("e:/file/img/2021515.jpg", file.getPath(), "e:/file/img/"+time+".png", questionTagVo.getAvatar(), questionTagVo.getCoverImg(),questionTagVo.getUName(),questionTagVo.getTitle());
            String newGreat = posterUrlGreatMaster.replace("e:/file/img/", "https://www.gofatoo.com/img/");
            /*if(newGreat!=null){
                if(circleFriendsVo.getType()==0){
                    //帖子分享数量加一
                    int i = circleMapper.updateForwardingNumber(circleFriendsVo.getId());
                    if(i<=0){
                        throw new ApplicationException(CodeType.SERVICE_ERROR,"分享错误");
                    }
                }
            }*/
            posterList.add(newGreat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return posterList;
    }
}
