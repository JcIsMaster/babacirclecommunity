package com.example.babacirclecommunity.honored.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.honored.service.IHonoredService;
import com.example.babacirclecommunity.honored.vo.UserHonoredCenterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author JC
 * @date 2021/11/10 11:21
 */
@Api(tags = "荣誉会员API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/HonoredController")
public class HonoredController {

    @Autowired
    private IHonoredService iHonoredService;

    /**
     * 查询用户荣誉权益中心
     * @return
     */
    @ApiOperation(value = "查询用户荣誉权益中心",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryUserHonoredCenter")
    public UserHonoredCenterVo queryUserHonoredCenter(int userId){
        if (userId == 0) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iHonoredService.queryUserHonoredCenter(userId);
    }

}
