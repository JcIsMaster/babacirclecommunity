package com.example.babacirclecommunity.sameCity.dao;

import com.example.babacirclecommunity.sameCity.vo.SameCityUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/10/23 14:38
 */
@Component
public interface SameCityMapper {

    /**
     * 查询同城商家用户列表
     * @param city
     * @param sql
     * @return
     */
    @Select("select a.id,a.user_id,b.user_name,b.user_sex,b.avatar,IFNULL(ROUND(DATEDIFF(CURDATE(), b.birthday)/365.2422),0) as userAge," +
            "a.json_class-> '$.is_shops' as isShops,a.json_class-> '$.platform' as platform,a.json_class-> '$.platformLogo' as platformLogo,a.json_class-> '$.stor' as stor," +
            "(select (count(1)*10) from tb_user_attention where bg_id=a.user_id and is_delete=1) as popularityValue from tb_parameter a " +
            "left join tb_user b on a.user_id = b.id " +
            "where a.json_class-> '$.is_shops' = 0 and b.city = #{city} ORDER BY popularityValue desc ${sql}")
    List<SameCityUser> queryIsShopInTheSameCity(@Param("city") String city, @Param("sql") String sql);

    /**
     * 查询同城非商家用户列表
     * @param city
     * @param sql
     * @return
     */
    @Select("select a.id,a.user_id,b.user_name,b.user_sex,b.avatar,IFNULL(ROUND(DATEDIFF(CURDATE(), b.birthday)/365.2422),0) as userAge," +
            "a.json_class-> '$.is_shops' as isShops,a.json_class-> '$.is_status' as isStatus,a.json_class-> '$.job' as job," +
            "a.json_class-> '$.status' as status," +
            "(select (count(1)*10) from tb_user_attention where bg_id=a.user_id and is_delete=1) as popularityValue from tb_parameter a " +
            "left join tb_user b on a.user_id = b.id " +
            "where a.json_class-> '$.is_shops' = 1 and b.city = #{city} ORDER BY popularityValue desc ${sql}")
    List<SameCityUser> queryNoShopInTheSameCity(@Param("city") String city, @Param("sql") String sql);

    /**
     * 根据用户id查询用户选择参数
     * @param userId
     * @return
     */
    @Select("select json_class from tb_parameter where user_id = ${userId}")
    String queryParameterByUserId(@Param("userId") int userId);
}
