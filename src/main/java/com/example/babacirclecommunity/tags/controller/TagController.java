package com.example.babacirclecommunity.tags.controller;

import com.example.babacirclecommunity.home.entity.Community;
import com.example.babacirclecommunity.tags.entity.Tag;
import com.example.babacirclecommunity.tags.service.ITagService;
import com.example.babacirclecommunity.tags.vo.AllTagVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author JC
 * @date 2021/1/21 16:07
 */
@Api(tags = "标签API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/TagController")
public class TagController {


    @Autowired
    private ITagService iTagService;


    /**
     * 根据参数类型查询不同类型的标签
     * @return
     */
    @ApiOperation(value = "根据参数类型查询不同类型的标签",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectResourcesTag")
    public List<Tag> selectResourcesTag(int type) {
        List<Tag> tags = iTagService.selectResourcesAllTag(type);
        return tags;
    }


    /**
     * 根据一级标签id查询二级标签
     * @return
     */
    @ApiOperation(value = "根据一级标签id查询二级标签",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectResourcesAllTags")
    public List<Tag> selectResourcesAllTags(int tid) {
        List<Tag> tags = iTagService.selectResourcesAllTags(tid);
        return tags;
    }


    /**
     * 查询所有一级标签和二级标签
     * @return
     */
    @ApiOperation(value = " 查询所有一级标签和二级标签",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryAllPrimaryAndSecondaryTags")
    public List<AllTagVo> queryAllPrimaryAndSecondaryTags() {
        return iTagService.queryAllPrimaryAndSecondaryTags();
    }




}
