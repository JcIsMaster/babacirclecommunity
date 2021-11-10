package com.example.babacirclecommunity.sameCity.controller;

import com.example.babacirclecommunity.common.utils.Paging;
import com.example.babacirclecommunity.sameCity.service.SameCityService;
import com.example.babacirclecommunity.sameCity.vo.SameCityUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author JC
 * @date 2021/10/23 11:39
 */
@Api(tags = "同城API")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/SameCityController")
public class SameCityController {

    @Autowired
    private SameCityService sameCityService;

    @ApiOperation(value = "查询同城用户列表",notes = "成功返回数据 反则为空")
    @ResponseBody
    @PostMapping("/queryFriendsInTheSameCity")
    public List<SameCityUser> queryFriendsInTheSameCity(int isShop, String city, Paging paging) {
        return sameCityService.queryFriendsInTheSameCity(isShop, city, paging);
    }
}
