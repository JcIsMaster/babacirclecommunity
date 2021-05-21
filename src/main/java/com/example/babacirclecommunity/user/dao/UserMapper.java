package com.example.babacirclecommunity.user.dao;

import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import com.example.babacirclecommunity.user.vo.PersonalUserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MQ
 * @date 2021/5/20 19:48
 */
@Component
public interface UserMapper {

    /**
     * 根据用户id查询个别用户字段信息
     * @param userId 当前登录id
     * @return
     */
    @Select("select id,user_name,introduce,picture,avatar from tb_user where id=${userId}")
    PersonalCenterUserVo queryUserById(@Param("userId") int userId);

    /**
     * 根据用户名称模糊查询
     * @param userName 用户名称
     * @param paging
     * @return
     */
    @Select("select id,user_name,introduce,avatar from tb_user where user_name like CONCAT('%',#{userName},'%') ${paging}")
    List<PersonalUserVo> queryUserLike(@Param("userName") String userName, @Param("paging") String paging);
}
