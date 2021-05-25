package com.example.babacirclecommunity.circle.dao;

import com.example.babacirclecommunity.circle.entity.Circle;
import com.example.babacirclecommunity.circle.entity.CommunityUser;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.circle.vo.CircleImgIdVo;
import com.example.babacirclecommunity.circle.vo.CircleVo;
import com.example.babacirclecommunity.home.entity.Community;
import com.example.babacirclecommunity.user.vo.UserVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

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
    @Select("select r.whether_official,r.id,r.tag_id, r.community_name,r.posters,r.introduce,IFNULL(t1.count1, 0) AS cnt from tb_community r LEFT JOIN" +
            " (SELECT community_id,user_id,COUNT(*) AS count1 FROM tb_community_user p  GROUP BY community_id) t1" +
            " on r.id=t1.community_id where r.user_id=${userId} ORDER BY t1.community_id ${paging}")
    List<CircleVo> myCircleAndCircleJoined(@Param("userId") int userId, @Param("paging") String paging);

    /**
     * 查询我加入的圈子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select r.id,r.tag_id, r.community_name, r.posters,r.introduce,IFNULL(t1.count1, 0) AS cnt from tb_community r LEFT JOIN " +
            "(SELECT community_id,user_id,COUNT(*) AS count1 FROM tb_community_user p  GROUP BY community_id) t1 " +
            " on r.id=t1.community_id where t1.user_id=${userId} ORDER BY t1.community_id ${paging}")
    List<CircleVo> circleJoined(@Param("userId") int userId,@Param("paging") String paging);

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
    @Select("select a.community_name,a.id,count(b.tags_two) as cnt,a.tag_id,a.introduce,a.posters from tb_community a " +
            "INNER JOIN tb_circles b on a.tag_id=b.tags_two where b.is_delete=1 GROUP BY b.tags_two order by cnt desc ${paging}")
    List<CircleVo> queryPopularCircles(@Param("paging") String paging);

    /**
     * 模糊查询圈子（社区）
     * @param communityName 内容
     * @return
     */
    @Select("select posters,tag_id,introduce,community_name,whether_official from tb_community where whether_public=1 and community_name like CONCAT('%',#{communityName},'%')")
    List<CircleVo> queryCircles(@Param("communityName") String communityName);


    /**
     * 查询官方圈子
     * @return
     */
    @Select("select posters as cover,tag_id,community_name from tb_community where whether_official=1")
    List<CircleImgIdVo> queryOfficialCircle();
    /**
     * 统计每个圈子的人数
     * @param id 用户id
     * @return
     */
    @Select("select count(*) from tb_community_user where community_id=${id}")
    int countCircleJoined(@Param("id") int id);

    /**
     * 根据圈子内容模糊查询
     * @param content 内容
     * @param paging 分页
     * @return
     */
    @Select("select  a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.create_at,c.id as uId,c.avatar,c.user_name,b.tag_name,b.id as tagId from" +
            " tb_circles a INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.content like CONCAT('%',#{content},'%') and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryFuzzyCircle(@Param("content") String content, @Param("paging") String paging);

    /**
     *  根据用户id查询圈子文章
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select  a.forwarding_number,a.content,a.id,c.id as uId,c.user_name,c.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId from tb_circles a INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.user_id=${userId} and a.is_delete=1 order by a.create_at desc ${paging}")
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
    @Select("select img_url from tb_img where z_id=${id}")
    String[] selectImgByPostId(@Param("id") int id);

    /**
     * 查询我关注的人发布的圈子文章
     * @param userId 用户id
     * @return
     */
    @Select("select a.forwarding_number,a.id,a.content,a.tags_one,a.tags_two,a.type,d.id as uId,d.user_name,d.avatar,a.video,a.cover,a.browse,a.create_at,c.tag_name,c.id as tagId from tb_circles a INNER JOIN tb_user d on a.user_id=d.id INNER JOIN tb_user_attention b on a.user_id=b.bg_id INNER JOIN tb_tags c on a.tags_two=c.id where b.gu_id=${userId} and b.is_delete=1 and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryAttentionPerson(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 查询图文或者视频
     * @param type 类型（0 图文  1视频）
     * @param paging 分页
     * @return List<CircleClassificationVo>
     */
    @Select("select  a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.create_at,b.tag_name,b.id as tagId,c.avatar,c.id as uId,c.user_name " +
            "from tb_circles a INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id  " +
            "where a.type=${type} and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryImagesOrVideos(@Param("type") int type, @Param("paging") String paging);

    /**
     * 查询单个圈子的帖子
     * @param id 帖子id
     * @return
     */
    @Select("select  a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.create_at,b.tag_name,b.id as tagId,c.avatar,c.id as uId,c.user_name " +
            "from tb_circles a INNER JOIN tb_user c on a.user_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id  " +
            "where a.id=${id} and a.is_delete=1")
    CircleClassificationVo querySingleCircle(@Param("id") int id);

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
    @Select("select cover,id from tb_circles where tags_two=${tagsTwo} order by create_at desc limit 4")
    List<CircleImgIdVo> queryCoveId(@Param("tagsTwo") int tagsTwo);

    /**
     * 添加圈子
     * @param community
     */
    @Insert("insert into tb_community(community_name,posters,user_id,introduce,announcement,create_at,tag_id,whether_public)" +
            "values(#{community.communityName},#{community.posters},${community.userId},#{community.introduce},#{community.announcement}" +
            ",#{community.createAt},#{community.tagId},${community.whetherPublic})")
    @Options(useGeneratedKeys=true, keyProperty="community.id",keyColumn="id")
    int addCommunity(@Param("community") Community community);

    /**
     * 初始化圈子人数
     * @param communityUser
     */
    @Insert("insert into tb_community_user(community_id,user_id,create_at)" +
            "values(${communityUser.communityId},${communityUser.userId},#{communityUser.create_at})")
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
     * @param id 标签id
     * @param paging 分页
     * @return
     */
    @Select("select a.forwarding_number,a.id,a.content,a.browse,a.video,a.cover,a.create_at,b.tag_name,b.id as tagId,c.avatar,c.id as uId,c.user_name " +
            "from tb_circles a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id  " +
            "where a.tags_two=${id} and a.is_delete=1 order by a.create_at desc  ${paging}")
    List<CircleClassificationVo> selectPostsBasedTagIdCircleTwo(@Param("id") int id, @Param("paging") String paging);


}
