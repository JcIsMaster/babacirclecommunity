package com.example.babacirclecommunity.user.dao;

import com.example.babacirclecommunity.user.entity.User;
import com.example.babacirclecommunity.user.vo.PersonalCenterUserVo;
import com.example.babacirclecommunity.user.vo.PersonalUserVo;
import com.example.babacirclecommunity.user.vo.UserPersonalVo;
import com.example.babacirclecommunity.user.vo.UserVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
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
    @Select("select id,user_name,user_sex,curr_province,city,introduce,avatar,birthday from tb_user where id=${userId}")
    PersonalCenterUserVo queryUserById(@Param("userId") int userId);

    /**
     * 根据用户id查询货源用户字段信息
     * @param userId 当前登录id
     * @return
     */
    @Select("select id,user_name,city,user_sex,birthday,avatar from tb_user " +
            "where id=${userId}")
    UserPersonalVo queryResourceUserById(@Param("userId") int userId);

    /**
     * 根据用户id查询合作用户字段信息
     * @param userId 当前登录id
     * @return
     */
    @Select("select a.id,a.user_name,a.city,a.user_sex,a.birthday,a.avatar,b.collaborate_introduce as resource_introduce from tb_user a left join tb_collaborate_introduce b " +
            "on a.id = b.user_id where a.id=${userId}")
    UserPersonalVo queryCollaborateUserById(@Param("userId") int userId);

    /**
     * 根据用户id查询个别用户字段信息
     * @param userId 当前登录id
     * @return
     */
    @Select("select * from tb_user where id=${userId}")
    User queryAllUser(@Param("userId") int userId);

    /**
     * 根据用户名称模糊查询
     * @param userName 用户名称
     * @param paging
     * @return
     */
    @Select("select id,user_name,introduce,avatar from tb_user where user_name like CONCAT('%',#{userName},'%') ${paging}")
    List<PersonalUserVo> queryUserLike(@Param("userName") String userName, @Param("paging") String paging);

    /**
     * 根据openid查询用户是否存在
     * @param openId
     * @return
     */
    @Select("select a.id,a.user_name,a.user_sex,a.avatar,a.introduce,a.birthday,a.curr_province,a.city,a.county,a.open_id,a.is_delete,a.picture,b.can_withdraw_gold_coins,b.may_not_withdraw_gold_coins," +
            "c.level,c.current_total_points from tb_user a inner join tb_user_gold_coins b on a.id = b.user_id left join tb_honored c on a.id = c.user_id where open_id=#{openId}")
    User selectUserByOpenId(@Param("openId") String openId);


    /**
     * 查询用户表中的最大id
     * @return
     */
    @Select("select max(id) from tb_user")
    int selectMaxId();

    /**
     * 增加用户信息
     * @param user 用户对象
     * @return
     */
    @Insert("insert into tb_user(user_name,open_id,user_sex,avatar,m_code,create_at)values(#{user.userName},#{user.openId},${user.userSex},#{user.avatar},#{user.mCode},#{user.createAt})")
    @Options(useGeneratedKeys=true, keyProperty="user.id", keyColumn="id")
    int addUser(@Param("user") User user);

    /**
     *  查询用户信息和金币信息
     * @param id
     * @return
     */
    @Select("select a.picture,a.id,a.user_name,a.user_sex,a.birthday,a.avatar,a.introduce,a.curr_province,a.city,a.county,a.open_id," +
            "b.can_withdraw_gold_coins,b.may_not_withdraw_gold_coins,c.level,c.current_total_points from tb_user a inner join tb_user_gold_coins b " +
            "on a.id=b.user_id left join tb_honored c on a.id = c.user_id where a.id = ${id}")
    User selectUserById(@Param("id") int id);

    /**
     * 根据id查询部分用户信息
     * @param id 他人id
     * @return
     */
    @Select("select id,user_name,avatar from tb_user where id=${id}")
    UserVo queryUserPartialInformation(@Param("id") int id);

    /**
     * 根据openid查询用户id
     * @param openid
     * @return
     */
    @Select("select id from tb_user where open_id=#{openid}")
    Integer queryUserIdByOpenId(@Param("openid") String openid);
}
