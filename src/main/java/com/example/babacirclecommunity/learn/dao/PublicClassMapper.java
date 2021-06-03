package com.example.babacirclecommunity.learn.dao;

import com.example.babacirclecommunity.learn.entity.ClassOrder;
import com.example.babacirclecommunity.learn.vo.PublicClassTagVo;
import com.example.babacirclecommunity.learn.vo.PublicClassVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/5/4 17:57
 */
@Component
public interface PublicClassMapper {

    /**
     * 查询公开课列表
     * @param content
     * @param tagId
     * @param sql
     * @return
     */
    @Select("<script>"+
            "select a.id,a.title,a.tags_two,a.cover_img,a.price,a.buyer_num,b.tag_name from " +
            "tb_public_class a LEFT JOIN tb_tags b on a.tags_two = b.id where " +
            "<if test='tagId != null and tagId != 125'> a.tags_two = ${tagId} and </if>"+
            "<if test='content != null'>a.title LIKE CONCAT('%',#{content},'%') and </if>"+
            "a.is_delete = 1 ${sql}"+
            "</script>")
    List<PublicClassTagVo> queryPublicClassList(@Param("content") String content,@Param("tagId") Integer tagId,@Param("sql") String sql);

    /**
     * 根据userId查询公开课列表
     * @param uId
     * @param paging
     * @return
     */
    @Select("select a.id,a.title,a.tags_two,a.cover_img,a.price,a.buyer_num,b.tag_name from " +
            "tb_public_class a LEFT JOIN tb_tags b on a.tags_two = b.id where a.u_id = ${uId} and a.is_delete = 1 ORDER BY a.create_at desc ${paging}")
    List<PublicClassTagVo> queryPublicClassListByUser(@Param("uId") int uId, @Param("paging") String paging);

    /**
     * 根据id查询公开课详情
     * @param id
     * @return
     */
    @Select("select * from tb_public_class where id = ${id} and is_delete = 1")
    PublicClassVo queryPublicClassById(@Param("id") int id);

    /**
     * 根据id查询公开课详情
     * @param id
     * @return
     */
    @Select("select a.title,a.cover_img,b.user_name as uName,b.avatar as uAvatar from tb_public_class a inner join tb_user b on a.u_id=b.id where id = ${id} and is_delete = 1")
    PublicClassVo queryPublicClassPosters(@Param("id") int id);

    /**
     * 根据id查询公开课发帖人
     * @param id
     * @return
     */
    @Select("select u_id from tb_public_class where id = ${id}")
    Integer queryPublicClassUserIdById(@Param("id") int id);

    /**
     * 修改公开课收藏数
     * @param id
     * @param math
     * @return
     */
    @Update("update tb_public_class set collect = collect ${math} 1 where id = ${id}")
    int updatePublicClassCollect(@Param("id") int id,@Param("math") String math);

    /**
     * 新增购买记录
     * @param classOrder
     * @return
     */
    @Insert("insert into tb_class_order(t_id,u_id,price,create_at,is_delete) value(${classOrder.tId},${classOrder.uId},${classOrder.price},#{classOrder.createAt},1)")
    @Options(useGeneratedKeys=true, keyProperty="classOrder.id",keyColumn="id")
    int addBuyClassRecording(@Param("classOrder") ClassOrder classOrder);

    /**
     * 修改公开课购买人数
     * @param id
     * @return
     */
    @Update("update tb_public_class set buyer_num = buyer_num + 1 where id = ${id}")
    int updatePublicClassBuyerNum(@Param("id") int id);

    /**
     * 查看看贴人是否购买过该课程
     * @param tId 公开课id
     * @param uId 看贴人id
     * @return
     */
    @Select("select count(*) from tb_class_order where t_id = ${tId} and u_id = ${uId} and is_delete = 1")
    Integer queryBuyerStatus(@Param("tId") int tId,@Param("uId") int uId);
}
