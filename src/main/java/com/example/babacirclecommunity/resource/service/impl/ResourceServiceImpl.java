package com.example.babacirclecommunity.resource.service.impl;

import com.example.babacirclecommunity.circle.dao.AttentionMapper;
import com.example.babacirclecommunity.circle.dao.BrowseMapper;
import com.example.babacirclecommunity.circle.entity.Browse;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.constanct.PointsType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.*;
import com.example.babacirclecommunity.resource.dao.CollectionMapper;
import com.example.babacirclecommunity.resource.dao.ResourceMapper;
import com.example.babacirclecommunity.resource.entity.Collection;
import com.example.babacirclecommunity.resource.entity.Resources;
import com.example.babacirclecommunity.resource.service.IResourceService;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.resource.vo.ResourcesVo;
import com.example.babacirclecommunity.user.dao.UserMapper;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import com.example.babacirclecommunity.user.vo.UserPersonalVo;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MQ
 * @date 2021/5/27 17:56
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ResourceServiceImpl implements IResourceService {


    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private BrowseMapper browseMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AttentionMapper attentionMapper;

    public String getPaging(Paging paging) {
        Integer page = (paging.getPage() - 1) * paging.getLimit();
        return "limit " + page + "," + paging.getLimit() + "";
    }

    @Override
    public List<ResourceClassificationVo> queryResource(Paging paging, int orderRule, int tagId, String title) {

        if (title == null || "".equals(title) || "undefined".equals(title)) {
            title = "";
        }
        return resourceMapper.queryResource(getPaging(paging), orderRule, title, tagId);
    }

    @Override
    public ResourcesVo selectSingleResourcePost(int id, int userId) throws ParseException {
        ResourcesVo resourcesVo = resourceMapper.selectSingleResourcePost(id);
        if (resourcesVo == null) {
            throw new ApplicationException(CodeType.RESOURCES_NOT_FIND);
        }
        //在用户登录的情况下 增加帖子浏览记录
        if (userId != 0) {
            //查看是否收藏
            int selectWhetherCollection = resourceMapper.selectWhetherCollection(userId, id);
            if (selectWhetherCollection > 0) {
                resourcesVo.setWhetherCollection(1);
            }
            //查看当前用户是否关注发帖人
            int whetherAttention = attentionMapper.queryWhetherAttention(userId, resourcesVo.getUId());
            if (whetherAttention > 0) {
                resourcesVo.setWhetherAttention(1);
            }

            //得到上一次观看帖子的时间
            Browse browse = new Browse();
            String s = resourceMapper.queryCreateAt(id, userId);
            if (s == null) {
                //增加浏览记录
                browse.setCreateAt(System.currentTimeMillis() / 1000 + "");
                browse.setUId(userId);
                browse.setZqId(id);
                browse.setType(0);

                //增加浏览记录
                int i = browseMapper.addBrowse(browse);
                if (i <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR, "增加浏览记录错误");
                }

                //修改帖子浏览数量
                int i1 = resourceMapper.updateBrowse(id);
                if (i1 <= 0) {
                    throw new ApplicationException(CodeType.SERVICE_ERROR);
                }
            } else {
                //得到过去时间和现在的时间是否相隔1440分钟 如果相隔了 就添加新的浏览记录
                long minutesApart = TimeUtil.getMinutesApart(s);
                if (minutesApart >= 1440) {
                    //修改帖子浏览数量
                    int i1 = resourceMapper.updateBrowse(id);
                    if (i1 <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }

                }
            }
        }

        //得到当前时间戳和过去时间戳比较相隔多少分钟或者多少小时或者都少天或者多少年
        String time = DateUtils.getTime(resourcesVo.getCreateAt());

        //根据帖子id查询出当前帖子图片
        String[] strings = resourceMapper.selectImgByPostId(resourcesVo.getId());
        resourcesVo.setImg(strings);
        resourcesVo.setCreateAt(String.valueOf(time));

        //得到收藏数量
        int i = resourceMapper.selectCollectNumber(id);
        resourcesVo.setCollect(i);

        //得到浏览过人的头像
        String[] strings1 = resourceMapper.selectBrowseAvatar(id);
        resourcesVo.setBrowseAvatar(strings1);

        //得到这个帖子的观看数量
        int browse = resourceMapper.countPostNum(id);
        resourcesVo.setBrowse(browse);

        return resourcesVo;
    }

    @Override
    public Map<String, Object> queryHavePostedPosts(int userId, int othersId, Paging paging) {

        //查询资源帖子信息
        List<ResourceClassificationVo> homeClassificationVos = resourceMapper.queryHavePostedPosts(othersId,0,getPaging(paging));

        //查询资源视频帖子信息
        List<ResourceClassificationVo> videoClassificationVos = resourceMapper.queryHavePostedPosts(othersId,1,getPaging(paging));

        //根据用户id查询出用户信息
        UserPersonalVo userPersonalVo = userMapper.queryResourceUserById(othersId);
        //获取当前年份
        int year = Calendar.getInstance().get(Calendar.YEAR);
        //设置用户年龄
        if(userPersonalVo.getBirthday() != null && !userPersonalVo.getBirthday().equals("") && !userPersonalVo.getBirthday().equals("null")){
            userPersonalVo.setAge(String.valueOf(year - Integer.parseInt(userPersonalVo.getBirthday().substring(0,4))));
        }
        else {
            userPersonalVo.setAge("未知");
        }

        Map<String, Object> map = new HashMap<>(5);

        map.put("homeClassificationVos", homeClassificationVos);
        map.put("videoClassificationVos", videoClassificationVos);
        map.put("user", userPersonalVo);

        //查询是否关注该用户
        int i1 = attentionMapper.queryWhetherAttention(userId, othersId);
        if (i1 > 0) {
            map.put("attention",1);
        }
        else {
            map.put("attention",0);
        }

        return map;
    }

    @Override
    public ResultUtil editUserResourceIntroduce(int userId, String introduce) {
        String userIntroduce = resourceMapper.queryUserResourceIntroduce(userId);
        //如果数据存在，修改介绍
        if (userIntroduce != null){
            int i = resourceMapper.updateUserResourceIntroduce(userId,introduce);
            if (i <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR,"修改用户资源介绍失败");
            }
            return ResultUtil.success(i);
        }
        //如果数据不存在，则添加介绍
        int i = resourceMapper.addUserResourceIntroduce(userId,introduce);
        if (i <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR,"添加用户资源介绍失败");
        }
        return ResultUtil.success(i);
    }

    @Override
    public List<ResourceClassificationVo> selectRecommendedSecondaryTagId(int id, int userId, int tid) {
        List<ResourceClassificationVo> homeClassificationVos = resourceMapper.selectRecommendedSecondaryTagId(id);

        //筛选掉等于当前用户id的数据
        //筛选掉当前点进来的帖子是一样的就干掉
        return homeClassificationVos.stream().filter(u -> u.getUId() != userId).filter(a -> a.getId() != tid).collect(Collectors.toList());
    }

    @Override
    public List<ResourcesVo> queryAllVideosPrimaryTagId(int id, Paging paging, int userId) throws ParseException {
        Integer page = (paging.getPage() - 1) * paging.getLimit();
        String pagings = "limit " + page + "," + paging.getLimit();

        //是否收藏
        int selectWhetherCollection = 0;

        //List存储数据顺序与插入数据顺序一致，存在先进先出的概念。
        List<ResourcesVo> resourcesVoa = new ArrayList<>();

        //根据id查询单个帖子
        ResourcesVo resourcesVo = resourceMapper.selectSingleResourcePost(id);
        //得到这个帖子的观看数量
        int countPostNum = resourceMapper.countPostNum(resourcesVo.getId());
        resourcesVo.setBrowse(countPostNum);

        //得到收藏数量
        int selectCollectNumber = resourceMapper.selectCollectNumber(resourcesVo.getId());
        resourcesVo.setCollect(selectCollectNumber);

        //查看是否收藏
        selectWhetherCollection = resourceMapper.selectWhetherCollection(userId, resourcesVo.getId());
        if (selectWhetherCollection > 0) {
            resourcesVo.setWhetherCollection(1);
        }
        resourcesVoa.add(resourcesVo);

        //根据一级标签id查询所有视频
        List<ResourcesVo> resourcesVos1 = resourceMapper.queryAllVideosPrimaryTagId(resourcesVo.getTagsOne(), pagings);

        //去除一样的
        List<ResourcesVo> resourcesVos = resourcesVos1.stream().filter(u -> u.getId() != resourcesVo.getId()).collect(Collectors.toList());
        for (int i = 0; i < resourcesVos.size(); i++) {
            if (userId != 0){
                //查看是否收藏
                selectWhetherCollection = resourceMapper.selectWhetherCollection(userId, resourcesVos.get(i).getId());
                if (selectWhetherCollection > 0) {
                    resourcesVos.get(i).setWhetherCollection(1);
                }

                //得到上一次观看帖子的时间
                Browse browse = new Browse();
                String s = resourceMapper.queryCreateAt(resourcesVos.get(i).getId(), userId);
                if (s == null) {
                    //增加浏览记录
                    browse.setCreateAt(System.currentTimeMillis() / 1000 + "");
                    browse.setUId(userId);
                    browse.setZqId(resourcesVos.get(i).getId());
                    browse.setType(0);

                    //增加浏览记录
                    int iq = browseMapper.addBrowse(browse);
                    if (iq <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR, "增加浏览记录错误");
                    }

                    //修改帖子浏览数量
                    int i1 = resourceMapper.updateBrowse(resourcesVos.get(i).getId());
                    if (i1 <= 0) {
                        throw new ApplicationException(CodeType.SERVICE_ERROR);
                    }
                } else {
                    //得到过去时间和现在的时间是否相隔1440分钟 如果相隔了 就添加新的浏览记录
                    long minutesApart = TimeUtil.getMinutesApart(s);
                    if (minutesApart >= 1440) {
                        //增加浏览记录
//                        browse.setCreateAt(System.currentTimeMillis() / 1000 + "");
//                        browse.setUId(userId);
//                        browse.setZqId(resourcesVos.get(i).getId());
//                        browse.setType(0);

                        //增加浏览记录
//                        int ie = browseMapper.addBrowse(browse);
//                        if (ie <= 0) {
//                            throw new ApplicationException(CodeType.SERVICE_ERROR, "增加浏览记录错误");
//                        }

                        //修改帖子浏览数量
                        int i1 = resourceMapper.updateBrowse(resourcesVos.get(i).getId());
                        if (i1 <= 0) {
                            throw new ApplicationException(CodeType.SERVICE_ERROR);
                        }

                    }
                }
            }

            //得到当前时间戳和过去时间戳比较相隔多少分钟或者多少小时或者都少天或者多少年
            String time = DateUtils.getTime(resourcesVos.get(i).getCreateAt());
            resourcesVos.get(i).setCreateAt(time);


            //得到这个帖子的观看数量
            int i2 = resourceMapper.countPostNum(resourcesVos.get(i).getId());
            resourcesVos.get(i).setBrowse(i2);

            //得到收藏数量
            int i1 = resourceMapper.selectCollectNumber(resourcesVos.get(i).getId());
            resourcesVos.get(i).setCollect(i1);

            //将查询出来的帖子视屏存放打list中
            resourcesVoa.add(resourcesVos.get(i));
        }

        return resourcesVoa;
    }

    @Override
    public int collectionPost(Collection collection) {
        collection.setCreateAt(System.currentTimeMillis() / 1000 + "");

        //查看是否有数据存在
        Collection collection1 = collectionMapper.selectCountWhether(collection.getUserId(), collection.getTId(), 0);

        //如果不存在
        if (collection1 == null) {
            //添加收藏信息
            int addCollection = collectionMapper.addCollectionPost(collection.getUserId(), collection.getTId(), collection.getCreateAt(), collection.getRemarks(), 0);
            if (addCollection <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR, "添加收藏信息错误");
            }
            int i = resourceMapper.updateCollect(collection1.getTId());
            if (i <= 0){
                throw new ApplicationException(CodeType.SERVICE_ERROR, "修改收藏数量错误");
            }
            return addCollection;
        }

        int i = 0;
        int j = 0;
        //如果当前状态是1 那就改为0 取消收藏
        if (collection1.getIsDelete() == 1) {
            i = collectionMapper.updateCollectionStatus(collection1.getId(), 0, 0);
            j = resourceMapper.updateCollectCut(collection1.getTId());
        }

        //如果当前状态是0 那就改为1 为收藏状态
        if (collection1.getIsDelete() == 0) {
            i = collectionMapper.updateCollectionStatus(collection1.getId(), 1, 0);
            j = resourceMapper.updateCollect(collection1.getTId());
        }

        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
        if (j <= 0){
            throw new ApplicationException(CodeType.SERVICE_ERROR, "修改收藏数量错误");
        }

        return j;
    }

    @Override
    public int issueResourceOrCircle(Resources resources, String imgUrl) throws Exception {

        //获取token
        String token = ConstantUtil.getToken();
        String identifyTextContent = ConstantUtil.identifyText(resources.getTitle(), token);
        if ("87014".equals(identifyTextContent)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "内容违规");
        }

        //获取token
        String token1 = ConstantUtil.getToken();
        String identifyTextContent1 = ConstantUtil.identifyText(resources.getContent(), token1);
        if ("87014".equals(identifyTextContent1)) {
            throw new ApplicationException(CodeType.SERVICE_ERROR, "内容违规");
        }

        return issue(resources, imgUrl);
    }

    @Override
    public List<String> getPosterImage(String pageUrl, String id) throws Exception {
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;


        //根据id查询帖子信息
//        ResourcesVo resourcesVo = resourceMapper.querySingleResourcePost(id);
//        if ((resourcesVo == null)) {
//            throw new ApplicationException(CodeType.SERVICE_ERROR, "帖子不存在");
//        }
        //String time = "";

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

            //time = System.currentTimeMillis() / 1000 + 13 + "";

            //WxPoster wxPoster = new WxPoster();
            //生成海报5
            //String posterUrlGreatMaster = wxPoster.getPosterUrlGreatMasterResource("e:/file/img/2021515.jpg", file.getPath(), "e:/file/img/" + time + ".png", resourcesVo.getAvatar(), resourcesVo.getCover(), resourcesVo.getContent(), resourcesVo.getUserName(), resourcesVo.getTitle());
            String newGreat = file.getPath().replace("e:\\file\\img\\", "https://www.gofatoo.com/img/");
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

    @Override
    public void deleteResourceById(int id) {
        int i = resourceMapper.deleteResourceById(id);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }
    }

    public int issue(Resources resources, String imgUrl) throws Exception {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        resources.setCreateAt(time);
        String[] split = null;

        //自己选封面
//        if (whetherCover == 1) {
//            if (resources.getType() == 0) {
//                split = imgUrl.split(",");
//            }
//        }

        //系统默认封面
//        if (whetherCover == 0) {
            //视频
            if (resources.getType() == 1) {
                String videoCover = FfmpegUtil.getVideoCover(resources.getVideo());
                resources.setCover(videoCover);
                //图片
            } else if (resources.getType() == 0) {
                split = imgUrl.split(",");
                resources.setCover(split[0]);
            }
//        }

        int i = resourceMapper.addResourcesPost(resources);
        if (i <= 0) {
            throw new ApplicationException(CodeType.SERVICE_ERROR);
        }

        if (resources.getType() == 0) {
            int addImg = resourceMapper.addImg(resources.getId(), split, time, 0);
            if (addImg <= 0) {
                throw new ApplicationException(CodeType.SERVICE_ERROR);
            }
        }

        //为用户添加荣誉积分
        if (resources.getTagsOne() == 12) {
            HonoredPointsUtil.addHonoredPoints(resources.getUId(), PointsType.HONORED_POINTS_RESOURCE,0, time);
        }else if (resources.getTagsOne() == 13) {
            HonoredPointsUtil.addHonoredPoints(resources.getUId(), PointsType.HONORED_POINTS_COLLABORATE,0, time);
        }

        return i;
    }
}
