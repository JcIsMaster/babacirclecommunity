package com.example.babacirclecommunity.resource.dao;

import com.example.babacirclecommunity.resource.vo.ResourceClassificationVo;
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
            "<if test='title!=null and title!=\"\">" +
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
}
