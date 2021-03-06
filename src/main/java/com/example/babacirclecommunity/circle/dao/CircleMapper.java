package com.example.babacirclecommunity.circle.dao;

import com.example.babacirclecommunity.circle.entity.Circle;
import com.example.babacirclecommunity.circle.entity.CommunityTopic;
import com.example.babacirclecommunity.circle.entity.CommunityUser;
import com.example.babacirclecommunity.circle.entity.Haplont;
import com.example.babacirclecommunity.circle.vo.*;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.home.entity.Community;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.user.vo.UserVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import java.util.List;

/**
 * @author MQ
 * @date 2021/5/20 19:38
 */
@Component
public interface CircleMapper {

    /**
     * 根据id查询创建的圈子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select r.whether_public,r.user_id,r.whether_official,r.id,r.tag_id, r.community_name,r.posters,r.introduce, " +
            "(select count(tags_two) from tb_circles where is_delete = 1 and tags_two = r.tag_id) AS cnt from tb_community r " +
            "where r.is_delete = 1 and r.user_id=${userId} ORDER BY r.whether_top desc ${paging}")
    List<CircleVo> myCircleAndCircleJoined(@Param("userId") int userId, @Param("paging") String paging);

    /**
     * 查询创建的圈子的数量
     * @param userId
     * @return
     */
    @Select("select count(id) from tb_community where user_id = ${userId} and is_delete = 1")
    Integer myCircleCount(@Param("userId") int userId);

    /**
     * 查询我加入的圈子 （根据圈子帖子数量排序）
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select b.user_id,b.id,b.tag_id,b.community_name,b.posters,b.introduce,(select count(tags_two) from tb_circles where is_delete = 1 and tags_two = b.tag_id) " +
            "as cnt from tb_community_user a " +
            "INNER JOIN tb_community b on a.community_id=b.id where a.user_id=${userId} " +
            "and b.is_delete = 1 ORDER BY cnt desc,a.create_at desc ${paging}")
    List<CircleVo> circleJoined(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 查询我加入的圈子数量
     * @param userId
     * @return
     */
    @Select("select count(b.id) from tb_community_user a " +
            "INNER JOIN tb_community b on a.community_id = b.id where a.user_id = ${userId} and b.user_id != ${userId}")
    Integer circleJoinedCount(@Param("userId") int userId);

    /**
     * 根据用户id查询常逛的圈子（3条）
     * @param userId
     * @return
     */
    @Select("select c.tag_id,c.community_name,c.posters,count(*) as num from tb_browse a left join tb_circles b on a.zq_id = b.id " +
            "INNER JOIN tb_community c on b.tags_two = c.tag_id " +
            "where a.u_id = ${userId} and a.type = 1 and c.is_delete = 1 GROUP BY b.tags_two order by num desc limit 3")
    List<CircleMostViewVo> queryMostViewedCircle(@Param("userId") int userId);

    /**
     * 搜索自己创建的圈子
     * @param userId 用户id
     * @param communityName 圈子名称
     * @param paging
     * @return
     */
    @Select("select r.whether_official,r.id,r.tag_id, r.community_name,r.posters,r.introduce,IFNULL(t1.count1, 0) AS cnt from tb_community r LEFT JOIN" +
            " (SELECT community_id,user_id,COUNT(*) AS count1 FROM tb_community_user p  GROUP BY community_id) t1" +
            " on r.id=t1.community_id where r.community_name like CONCAT('%',#{communityName},'%') and r.user_id=${userId} ORDER BY t1.community_id ${paging}")
    List<CircleVo> searchFundCircle(@Param("userId") int userId,@Param("communityName") String communityName, @Param("paging") String paging);

    /**
     * 查询热门的圈子(根据帖子数量)
     * @param paging
     * @return
     */
    @Select("select a.whether_public,a.user_id,a.community_name,a.id,(select count(tags_two) from tb_circles where is_delete = 1 and tags_two = a.tag_id) as cnt," +
            "a.tag_id,a.introduce,a.posters from tb_community a " +
            "where a.is_delete=1 and a.whether_public=1 GROUP BY a.tag_id order by cnt desc ${paging}")
    List<CircleVo> queryPopularCircles(@Param("paging") String paging);

    /**
     * 模糊查询圈子
     * @param communityName 内容
     * @return
     */
    @Select("select id,posters,tag_id,community_name,whether_official from tb_community where whether_public=1 and community_name like CONCAT('%',#{communityName},'%') and is_delete = 1")
    List<CommunitySearchVo> queryCirclesByName(@Param("communityName") String communityName);

    /**
     * 查询是否有同名圈子
     * @param communityName 圈子名
     * @return
     */
    @Select("select count(id) from tb_community where is_delete = 1 and community_name = #{communityName}")
    int querySameNameExist(@Param("communityName") String communityName);

    /**
     * 查询广场热门话题
     * @return
     */
    @Select("select a.id,a.topic_name,sum(c.ct) cs from (select community_id,count(id) as ct from tb_community_user GROUP BY community_id) c left join tb_community b on c.community_id = b.id " +
            "INNER JOIN tb_community_topic a on b.community_type = a.id GROUP BY a.id order by cs desc limit 3")
    List<HotTopicVo> queryHotTopic();

    /**
     * 查询所有话题
     * @return
     */
    @Select("select * from tb_community_topic")
    List<CommunityTopic> queryAllTopic();

    /**
     * 根据话题查询圈子
     * @param topicId
     * @param sql
     * @return
     */
    @Select("select a.whether_public,a.user_id,a.community_name,a.id,(select count(tags_two) from tb_circles where is_delete = 1 and tags_two = a.tag_id) as cnt," +
            "a.tag_id,a.introduce,a.posters from tb_community a " +
            "where a.is_delete=1 and a.whether_public=1 and a.community_type = ${topicId} GROUP BY a.tag_id ${sql}")
    List<CircleVo> queryCommunityByTopic(@Param("topicId") int topicId, @Param("sql") String sql);

    /**
     * 查询该话题下所有的帖子（点赞排序）
     * @param id
     * @param topic
     * @param sql
     * @return
     */
    @Select("select a.type,a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.address,a.create_at,b.community_name as tagName,b.tag_id as tagId" +
            ",c.avatar,c.id as uId,c.user_name,c.user_sex,ifnull(d.giveNumber,0) as giveNumber ,ifnull(e.uu,0) as numberPosts " +
            "from tb_circles a LEFT JOIN (select COALESCE(count(*),0) as giveNumber,zq_id from tb_circles_give where give_cancel=1 GROUP BY zq_id) d on a.id=d.zq_id " +
            "LEFT JOIN (select COALESCE(count(*),0) as uu,t_id from tb_comment where is_delete = 1 GROUP BY t_id) e on a.id=e.t_id inner JOIN tb_user c on a.user_id=c.id " +
            "LEFT join tb_community b on a.tags_two = b.tag_id " +
            "LEFT join tb_community_topic g on b.community_type = g.id " +
            "where a.is_delete=1 and g.id = ${topic} and a.id <> ${id} order by giveNumber desc ${sql}")
    List<CircleClassificationVo> queryCirclesByTopic(@Param("id") int id,@Param("topic") int topic,@Param("sql") String sql);

    /**
     * 统计每个圈子的人数
     * @param id 圈子id
     * @return
     */
    @Select("select IFNULL(count(*),0) from tb_community_user where community_id=${id}")
    int countCircleJoined(@Param("id") int id);

    /**
     * 查询精选话题帖子
     * @param paging
     * @return
     */
    @Select("select a.type,a.id,a.content,a.browse,a.video,a.cover,a.address,a.create_at,b.tag_name,b.id as tagId,c.avatar,c.id as uId,c.user_name,c.user_sex " +
            "from tb_circles a INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.is_delete=1 and a.is_featured=1 ${paging}")
    List<CircleClassificationVo> queryFeatured(@Param("paging") String paging);

    /**
     * 根据圈子内容模糊查询
     * @param content 内容
     * @param paging 分页
     * @return
     */
    @Select("select a.type,a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.address,a.create_at,c.id as uId,c.avatar,c.user_name,b.tag_name,b.id as tagId from" +
            " tb_circles a INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.content like CONCAT('%',#{content},'%') and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryFuzzyCircle(@Param("content") String content, @Param("paging") String paging);

    /**
     *  根据用户id查询圈子文章
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select a.type,a.forwarding_number,a.content,a.id,c.id as uId,c.user_name,c.avatar,a.title,a.create_at,a.type,a.video,a.cover,a.address," +
            "b.tag_name,b.id as tagId from tb_circles a INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id " +
            "where a.user_id=${userId} and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryHavePostedCirclePosts(@Param("userId") int userId,@Param("paging") String paging);

    /**
     *  根据用户id查询圈子文章数量
     * @param userId 用户id
     * @return
     */
    @Select("select count(a.id) from tb_circles a INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.user_id=${userId} and a.is_delete=1")
    int queryHavePostedCircleNum(@Param("userId") int userId);

    /**
     * 根据帖子id查询当前帖子图片
     * @param id
     * @return
     */
    @Select("select img_url from tb_img where z_id=${id} and type=1")
    String[] selectImgByPostId(@Param("id") int id);

    /**
     * 查询我关注的人发布的圈子文章
     * @param userId 用户id
     * @return
     */
    @Select("select a.type,a.forwarding_number,a.id,a.content,a.tags_one,a.tags_two,a.type,d.id as uId,d.user_name,d.avatar" +
            ",d.user_sex,a.video,a.cover,a.browse,a.address,a.create_at,c.tag_name,c.id as tagId,ifnull(d.giveNumber,0) as giveNumber " +
            ",ifnull(e.uu,0) as numberPosts from tb_circles a LEFT JOIN (select count(*) as giveNumber,zq_id from " +
            "tb_circles_give where give_cancel=1 GROUP BY zq_id) d on a.id=d.zq_id LEFT JOIN (select COALESCE(count(*),0) as uu,t_id " +
            "from tb_comment where is_delete = 1 GROUP BY t_id) e on a.id=e.t_id INNER JOIN tb_user d on a.user_id=d.id INNER JOIN " +
            "tb_user_attention b on a.user_id=b.bg_id INNER JOIN tb_tags c on a.tags_two=c.id where b.gu_id=${userId} and b.is_delete=1 " +
            "and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryAttentionPerson(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 查询图文或者视频
     * @param type 类型（0 图文  1视频）
     * @param paging 分页
     * @return List<CircleClassificationVo>
     */
    @Select("select a.type,a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.create_at,b.tag_name,b.id as tagId,c.avatar" +
            ",c.id as uId,c.user_name,ifnull(d.giveNumber,0) as giveNumber ,ifnull(e.uu,0) as numberPosts from tb_circles a " +
            "LEFT JOIN (select count(*) as giveNumber,zq_id from tb_circles_give where give_cancel=1 GROUP BY zq_id) d on a.id=d.zq_id " +
            "LEFT JOIN (select COALESCE(count(*),0) as uu,t_id from tb_comment GROUP BY t_id) e on a.id=e.t_id " +
            "INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id  " +
            "where a.type=${type} and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryImagesOrVideos(@Param("type") int type, @Param("paging") String paging);

    /**
     * 查询单个圈子的帖子
     * @param id 帖子id
     * @return
     */
    @Select("select a.type,a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.create_at,b.tag_name,b.id as tagId," +
            "c.avatar,c.id as uId,c.user_name,ifnull(d.giveNumber,0) as giveNumber ,ifnull(e.uu,0) as numberPosts " +
            "from tb_circles a LEFT JOIN (select count(*) as giveNumber,zq_id from tb_circles_give where give_cancel=1 GROUP BY zq_id) d on a.id=d.zq_id " +
            "LEFT JOIN (select COALESCE(count(*),0) as uu,t_id from tb_comment GROUP BY t_id) e on a.id=e.t_id " +
            "INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id  " +
            "where a.id=#{id} and a.is_delete=1")
    CircleClassificationVo querySingleCircle(@Param("id") String id);

    /**
     * 查询单个圈子的帖子
     * @param id 帖子id
     * @return
     */
    @Select("select a.type,a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.address,a.create_at,b.tag_name,b.id as tagId," +
            "c.avatar,c.id as uId,c.user_name,ifnull(d.giveNumber,0) as giveNumber ,ifnull(e.uu,0) as numberPosts " +
            "from tb_circles a LEFT JOIN (select count(*) as giveNumber,zq_id from tb_circles_give where give_cancel=1 GROUP BY zq_id) d on a.id=d.zq_id " +
            "LEFT JOIN (select COALESCE(count(*),0) as uu,t_id from tb_comment where is_delete = 1 GROUP BY t_id) e on a.id=e.t_id " +
            "INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id " +
            "where a.id=${id} and a.is_delete=1")
    CircleClassificationVo selectSingleCircle(@Param("id") int id);

    /**
     * 浏览量加一
     * @param id 帖子id
     * @return
     */
    @Insert("update tb_circles set browse=browse+1 where id=${id} ")
    int updateBrowse(@Param("id") int id);

    /**
     * 转发数量加一
     * @param id 帖子id
     * @return
     */
    @Update("update tb_circles set forwarding_number=forwarding_number+1 where id=${id}")
    int updateForwardingNumber(@Param("id") int id);


    /**
     * 根据二级标签id查询封面和id
     * @param tagsTwo 二级标签id
     * @return
     */
    @Select("select cover,id from tb_circles where tags_two=${tagsTwo} and is_delete=1 order by create_at desc limit 4")
    List<CircleImgIdVo> queryCoveId(@Param("tagsTwo") int tagsTwo);

    /**
     * 添加圈子
     * @param community
     * @return
     */
    @Insert("insert into tb_community(community_name,posters,user_id,introduce,announcement,create_at,tag_id,whether_public,community_type)" +
            "values(#{community.communityName},#{community.posters},${community.userId},#{community.introduce},#{community.announcement}" +
            ",#{community.createAt},#{community.tagId},${community.whetherPublic},${community.communityType})")
    @Options(useGeneratedKeys=true, keyProperty="community.id",keyColumn="id")
    int addCommunity(@Param("community") Community community);

    /**
     * 初始化圈子人数
     * @param communityUser
     * @return
     */
    @Insert("insert into tb_community_user(community_id,user_id,create_at)" +
            "values(${communityUser.communityId},${communityUser.userId},#{communityUser.createAt})")
    int addCommunityUser(@Param("communityUser") CommunityUser communityUser);

    /**
     * 批量增加图片
     * @param zId 帖子id
     * @param imgUrl 图片地址
     * @param createAt 创建时间
     * @param postType 帖子类型
     * @return
     */
    @Insert("<script>" +
            "insert into tb_img(z_id,img_url,type,create_at) VALUES  " +
            "<foreach collection='imgUrl' item='item' index='index' separator=','>" +
            "(${zId},#{item},${postType},#{createAt})" +
            "</foreach>" +
            "</script>")
    int addImg(@Param("zId") int zId, @Param("imgUrl") String[] imgUrl,@Param("createAt") String createAt,@Param("postType") int postType);


    /**
     * 增加圈子帖子
     * @param circle
     * @return
     */
    @Insert("insert into tb_circles(content,tags_two,type,video,cover,create_at,user_id,title,haplont_type)values(#{circle.content},${circle.tagsTwo},${circle.type},#{circle.video},#{circle.cover},#{circle.createAt},${circle.userId},#{circle.title},${circle.haplontType})")
    @Options(useGeneratedKeys=true, keyProperty="circle.id",keyColumn="id")
    int addCirclePost(@Param("circle") Circle circle);

    /**
     * 根据圈子中二级标签id查询帖子
     * @param tagId 标签id
     * @param paging 分页
     * @return
     */
    @Select("select a.type,a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.address,a.create_at,b.tag_name,b.id as tagId,c.avatar,c.id as uId,c.user_name " +
            "from tb_circles a INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.is_delete=1 and a.tags_two=${tagId} ${paging}")
    List<CircleClassificationVo> selectPostsBasedTagIdCircleTwo(@Param("tagId") int tagId, @Param("paging") String paging);

    /**
     * 查询圈子成员
     * @param communityId 圈子id
     * @return
     */
    @Select("select b.id,b.user_name,b.avatar,b.introduce from tb_community_user a INNER JOIN tb_user b on a.user_id=b.id where a.community_id=${communityId}")
    List<UserVo> queryCircleMembers(@Param("communityId") int communityId);

    /**
     * 推荐（默认热门，浏览量）
     * @param paging
     * @return
     */
    @Select("select a.type,a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.address,a.create_at,b.tag_name,b.id as tagId" +
            ",c.avatar,c.id as uId,c.user_name,c.user_sex,ifnull(d.giveNumber,0) as giveNumber ,ifnull(e.uu,0) as numberPosts " +
            "from tb_circles a LEFT JOIN (select count(*) as giveNumber,zq_id from tb_circles_give where give_cancel=1 GROUP BY zq_id) d on a.id=d.zq_id " +
            "LEFT JOIN (select COALESCE(count(*),0) as uu,t_id from tb_comment where is_delete = 1 GROUP BY t_id) e on a.id=e.t_id INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id " +
            "where a.is_delete=1 order by a.browse desc ${paging}")
    List<CircleClassificationVo> queryReferenceCircles(@Param("paging") String paging);

    /**
     * 推荐（已登录且参与匹配）
     * @param needs
     * @param paging
     * @return
     */
    @Select("(select a.type,a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.address,a.create_at,b.tag_name,b.id as tagId" +
            ",c.avatar,c.id as uId,c.user_name,c.user_sex,ifnull(d.giveNumber,0) as giveNumber ,ifnull(e.uu,0) as numberPosts " +
            "from tb_circles a LEFT JOIN (select count(*) as giveNumber,zq_id from tb_circles_give where give_cancel=1 GROUP BY zq_id) d on a.id=d.zq_id " +
            "LEFT JOIN (select COALESCE(count(*),0) as uu,t_id from tb_comment where is_delete = 1 GROUP BY t_id) e on a.id=e.t_id INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id " +
            "where a.is_delete=1 and a.user_id in (select user_id from tb_parameter where json_class-> '$.twoSelect' = #{needs}) order by a.create_at desc) " +
            "UNION " +
            "(select a.type,a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.address,a.create_at,b.tag_name,b.id as tagId" +
            ",c.avatar,c.id as uId,c.user_name,c.user_sex,ifnull(d.giveNumber,0) as giveNumber ,ifnull(e.uu,0) as numberPosts " +
            "from tb_circles a LEFT JOIN (select count(*) as giveNumber,zq_id from tb_circles_give where give_cancel=1 GROUP BY zq_id) d on a.id=d.zq_id " +
            "LEFT JOIN (select COALESCE(count(*),0) as uu,t_id from tb_comment where is_delete = 1 GROUP BY t_id) e on a.id=e.t_id INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id " +
            "where a.is_delete=1 order by a.browse desc,giveNumber desc) ${paging}")
    List<CircleClassificationVo> queryReferenceLoggedAndMatch(@Param("needs") String needs,@Param("paging") String paging);

    /**
     * 推荐（已登录但未参与匹配）
     * @param userId
     * @param paging
     * @return
     */
    @Select("(select a.type,a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.address,a.create_at,b.tag_name,b.id as tagId" +
            ",c.avatar,c.id as uId,c.user_name,c.user_sex,ifnull(d.giveNumber,0) as giveNumber ,ifnull(e.uu,0) as numberPosts " +
            "from tb_circles a LEFT JOIN (select count(*) as giveNumber,zq_id from tb_circles_give where give_cancel=1 GROUP BY zq_id) d on a.id=d.zq_id " +
            "LEFT JOIN (select COALESCE(count(*),0) as uu,t_id from tb_comment where is_delete = 1 GROUP BY t_id) e on a.id=e.t_id INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id " +
            "where a.is_delete=1 and a.tags_two in (select DISTINCT a.tags_two from tb_circles a INNER JOIN tb_browse b on a.id = b.zq_id where b.type = 1 and " +
            "UNIX_TIMESTAMP(DATE_SUB(FROM_UNIXTIME(unix_timestamp(now()),'%Y-%m-%d %H:%i:%s'), INTERVAL 30 DAY))<=b.create_at and b.u_id = ${userId}))" +
            "UNION " +
            "(select a.type,a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.address,a.create_at,b.tag_name,b.id as tagId" +
            ",c.avatar,c.id as uId,c.user_name,c.user_sex,ifnull(d.giveNumber,0) as giveNumber ,ifnull(e.uu,0) as numberPosts " +
            "from tb_circles a LEFT JOIN (select count(*) as giveNumber,zq_id from tb_circles_give where give_cancel=1 GROUP BY zq_id) d on a.id=d.zq_id " +
            "LEFT JOIN (select COALESCE(count(*),0) as uu,t_id from tb_comment where is_delete = 1 GROUP BY t_id) e on a.id=e.t_id INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id " +
            "where a.is_delete=1 order by a.browse desc,giveNumber desc) ${paging}")
    List<CircleClassificationVo> queryReferenceLoggedAndNotMatch(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 添加圈子的标签
     * @param tagId 标签id
     * @param haplontId 名称
     * @return
     */
    @Insert("insert into tb_tag_haplont(tag_id,haplont_id)values(${tagId},#{haplontId})")
    int addTagHaplont(@Param("tagId") int tagId,@Param("haplontId") int haplontId);

    /**
     * 添加单元体
     * @return
     */
    @Insert("insert into tb_haplont(h_name,create_at)values(#{haplont.hName},#{haplont.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="haplont.id",keyColumn="id")
    int addHaplont(@Param("haplont") Haplont haplont);

    /**
     * 根据三级标签查询资源数据
     * @param haplontType
     * @param paging
     * @param tagId
     * @return
     */
    @Select("select a.id,c.id as uId,c.avatar,c.user_name,a.title,a.browse,a.type,a.video,a.cover,a.address,a.create_at,a.content,b.tag_name,b.id as tagId from" +
            " tb_circles a INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.haplont_type=${haplontType} and a.tags_two=${tagId} and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryPostByHaplontType(@Param("haplontType") int haplontType, @Param("paging") String paging, @Param("tagId") int tagId);

    /**
     * 删除资源 圈子
     * @param id 标签id
     * @return
     */
    @Update("update tb_circles set is_delete = 0 where tags_two=${id}")
    int deletePosts(@Param("id") int id);

    /**
     * 删除圈子 帖子
     * @param id 帖子id
     * @return
     */
    @Update("update tb_circles set is_delete = 0 where id=${id}")
    int deleteCircles(@Param("id") int id);

    /**
     * 查询官方圈子
     * @param paging
     * @return
     */
    @Select("select * from tb_community where whether_official = 1 and is_delete = 1")
    List<Community> queryOfficialCircleList(@Param("paging") String paging);

    /**
     * 查询用户是否加入了某圈子
     * @param id
     * @param userId
     * @return
     */
    @Select("select count(*) from tb_community_user where community_id = ${id} and user_id = ${userId}")
    int queryWhetherJoinedCircle(@Param("id") int id,@Param("userId") int userId);
}
