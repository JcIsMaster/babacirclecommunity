package com.example.babacirclecommunity.learn.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.learn.entity.ClassOrder;
import com.example.babacirclecommunity.learn.service.IPublicClassService;
import com.example.babacirclecommunity.learn.vo.PublicClassVo;
import com.example.babacirclecommunity.personalCenter.vo.ClassPersonalVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @author JC
 * @date 2021/5/4 17:04
 */
@Api(tags = "公开课API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/PublicClassController")
public class PublicClassController {

    @Autowired
    private IPublicClassService iPublicClassService;

    /**
     * 根据id查询公开课详情
     * @param id
     * @param userId
     * @return
     * @throws ParseException
     */
    @ApiOperation(value = "根据id查询公开课详情",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryPublicClassById")
    public PublicClassVo queryPublicClassById(int id,int userId) throws Exception{
        if(id==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPublicClassService.queryPublicClassById(id,userId);
    }

    /**
     * 公开课收藏
     * @param id 公开课id
     * @param userId 收藏人id
     * @return
     */
    @ApiOperation(value = "收藏",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/giveCollect")
    public int giveCollect(int id, int userId){
        if(id==0 || userId==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iPublicClassService.giveCollect(id,userId);
    }

    /**
     * 公开课购买
     * @param classOrder
     * @return
     */
    @ApiOperation(value = "购买",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/buyerClass")
    public ResultUtil buyerClass(ClassOrder classOrder) throws Exception{
        return iPublicClassService.buyerClass(classOrder);
    }

    /**
     * 查询公开课个人中心
     * @param userId
     * @param otherId
     * @return
     */
    @ApiOperation(value = "查询公开课个人中心",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryClassPersonal")
    public ClassPersonalVo queryClassPersonal(int userId, int otherId, Paging paging){
        return iPublicClassService.queryClassPersonal(userId,otherId,paging);
    }

    /**
     * 得到公开课海报
     * @param id 帖子id
     * @param pageUrl 二维码指向的地址
     * @return
     */
    @ApiOperation(value = "得到公开课海报",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/getPublicClass")
    public List<String> getPublicClass(String id, String pageUrl){
        return iPublicClassService.getPublicClass(id,pageUrl);
    }
}
