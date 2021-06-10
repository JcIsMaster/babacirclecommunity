package com.example.babacirclecommunity.circle.dao;

import com.example.babacirclecommunity.circle.entity.Attention;
import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author MQ
 * @date 2021/3/6 13:33
 */
@Component
public interface AttentionMapper {

    /**
     * 查询我是否关注该用户
     * @param guId 关注人id
     * @param bgId 被关注人id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_user_attention where gu_id=${guId} and bg_id=${bgId} and is_delete=1")
    int queryWhetherAttention(@Param("guId") int guId,@Param("bgId") int bgId);

    /**
     * 查询我是否关注过该用户
     * @param guId 关注人id
     * @param bgId 被关注人id
     * @return
     */
    @Select("select * from tb_user_attention where gu_id=${guId} and bg_id=${bgId}")
    Attention queryWhetherExist(@Param("guId") int guId, @Param("bgId") int bgId);

    /**
     * 修改关注状态
     * @param isDelete （取消关注，关注）
     * @param guId 关注人id
     * @param bgId 被关注人id
     * @return
     */
    @Update("Update tb_user_attention set is_delete=${isDelete}  where gu_id=${guId} and bg_id=${bgId} ")
    int updatePostingFollow(@Param("isDelete") int isDelete,@Param("guId") int guId,@Param("bgId") int bgId);

    /**
     * 添加关注信息
     * @param attention 关注对象
     * @return
     */
    @Insert("insert into tb_user_attention(gu_id,bg_id,remarks,create_at)values(${attention.userId},${attention.bgId},#{attention.remarks},#{attention.createAt})")
    int addAttention(@Param("attention") Attention attention);

    /**
     * 查询我关注的人发的圈子帖子
     * @param userId 用户id
     * @param paging
     * @return
     */
    @Select("select a.forwarding_number,a.id,a.content,a.tags_one,a.tags_two,a.type,d.id as uId,d.user_name,d.avatar,a.video,a.cover,a.browse,a.create_at,c.tag_name,c.id as tagId from tb_circles a INNER JOIN tb_user d on a.user_id=d.id INNER JOIN tb_user_attention b on a.user_id=b.bg_id INNER JOIN tb_tags c on a.tags_two=c.id where b.gu_id=${userId} and b.is_delete=1 and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryAttentionPerson(@Param("userId") int userId, @Param("paging") String paging);

    /**
     * 查询我关注的人发的圈子帖子数量
     * @param userId 用户id
     * @return
     */
    @Select("select COALESCE(count(a.id),0) from tb_circles a INNER JOIN tb_user d on a.user_id=d.id INNER JOIN tb_user_attention b on a.user_id=b.bg_id INNER JOIN tb_tags c on a.tags_two=c.id where b.gu_id=${userId} and b.is_delete=1")
    int countAttentionCircle(@Param("userId") int userId);

}
