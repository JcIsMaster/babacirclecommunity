package com.example.babacirclecommunity.circle.dao;

import com.example.babacirclecommunity.circle.entity.CommunityUser;
import com.example.babacirclecommunity.circle.entity.Haplont;
import com.example.babacirclecommunity.circle.vo.CommunityVo;
import com.example.babacirclecommunity.home.entity.Community;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/5/24 13:37
 */
@Component
public interface CommunityMapper {

    /**
     * 根据圈子id查询圈子信息
     * @param id 标签id
     * @return
     */
    @Select("select a.*,b.user_name,b.id as userId from tb_community a INNER JOIN tb_user b on a.user_id=b.id where a.tag_id=${id} and a.is_delete=1")
    CommunityVo selectCommunityCategoryId(@Param("id") int id);

    /**
     * 根据圈子id查询该圈子有多少人
     * @param id 圈子id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_community_user where community_id=${id} ")
    int selectTotalNumberCirclesById(@Param("id") int id);

    /**
     * 根据圈子id查询这个圈子里面的人
     * @param id 圈子id
     * @return
     */
    @Select("select b.avatar from tb_community_user a INNER JOIN tb_user b on a.user_id=b.id where a.community_id=${id} limit 6")
    String[] selectCirclesAvatars(@Param("id") int id);

    /**
     * 根据圈子id查询该用户是否在这个圈子里面
     * @param id
     * @return
     */
    @Select("select * from tb_community_user where community_id=${id}")
    List<CommunityUser> queryCommunityById(@Param("id") int id);

    /**
     * 根据标签id查询单元体导航栏
     * @param id 标签id
     * @return
     */
    @Select("SELECT a.id,a.h_name FROM `tb_haplont` a INNER JOIN tb_tag_haplont b on  a.id=b.haplont_id where b.tag_id=${id}")
    List<Haplont> selectHaplontByTagId(@Param("id") int id);

    /**
     * 根据圈子id和用户id查询是否在这个圈子里面
     * @param id 圈子id
     * @param userId 用户id
     * @return
     */
    @Select("select COALESCE(count(*),0) from tb_community_user where community_id=${id} and user_id=${userId}")
    int queryWhetherThereCircle(@Param("id") int id,int userId);

    /**
     * 退出圈子
     * @param id 圈子id
     * @param userId 用户id
     * @return
     */
    @Delete("delete from tb_community_user where community_id=${id} and user_id=${userId}")
    int exitGroupChat(@Param("id") int id,@Param("userId") int userId);

    /**
     * 加入圈子
     * @param communityUser
     * @return
     */
    @Insert("insert into tb_community_user(community_id,user_id,create_at)values(${communityUser.communityId},${communityUser.userId},#{communityUser.createAt})")
    int joinCircle(@Param("communityUser") CommunityUser communityUser);

    /**
     * 修改圈子
     * @param community
     * @return
     */
    @Update("update tb_community set community_name=#{community.communityName},posters=#{community.posters},introduce=#{community.introduce}," +
            "announcement=#{community.announcement},whether_public=${community.whetherPublic} where id=${community.id}")
    int updateCircle(@Param("community") Community community);

    /**
     * 删除圈子
     * @param id 圈子id
     * @return
     */
    @Update("update tb_community set is_delete=0 where id=${id}")
    int deleteCircle(@Param("id") int id);

    /**
     * 置顶圈子
     * @param id
     * @return
     */
    @Update("update tb_community set whether_top = 1 where id=${id}")
    int topCircle(@Param("id") int id);

    /**
     * 取消置顶圈子
     * @param userId
     * @return
     */
    @Update("update tb_community set whether_top = 0 where user_id=${userId}")
    int cancelTopCircle(@Param("userId") int userId);
}
