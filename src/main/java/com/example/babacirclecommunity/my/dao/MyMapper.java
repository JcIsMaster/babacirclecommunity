package com.example.babacirclecommunity.my.dao;

import com.example.babacirclecommunity.my.entity.ComplaintsSuggestions;
import com.example.babacirclecommunity.my.vo.PeopleCareAboutVo;
import com.example.babacirclecommunity.user.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/6/8 11:15
 */
@Component
public interface MyMapper {

    /**
     * 根据用户id查询关注的人
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select b.id,b.user_name,b.avatar,b.introduce from tb_user_attention a INNER JOIN tb_user b on a.bg_id=b.id where a.gu_id=${userId} and a.is_delete=1 ${paging}")
    List<PeopleCareAboutVo> queryPeopleCareAbout(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 根据用户id查询我的粉丝
     * @param userId 用户id
     * @param paging 分页
     * @return
     */
    @Select("select b.id,b.user_name,b.avatar,b.introduce from tb_user_attention a INNER JOIN tb_user b on a.gu_id=b.id where a.bg_id=${userId} and a.is_delete=1 ")
    List<PeopleCareAboutVo> queryFan(@Param("userId") int userId,@Param("paging") String paging);

    /**
     * 添加投诉与建议新
     * @param complaintsSuggestions 投诉与建议新对象
     * @return
     */
    @Insert("insert into tb_complaints_suggestions(content,user_id,create_at)values(#{complaintsSuggestions.content},${complaintsSuggestions.userId},#{complaintsSuggestions.createAt})")
    int addComplaintsSuggestions(@Param("complaintsSuggestions") ComplaintsSuggestions complaintsSuggestions );

    /**
     * 添加观看记录
     * @param bUserId 被观看人id
     * @param gUserId 观看人id
     * @param createAt 创建时间
     * @return
     */
    @Insert("insert into tb_viewing_record(viewers_id,beholder_id,create_at)values(${gUserId},${bUserId},#{createAt})")
    int addViewingRecord(@Param("bUserId") int bUserId,@Param("gUserId") int gUserId,@Param("createAt") String createAt);

    /**
     * 查询观看我的人
     * @param userId 被观看人id
     * @param paging 分页
     * @return
     */
    @Select("select b.id,b.avatar,b.user_name,b.introduce,a.create_at from (select DISTINCT * from tb_viewing_record  ORDER BY create_at desc) a INNER JOIN tb_user b on a.viewers_id=b.id where  a.beholder_id=${userId} GROUP BY a.viewers_id ORDER BY a.create_at desc ${paging}")
    List<User> queryPeopleWhoHaveSeenMe(@Param("userId") int userId, @Param("paging") String paging);

    /**
     * 修改用户信息
     * @param sql 拼接内容
     * @param id 用户id
     * @return
     */
    @Update("update tb_user set ${sql} where id=${id}")
    int updateUserMessage(@Param("sql") String sql,@Param("id") int id);
}
