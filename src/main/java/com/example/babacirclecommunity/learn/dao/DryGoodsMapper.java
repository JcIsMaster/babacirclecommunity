package com.example.babacirclecommunity.learn.dao;

import com.example.babacirclecommunity.learn.entity.DryGoods;
import com.example.babacirclecommunity.learn.entity.LearnPostExceptional;
import com.example.babacirclecommunity.learn.vo.DryGoodsPostersVo;
import com.example.babacirclecommunity.learn.vo.DryGoodsTagVo;
import com.example.babacirclecommunity.learn.vo.DryGoodsVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/4/16 16:03
 */
@Component
public interface DryGoodsMapper {

    /**
     * 查询干货列表
     * @param content
     * @param tagId
     * @param sql
     * @return
     */
    @Select("<script>"+
            "select a.id,a.title,a.description,a.cover_img,a.tags_two,a.create_at,b.tag_name,a.u_id,c.user_name,c.avatar from tb_dry_goods a " +
            "INNER JOIN tb_tags b on a.tags_two = b.id " +
            "INNER JOIN tb_user c on a.u_id = c.id where " +
            "<if test='tagId != null and tagId != 125'> a.tags_two = ${tagId} and </if>"+
            "<if test='content != null'>a.title LIKE CONCAT('%',#{content},'%') or a.description LIKE CONCAT('%',#{content},'%') and </if>"+
            "a.is_delete = 1 ${sql}" +
            "</script>")
    List<DryGoodsVo> queryDryGoodsList(@Param("content") String content,@Param("tagId") Integer tagId,@Param("sql") String sql);

    /**
     * 根据userId查询干货列表
     * @param uId
     * @param paging
     * @return
     */
    @Select("select id,title,description,cover_img from tb_dry_goods where u_id = ${uId} and is_delete = 1 ORDER BY create_at desc ${paging}")
    List<DryGoodsVo> queryDryGoodsListByUser(@Param("uId") int uId, @Param("paging") String paging);

    /**
     * 根据id查询干货详情
     * @param id
     * @return
     */
    @Select("select a.*,b.tag_name,COALESCE(SUM(c.gold_num),0) as goldNum from tb_dry_goods a INNER JOIN tb_tags b " +
            "on a.tags_two = b.id LEFT JOIN tb_learn_post_exceptional c on a.id = c.t_id and c.type = 1 " +
            "where a.id = ${id} and a.is_delete = 1")
    DryGoodsTagVo queryDryGoodsById(@Param("id") int id);

    /**
     * 干货海报
     * @param id 帖子id
     * @return
     */
    @Select("select a.title,a.cover_img,b.user_name,b.avatar from tb_dry_goods a inner join tb_user b on a.u_id=b.id " +
            "where a.id = ${id} and a.is_delete = 1")
    DryGoodsPostersVo queryDryGoodsPosters(@Param("id") int id);

    /**
     * 增加干货帖
     * @param dryGoods
     * @return
     */
    @Insert("insert into tb_dry_goods(u_id,title,tags_one,tags_two,description,cover_img,content,create_at) value(${dryGoods.uId},#{dryGoods.title},${dryGoods.tagsOne},${dryGoods.tagsTwo},#{dryGoods.description},#{dryGoods.coverImg},#{dryGoods.content},#{dryGoods.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="dryGoods.id",keyColumn="id")
    int addDryGoods(@Param("dryGoods") DryGoods dryGoods);

    /**
     * 修改帖子点赞数
     * @param id
     * @param math
     * @return
     */
    @Update("update tb_dry_goods set favour = favour ${math} 1 where id = ${id}")
    int updateDryGoodsGive(@Param("id") int id,@Param("math") String math);

    /**
     * 修改帖子收藏数
     * @param id
     * @param math
     * @return
     */
    @Update("update tb_dry_goods set collect = collect ${math} 1 where id = ${id}")
    int updateDryGoodsCollect(@Param("id") int id,@Param("math") String math);

    /**
     * 新增打赏记录
     * @param exceptional
     * @return
     */
    @Insert("insert into tb_learn_post_exceptional(t_id,gold_num,rewarder_id,type,create_at) value(${exceptional.tId},${exceptional.goldNum},${exceptional.rewarderId},${exceptional.type},#{exceptional.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="exceptional.id",keyColumn="id")
    int addRewardGoldRecording(@Param("exceptional") LearnPostExceptional exceptional);

    /**
     * 根据id查询干货发帖人
     * @param id
     * @return
     */
    @Select("select u_id from tb_dry_goods where id = ${id}")
    Integer queryDryGoodsUserIdById(@Param("id") int id);
}
