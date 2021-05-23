package com.example.babacirclecommunity.tags.dao;

import com.example.babacirclecommunity.home.entity.Community;
import com.example.babacirclecommunity.tags.entity.Tag;
import com.example.babacirclecommunity.tags.vo.AllTagVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author JC
 * @date 2021/1/21 16:00
 */
@Component
public interface TagMapper {



    /**
     * 根据传的值查询对应的一级标签
     * @param type 0资源 1圈子
     * @return
     */
    @Select("select * from tb_tag where is_delete=1 and type=${type}")
    List<Tag> selectResourcesAllTag(@Param("type") int type);

    /**
     *  根据传的值查询对应的二级标签
     * @param type 0资源 1圈子
     * @return
     */
    @Select("select * from tb_tags where is_delete=1 and type=${type}")
    List<Tag> queryCorrespondingSecondaryLabel(@Param("type") int type);

    /**
     * 根据一级标签id查询二级标签
     * @param tid 一级标签id
     * @return
     */
    @Select("select * from tb_tags where t_id=${tid} and is_delete=1")
    List<Tag> selectResourcesAllTags(@Param("tid") int tid);



    /**
     * 根据二级标签id查询一级标签
     * @param id 二级标签id
     * @return
     */
    @Select("select t_id from tb_tags where id=${id} and is_delete=1")
    int queryLabelAccordingSecondaryId(@Param("id") int id);


    /**
     * 根据二级标签组查询每个标签发过多少个帖子
     *
     * @param id   标签id组
     * @param tableName 表名
     * @return
     */
    @Select("select COALESCE(count(a.tags_two),0) from ${tableName} a  where a.tags_two=${id} and a.is_delete=1")
    int selectTagsNum(@Param("id") int id, @Param("tableName") String tableName);

    /**
     * 查询所有标签
     * @return
     */
    @Select("select * from tb_tag")
    List<AllTagVo> queryAllTag();

    /**
     * 根据一级标签查询所有二级标签下面的帖子数量
     * @param id 一级标签id
     * @param str 表明
     * @return
     */
    @Select("SELECT d.id,d.tag_name,d.img_url,IFNULL(t1.count1, 0) AS num FROM tb_tags d  LEFT JOIN " +
            "(SELECT tags_two,COUNT(*) AS count1 FROM ${str} where is_delete=1 GROUP BY tags_two) t1" +
            " on d.id=t1.tags_two where d.t_id=${id} ORDER BY d.id;")
    List<Tag> queryHowManyPostsAreInEachCell(@Param("str") String str, @Param("id") int id);


    /**
     * 添加二级标签
     * @param tag
     * @return
     */
    @Insert("insert into tb_tags (tag_name,t_id,type,create_at,img_url)values(#{tag.tagName},${tag.tId},${tag.type},#{tag.createAt},#{tag.imgUrl})")
    @Options(useGeneratedKeys=true, keyProperty="tag.id", keyColumn="id")
    int addTag(@Param("tag") Tag tag);




    /**
     * 添加标签和导航栏的中间表
     * @param tagId 标签id 对应tb_tags表中的id
     * @param haplontId 单元体id 对应 tb_haplont表中的id
     * @return
     */
    @Insert("insert into tb_tag_haplont(tag_id,haplont_id)values(${tagId},${haplontId})")
    int addTagHaplont(@Param("tagId") int tagId,@Param("haplontId") int haplontId);


}
