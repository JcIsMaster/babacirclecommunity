package com.example.babacirclecommunity.home.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.home.service.IHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author JC
 * @date 2021/5/20 13:30
 */
@Api(tags = "首页API")
@CrossOrigin(origins = "*", maxAge = 100000)
@RestController
@Slf4j
@RequestMapping("/HomeController")
public class HomeController {

    @Autowired
    private IHomeService iHomeService;

    /**
     * 搜索数据接口
     * @return
     */
    @ApiOperation(value = "搜索数据接口",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/selectAllSearch")
    public Object selectAllSearch(int strata,String postingName,int userId, Paging paging)  {
        if(paging.getPage()==0){
            throw new ApplicationException(CodeType.PARAMETER_ERROR,"page不要传0，page传1");
        }

        return iHomeService.selectAllSearch(strata,postingName,userId, paging);
    }

    /**
     * 查询搜索记录和其他相关信息
     * @return
     */
    @ApiOperation(value = "查询搜索记录和其他相关信息",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/querySearchRecords")
    public Map<String,Object> querySearchRecords(int userId)  {
        return iHomeService.querySearchRecords(userId);
    }
}
