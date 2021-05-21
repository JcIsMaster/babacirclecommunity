package com.example.babacirclecommunity.circle.dao;

import com.example.babacirclecommunity.circle.vo.CircleClassificationVo;
import com.example.babacirclecommunity.circle.vo.CircleVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    @Select("SELECT r.id,r.tag_id, r.community_name, r.posters,COUNT(p.community_id) AS cnt FROM tb_community r" +
            " inner JOIN tb_community_user p on r.id = p.community_id where r.user_id=${userId} and r.type=1 GROUP BY p.community_id ${paging}")
    List<CircleVo> myCircleAndCircleJoined(@Param("userId") int userId, @Param("paging") String paging);

    /**
     * 查询我加入的圈子
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select b.id,b.tag_id, b.community_name, b.posters from tb_community_user a inner JOIN tb_community b on a.community_id=b.id where a.user_id=${userId} and b.type=1 GROUP BY a.community_id ${paging}")
    List<CircleVo> circleJoined(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 根据圈子内容模糊查询
     * @param content 内容
     * @param paging 分页
     * @return
     */
    @Select("select a.*,c.id as uId,c.avatar,c.user_name,b.tag_name,b.id as tagId from" +
            " tb_circles a INNER JOIN tb_user c on a.u_id=c.id INNER JOIN tb_tags b on a.tags_two=b.id where a.content like CONCAT('%',#{content},'%') and a.is_delete=1 order by a.create_at desc ${paging}")
    List<CircleClassificationVo> queryFuzzyCircle(@Param("content") String content, @Param("paging") String paging);


}
