package com.example.babacirclecommunity.resource.dao;

import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
import com.example.babacirclecommunity.resource.vo.ResourcesVo;
import org.apache.ibatis.annotations.Insert;
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
            "<if test='title!=null and title!=\"\" and title!=undefined '>"  +
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
    @Select("select a.id,a.tags_one,a.content,c.avatar,c.id as uId,c.user_name,a.title,a.favour,a.collect,a.browse,a.create_at,a.type,a.video,b.tag_name,b.id as tagId " +
            "from tb_resources a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.id=${id}")
    ResourcesVo selectSingleResourcePost(@Param("id") int id);
    /**
     * 根据用户id和帖子id查看是否收藏着帖子
     * @param userId 用户id
     * @param tid 帖子id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_user_collection where u_id=${userId} and t_id=${tid} and is_delete=1")
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
}
