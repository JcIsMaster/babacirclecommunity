package com.example.babacirclecommunity.learn.service.impl;

import com.example.babacirclecommunity.circle.dao.CircleMapper;
import com.example.babacirclecommunity.circle.entity.Circle;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.*;
import com.example.babacirclecommunity.inform.dao.InformMapper;
import com.example.babacirclecommunity.inform.entity.Inform;
import com.example.babacirclecommunity.learn.dao.DryGoodsCollectMapper;
import com.example.babacirclecommunity.learn.dao.DryGoodsGiveMapper;
import com.example.babacirclecommunity.learn.dao.LearnCommentMapper;
import com.example.babacirclecommunity.learn.dao.QuestionMapper;
import com.example.babacirclecommunity.learn.entity.Collect;
import com.example.babacirclecommunity.learn.entity.Give;
import com.example.babacirclecommunity.learn.entity.LearnCommentGive;
import com.example.babacirclecommunity.learn.entity.Question;
import com.example.babacirclecommunity.learn.service.IQuestionService;
import com.example.babacirclecommunity.learn.vo.LearnCommentVo;
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
import java.text.ParseException;
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

    @Autowired
    private InformMapper informMapper;

    @Autowired
    private CircleMapper circleMapper;

    @Override
    public List<QuestionTagVo> queryQuestionList(int userId,int orderRule, Integer tagId,Integer planClassId, String content, Paging paging) {
        int page = (paging.getPage() - 1) * paging.getLimit();
        String sql = "";
        //最新
        if (orderRule == 0){
            sql = "order by a.create_at DESC limit " + page + "," + paging.getLimit();
        }
        //最热
        else {
            sql = "order by a.favour_num DESC limit " + page + "," + paging.getLimit();
        }

        List<QuestionTagVo> questionTagVos = questionMapper.queryQuestionList(content, tagId, planClassId, sql);
        for (QuestionTagVo vo : questionTagVos) {
            if(vo.getCircleId() != 0){
                //得到图片组
                String[] strings = circleMapper.selectImgByPostId(vo.getCircleId());
                vo.setImg(strings);
            }

            //我是否对该帖子点过赞
            if (userId == 0){
                vo.setWhetherGive(0);
            }
            else {
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

    @Override
    public ResultUtil addQuestion(Question question,String imgUrl) throws Exception{
        //内容检测
        String identifyTextContent = ConstantUtil.identifyText(question.getDescription(), ConstantUtil.getToken());
        if ("87014".equals(identifyTextContent)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "内容违规");
        }

        if (question.getType() != 0){
            //同步到圈子
            Circle circle = new Circle();
            circle.setContent(question.getDescription());
            circle.setTagsTwo(question.getTagsTwo());
            circle.setUserId(question.getUserId());
            circle.setCreateAt(System.currentTimeMillis() / 1000 + "");
            circle.setHaplontType(question.getHaplontId());
            //如果是视频帖
            if (question.getType() == 2){
                circle.setType(1);
                String videoCover = FfmpegUtil.getVideoCover(question.getVideo());
                circle.setCover(videoCover);
                circle.setVideo(question.getVideo());
            } else {
                circle.setType(0);
                circle.setCover(imgUrl.split(",")[0]);
            }
            //添加圈子
            int k = circleMapper.addCirclePost(circle);
            if (k <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }

            //如果是图文贴
            if (question.getType() == 1) {
                String[] split = imgUrl.split(",");
                if (split.length > 9) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "最多只能上传9张图片！");
                }

                int addImg = circleMapper.addImg(circle.getId(), split, System.currentTimeMillis() / 1000 + "", 1);
                if (addImg <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            }
            //设置提问同步的圈子帖id
            question.setCircleId(circle.getId());
        }

        //新增提问
        question.setCreateAt(System.currentTimeMillis() / 1000 + "");
        int i = questionMapper.addQuestion(question);
        if (i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return ResultUtil.success("发布成功");
    }

    @Override
    public QuestionVo queryQuestionById(int id, int userId) {
        QuestionVo questionVo = questionMapper.queryQuestionById(id);
        //如果userId为0，用户处于未登录状态，状态设为未点赞
        if (userId == 0) {
            questionVo.setWhetherGive(0);
            return questionVo;
        }
        //我是否对该帖子点过赞
        Integer giveStatus = dryGoodsGiveMapper.whetherGive(0, userId, questionVo.getId());
        if (giveStatus == 0) {
            questionVo.setWhetherGive(0);
        } else {
            questionVo.setWhetherGive(1);
        }
        //得到图片组
        if (questionVo.getCircleId() != 0){
            String[] strings = circleMapper.selectImgByPostId(questionVo.getCircleId());
            questionVo.setImg(strings);
        }

        return questionVo;
    }

    @Override
    public int giveLike(int id, int userId,int thumbUpId) {
        //查询数据库是否存在该条数据
        Give give = dryGoodsGiveMapper.selectCountWhether(0, userId, id);
        if (give == null) {
            int i = dryGoodsGiveMapper.giveLike(0, id, userId, System.currentTimeMillis() / 1000 + "");
            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = questionMapper.updateQuestionGive(id, "+");
            if (j <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }



            //不给自己帖子点赞进入判断发送消息
            if(userId!=thumbUpId){
                //通知对象
                Inform inform=new Inform();
                inform.setContent(userId+"点赞了"+thumbUpId);
                inform.setCreateAt(System.currentTimeMillis()/1000+"");
                inform.setOneType(1);
                inform.setTId(id);
                inform.setInformType(1);
                inform.setNotifiedPartyId(thumbUpId);
                inform.setNotifierId(userId);

                //添加评论通知
                int i1 = informMapper.addCommentInform(inform);
                if(i1<=0){
                    throw new ApplicationException(CodeType.SERVICE_ERROR,"评论失败");
                }

                //发送消息通知
                GoEasyConfig.goEasy("channel"+thumbUpId,"1");
                log.info("{}","点赞消息通知成功");
            }

            return j;
        }
        int i = 0;
        int j = 0;
        //如果当前状态是1 那就改为0 取消点赞
        if (give.getGiveCancel() == 1) {
            i = dryGoodsGiveMapper.updateGiveStatus(give.getId(), 0);
            j = questionMapper.updateQuestionGive(id, "-");
        }
        //如果当前状态是0 那就改为1 为点赞状态
        if (give.getGiveCancel() == 0) {
            i = dryGoodsGiveMapper.updateGiveStatus(give.getId(), 1);
            j = questionMapper.updateQuestionGive(id, "+");
        }
        if (i <= 0 || j <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return j;
    }

    @Override
    public int giveCollect(int id, int userId) {
        //查询数据库是否存在该条数据
        Collect collect = dryGoodsCollectMapper.selectCountWhether(0, userId, id);
        if (collect == null) {
            int i = dryGoodsCollectMapper.giveCollect(0, id, userId, System.currentTimeMillis() / 1000 + "");
            if (i <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            int j = questionMapper.updateQuestionCollect(id, "+");
            if (j <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
            return j;
        }
        int i = 0;
        int j = 0;
        //如果当前状态是1 那就改为0 取消收藏
        if (collect.getGiveCancel() == 1) {
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 0);
            j = questionMapper.updateQuestionCollect(id, "-");
        }
        //如果当前状态是0 那就改为1 为收藏状态
        if (collect.getGiveCancel() == 0) {
            i = dryGoodsCollectMapper.updateCollectStatus(collect.getId(), 1);
            j = questionMapper.updateQuestionCollect(id, "+");
        }
        if (i <= 0 || j <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        return j;
    }

    @Override
    public List<QuestionPersonalVo> queryMyAskList(int userId,Paging paging) {

        Integer page = (paging.getPage() - 1) * paging.getLimit();
        String pag = "limit " + page + "," + paging.getLimit();

        List<QuestionPersonalVo> questionPersonalVos = questionMapper.queryMyAskList(userId, pag);

        for (QuestionPersonalVo vo : questionPersonalVos) {
            //我是否对该帖子点过赞
            Integer giveStatus = dryGoodsGiveMapper.whetherGive(0, userId, vo.getTId());
            if (giveStatus == 0) {
                vo.setWhetherGive(0);
            } else {
                vo.setWhetherGive(1);
            }

        }

        return questionPersonalVos;
    }

    @Override
    public List<String> getQuestionPosters(String id, String pageUrl) {
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;


        QuestionTagVo questionTagVo = questionMapper.queryQuestionPosters(id);
//        if (questionTagVo.getAnonymous() == 1) {
//            questionTagVo.setUName("匿名用户");
//            questionTagVo.setAvatar("https://www.gofatoo.com/img/_20210603155752.png");
//            if (questionTagVo.getCoverImg() == null || questionTagVo.getCoverImg().equals("")) {
//                questionTagVo.setCoverImg("https://www.gofatoo.com/img/_20210603155758.png");
//            }
//        }

        String time = "";

        List<String> posterList = new ArrayList<>();

        //获取token
        String token = ConstantUtil.getToken();

        try {
            String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token;

            Map<String, Object> param = new HashMap<>(15);
            //秘钥
            param.put("scene", id);
            //二维码指向的地址
            param.put("page", pageUrl);
            param.put("width", 430);
            param.put("auto_color", false);
            //去掉二维码底色
            param.put("is_hyaline", true);
            Map<String, Object> lineColor = new HashMap<>(10);
            lineColor.put("r", 0);
            lineColor.put("g", 0);
            lineColor.put("b", 0);
            param.put("line_color", lineColor);

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
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

            File file = new File("e:/file/img/" + System.currentTimeMillis() + ".png");

            if (!file.exists()) {
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

            time = System.currentTimeMillis() / 1000 + 13 + "";


            WxPoster wxPoster = new WxPoster();
            //生成海报5
            String posterUrlGreatMaster = wxPoster.getPosterUrlGreatMasterQuestion("e:/file/img/2021515.jpg", file.getPath(), "e:/file/img/" + time + ".png", questionTagVo.getAvatar(), "https://www.gofatoo.com/img/_20210603155758.png", questionTagVo.getUserName(), questionTagVo.getTitle());
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
