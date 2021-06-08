package com.example.babacirclecommunity.my.dao;

import com.example.babacirclecommunity.my.vo.PeopleCareAboutVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
}
