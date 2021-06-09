package com.example.babacirclecommunity.user.controller;

import com.example.babacirclecommunity.common.constanct.CodeType;
import com.example.babacirclecommunity.common.exception.ApplicationException;
import com.example.babacirclecommunity.user.entity.User;
import com.example.babacirclecommunity.user.service.IUserService;
import com.example.babacirclecommunity.user.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author MQ
 * @date 2021/5/20 19:47
 */
@Api(tags = "用户API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/UserController")
public class UserController {


    @Autowired
    private IUserService iUserService;

    /**
     * 小程序登陆
     * @return
     */
    @ApiOperation(value = "小程序登陆",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/wxLogin")
    public User wxLogin(String code, String userName, String avatar, String address, int sex) {
        if("undefined".equals(code)){
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        return iUserService.wxLogin(code,userName,avatar,address,sex);
    }

    /**
     * 查询用户id，名称，头像
     * @return
     */
    @ApiOperation(value = "查询用户id，名称，头像",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/QueryUserInformationBasedUserId")
    public UserVo QueryUserInformationBasedUserId(int id, int userId) {
        if (id == 0) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }

        return iUserService.QueryUserInformationBasedUserId(id, userId);

    }

    /**
     * 查询用户信息
     * @return
     */
    @ApiOperation(value = "查询用户信息",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryUserById")
    public User queryUserById(int userId) {
        if (userId == 0) {
            throw new ApplicationException(CodeType.PARAMETER_ERROR);
        }
        return iUserService.queryUserById(userId);

    }



}
