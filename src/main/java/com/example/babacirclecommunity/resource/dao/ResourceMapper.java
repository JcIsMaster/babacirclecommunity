package com.example.babacirclecommunity.resource.dao;

import com.example.babacirclecommunity.resource.entity.Resources;
import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.resource.vo.ResourcesVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/5/27 17:55
 */
@Component
public interface ResourceMapper {

    /**
     * 查询资源数据
     * @param paging 分页
     * @param orderRule 0 推荐 1 最新 2最热
     * @param title 标题
     * @param tagId 标签id
     * @return
     */
    @Select({"<script>"+
            "select a.id,c.avatar,c.id as uId,c.user_name,a.title,a.browse,a.type,a.video,a.cover,b.tag_name" +
            ",b.id as tagId from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id " +
            "where  a.is_delete=1 and tags_one=12" +
            "<if test='title!=null and title!=\"\"'>"  +
            "and a.title like CONCAT('%',#{title},'%') " +
            "</if>" +
            "<if test='tagId!=130'>" +
            " and a.tags_two=${tagId}" +
            "</if>" +
            "<if test='orderRule==1'>" +
            " order by a.create_at DESC " +
            "</if>" +
            "<if test='orderRule==2'>" +
            " order by a.favour DESC" +
            "</if>" +
            " ${paging}"+
            "</script>"})
    List<ResourceClassificationVo> queryResource(@Param("paging") String paging,@Param("orderRule") int orderRule,@Param("title") String title,@Param("tagId") int tagId);


    /**
     * 查询单个资源帖子
     * @param id 单个资源帖子id
     * @return
     */
    @Select("select a.cover,a.id,a.tags_one,a.content,c.avatar,c.id as uId,c.user_name,a.title,a.favour,a.collect,a.browse,a.create_at,a.type,a.video,b.tag_name,b.id as tagId " +
            "from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.id=${id} and a.is_delete=1")
    ResourcesVo selectSingleResourcePost(@Param("id") int id);
    /**
     * 根据用户id和帖子id查看是否收藏着帖子
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_user_collection where u_id=${userId} and t_id=${tid} and is_delete=1 ")
    int selectWhetherCollection(@Param("userId") int userId,@Param("tid") int tid);

    /**
     * 浏览量加一
     * @param id
     * @return
     */
    @Insert("update tb_resources set browse=browse+1 where id=${id} ")
    int updateBrowse(@Param("id") int id);

    /**
     * 根据帖子id查询当前帖子图片
     * @param id
     * @return
     */
    @Select("select img_url from tb_img where z_id=${id}")
    String[] selectImgByPostId(@Param("id") int id);

    /**
     * 得到帖子的收藏数量
     * @param id 帖子id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_user_collection where t_id=${id} and is_delete=1")
    int selectCollectNumber(@Param("id") int id);

    /**
     * 根据帖子查询观看过人的头像
     * @param tId
     * @return
     */
    @Select("select b.avatar from tb_browse a INNER JOIN tb_user b on a.u_id=b.id where a.zq_id=${tId} and a.type=0 GROUP BY b.id limit 6")
    String[] selectBrowseAvatar(@Param("tId") int tId);

    /**
     * 统计帖子的浏览量
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_browse where zq_id=${tid} and type=0")
    int countPostNum(@Param("tid")  int tid);

    /**
     * 查询我发布资源帖子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select a.content,a.id,c.id as uId,c.user_name,c.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.u_id=${userId} and a.is_delete=1 order by a.create_at desc ${paging}")
    List<ResourceClassificationVo> queryHavePostedPosts(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 根据二级标签id查询推荐的数据
     * @param id 二级标签id
     * @return
     */
    @Select("select a.id,c.id as uId,c.user_name,c.avatar,a.title,a.browse,a.type,a.video,a.cover,b.tag_name,b.id as tagId " +
            "from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where  a.id in (SELECT id FROM (SELECT id FROM tb_resources where tags_one=12 and tags_two=${id} and is_delete=1 ORDER BY RAND()  LIMIT 10) t) ")
    List<ResourceClassificationVo> selectRecommendedSecondaryTagId(@Param("id") int id);

    /**
     * 根据一级标签id查询所有视频
     * @param tagsOne 一级标签id
     * @param paging 分页
     * @return
     */
    @Select("select a.id,a.cover,a.tags_one,a.content,c.avatar,c.id as uId,c.user_name,a.title,a.favour,a.collect,a.browse,a.create_at,a.type,a.video,b.tag_name,b.id as tagId" +
            " from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.tags_one=${tagsOne} and a.type=1 and a.is_delete=1 ${paging}")
    List<ResourcesVo> queryAllVideosPrimaryTagId(@Param("tagsOne") int tagsOne,@Param("paging") String paging);

    /**
     * 增加资源帖子
     * @param resources
     * @return
     */
    @Insert("insert into tb_resources(content,tags_one,tags_two,type,video,cover,create_at,u_id,title,haplont_type)values(#{resources.content},${resources.tagsOne},${resources.tagsTwo},${resources.type},#{resources.video},#{resources.cover},#{resources.createAt},${resources.uId},#{resources.title},${resources.haplontType})")
    @Options(useGeneratedKeys=true, keyProperty="resources.id",keyColumn="id")
    int addResourcesPost(@Param("resources") Resources resources);

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
}
