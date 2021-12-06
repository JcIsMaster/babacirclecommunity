package com.example.babacirclecommunity.resourceMatch.contorller;

import com.example.babacirclecommunity.common.utils.ResultUtil;
import com.example.babacirclecommunity.resourceMatch.service.MatchUserService;
import com.example.babacirclecommunity.resourceMatch.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Api(tags = "匹配用户")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/MatchUserController")
public class MatchUserController {

    @Autowired
    private MatchUserService matchUserService;

    @ApiOperation(value = "匹配用户",notes="匹配用户")
    @ResponseBody
    @PostMapping("/parameter")
    public ResultUtil findResources(int type, int userId){
        //根据用户id查询目标用户填写的参数
        Parameter parameters = matchUserService.matchingByUserId(userId);
        int count=0;


        //新建一个集合，用于存放返回值，返回类型是Json实体类，即目标参数表中的json_class中的值
        List<JsonClass> jsonClasses =new ArrayList<>();
        List<UserInformation> userInformations = new ArrayList<>();



        //新建一个目标
        List<Parameter> parameters1 = null;
        if (parameters == null){
            return ResultUtil.success("");
        }

        JSONObject obj = JSONObject.fromObject(parameters.getJsonClass());
        JsonClass jb = (JsonClass) JSONObject.toBean(obj, JsonClass.class);

        String sql="";
        String str="ORDER BY RAND()";

        if (type==0){
            if (jb.getTwoSelect().equals("找货源")){
                parameters1 = matchUserService.Parameter("%供货%",userId,sql,str);
                for (Parameter parameter:parameters1) {
                    UserInformation user = new UserInformation();
                    JSONObject obj1 = JSONObject.fromObject(parameter.getJsonClass());
                    JsonClass jb1 = (JsonClass) JSONObject.toBean(obj1, JsonClass.class);
                    user.setAvatar(parameter.getAvatar());
                    user.setAge(parameter.getAge());
                    user.setUserName(parameter.getUserName());
                    user.setUserId(parameter.getUserId());
                    user.setUserSex(parameter.getUserSex());
                    user.setTwoSelect(jb1.getTwoSelect());
                    jb1.setUserId(parameter.getUserId());
                    jsonClasses.add(jb1);
                    userInformations.add(user);
                    count = matchUserService.count("%供货%",userId,sql,str);
                }
            }else if (jb.getTwoSelect().equals("找流量")){
                parameters1 = matchUserService.Parameter("%找合作%",userId,sql,str);
                for (Parameter parameter:parameters1) {
                    UserInformation user = new UserInformation();
                    user.setAvatar(parameter.getAvatar());
                    user.setAge(parameter.getAge());
                    user.setUserName(parameter.getUserName());
                    user.setUserId(parameter.getUserId());
                    user.setUserSex(parameter.getUserSex());
                    JSONObject obj1 = JSONObject.fromObject(parameter.getJsonClass());
                    JsonClass jb1 = (JsonClass) JSONObject.toBean(obj1, JsonClass.class);
                    user.setTwoSelect(jb1.getTwoSelect());
                    jb1.setUserId(parameter.getUserId());
                    jsonClasses.add(jb1);
                    userInformations.add(user);
                    count = matchUserService.count("%找合作%",userId,sql,str);
                }
            }
            else if (jb.getTwoSelect().equals("学电商") || jb.getTwoSelect().equals("认识人脉")){
                sql=" or json_class like '%\"is_shops\":0%'";
                parameters1 = matchUserService.Parameter("\"%is_status\":0%",userId,sql,str);
                for (Parameter parameter:parameters1) {
                    UserInformation user = new UserInformation();
                    user.setAvatar(parameter.getAvatar());
                    user.setAge(parameter.getAge());
                    user.setUserName(parameter.getUserName());
                    user.setUserId(parameter.getUserId());
                    user.setUserSex(parameter.getUserSex());
                    JSONObject obj1 = JSONObject.fromObject(parameter.getJsonClass());
                    JsonClass jb1 = (JsonClass) JSONObject.toBean(obj1, JsonClass.class);
                    user.setTwoSelect(jb1.getTwoSelect());
                    jb1.setUserId(parameter.getUserId());
                    jsonClasses.add(jb1);
                    userInformations.add(user);
                    count = matchUserService.count("\"%is_status\":0%",userId,sql,str);
                }
            }
            else if (jb.getTwoSelect().equals("供货")){
                sql=" or json_class like '%\"is_shops\":0%' or json_class like '%\"is_status\":1%'";
                parameters1 = matchUserService.Parameter("%找货源%",userId,sql,str);
                for (Parameter parameter:parameters1) {
                    UserInformation user = new UserInformation();
                    user.setAvatar(parameter.getAvatar());
                    user.setAge(parameter.getAge());
                    user.setUserName(parameter.getUserName());
                    user.setUserId(parameter.getUserId());
                    user.setUserSex(parameter.getUserSex());
                    JSONObject obj1 = JSONObject.fromObject(parameter.getJsonClass());
                    JsonClass jb1 = (JsonClass) JSONObject.toBean(obj1, JsonClass.class);
                    user.setTwoSelect(jb1.getTwoSelect());
                    jb1.setUserId(parameter.getUserId());
                    jsonClasses.add(jb1);
                    userInformations.add(user);
                    count = matchUserService.count("%找货源%",userId,sql,str);
                }
            }
            else if (jb.getTwoSelect().equals("找合作")){
                sql = " or json_class like '%实习%'";
                parameters1 = matchUserService.Parameter("%供货%",userId,sql,str);
                for (Parameter parameter:parameters1) {
                    UserInformation user = new UserInformation();
                    user.setAvatar(parameter.getAvatar());
                    user.setAge(parameter.getAge());
                    user.setUserName(parameter.getUserName());
                    user.setUserId(parameter.getUserId());
                    user.setUserSex(parameter.getUserSex());
                    JSONObject obj1 = JSONObject.fromObject(parameter.getJsonClass());
                    JsonClass jb1 = (JsonClass) JSONObject.toBean(obj1, JsonClass.class);
                    user.setTwoSelect(jb1.getTwoSelect());
                    jb1.setUserId(parameter.getUserId());
                    jsonClasses.add(jb1);
                    userInformations.add(user);
                    count = matchUserService.count("%供货%",userId,sql,str);
                }
            }
            else if (jb.getTwoSelect().equals("实习")){
                sql=" or json_class like '%\"is_shops\":0%'";
                parameters1 = matchUserService.Parameter("%找人才%",userId,sql,str);
                for (Parameter parameter:parameters1) {
                    UserInformation user = new UserInformation();
                    user.setAvatar(parameter.getAvatar());
                    user.setAge(parameter.getAge());
                    user.setUserName(parameter.getUserName());
                    user.setUserId(parameter.getUserId());
                    user.setUserSex(parameter.getUserSex());
                    JSONObject obj1 = JSONObject.fromObject(parameter.getJsonClass());
                    JsonClass jb1 = (JsonClass) JSONObject.toBean(obj1, JsonClass.class);
                    user.setTwoSelect(jb1.getTwoSelect());
                    jb1.setUserId(parameter.getUserId());
                    jsonClasses.add(jb1);
                    count = matchUserService.count("%找人才%",userId,sql,str);
                    userInformations.add(user);
                }
            }
            else if (jb.getTwoSelect().equals("找人才")){
                sql = " or json_class like '%\"is_status\":0%'";
                parameters1 = matchUserService.Parameter("%实习%",userId,sql,str);
                for (Parameter parameter:parameters1) {
                    UserInformation user = new UserInformation();
                    user.setAvatar(parameter.getAvatar());
                    user.setAge(parameter.getAge());
                    user.setUserName(parameter.getUserName());
                    user.setUserId(parameter.getUserId());
                    user.setUserSex(parameter.getUserSex());
                    JSONObject obj1 = JSONObject.fromObject(parameter.getJsonClass());
                    JsonClass jb1 = (JsonClass) JSONObject.toBean(obj1, JsonClass.class);
                    user.setTwoSelect(jb1.getTwoSelect());
                    jb1.setUserId(parameter.getUserId());
                    jsonClasses.add(jb1);
                    userInformations.add(user);
                    count = matchUserService.count("%实习%",userId,sql,str);
                }
            }
        }else {
            String value=jb.getTwoSelect();
            String values="%"+value+"%";
            parameters1 = matchUserService.Parameter(values,userId,sql,str);
            for (Parameter parameter:parameters1) {
                UserInformation user = new UserInformation();
                user.setAvatar(parameter.getAvatar());
                user.setAge(parameter.getAge());
                user.setUserName(parameter.getUserName());
                user.setUserId(parameter.getUserId());
                user.setUserSex(parameter.getUserSex());
                JSONObject obj1 = JSONObject.fromObject(parameter.getJsonClass());
                JsonClass jb1 = (JsonClass) JSONObject.toBean(obj1, JsonClass.class);
                user.setTwoSelect(jb1.getTwoSelect());
                jb1.setUserId(parameter.getUserId());
                jsonClasses.add(jb1);
                userInformations.add(user);
                count = matchUserService.count(values,userId,sql,str);

            }
        }
        return ResultUtil.success(userInformations,count);
    }


    @ApiOperation(value = "新增/修改用户匹配信息",notes="新增/修改用户匹配信息")
    @ResponseBody
    @PostMapping("/insertParameter")
    public ResultUtil insertParameter(int userId,String text){
        Parameter parameter = matchUserService.ParameterById(userId);
        if (parameter != null){
            return ResultUtil.success(matchUserService.updateParameter(userId, text));
        }
        return ResultUtil.success(matchUserService.insertParameter(userId, text));
    }


    @ApiOperation(value = "我的信息",notes="我的信息")
    @ResponseBody
    @PostMapping("/myInformation")
    public ResultUtil myInformation(int userId){
        Parameter parameters = matchUserService.matchingByUserId(userId);
        if (parameters==null){
            return ResultUtil.success("");
        }
        JSONObject obj1 = JSONObject.fromObject(parameters.getJsonClass());
        JsonClass jb1 = (JsonClass) JSONObject.toBean(obj1, JsonClass.class);
        int age=parameters.getAge();
        if (age<0){
         age = 0;
        }


        MyInformation myInformation = new MyInformation();
        // 0 商家  1 职业  2 状态
        if(jb1.getIs_shops()==0){
            myInformation.setStauts(0);
            myInformation.setTextValue(jb1.getPlatform());
        }
        else if(jb1.getIs_status()==0&&jb1.getIs_shops()==1){
            myInformation.setStauts(1);
            myInformation.setTextValue(jb1.getJob());
        }
        else if(jb1.getIs_status()==1&&jb1.getIs_shops()==1){
            myInformation.setStauts(2);
            myInformation.setTextValue(jb1.getStatus());
        }

        myInformation.setAvatar(parameters.getAvatar());
        myInformation.setTwoSelect(jb1.getTwoSelect());
        myInformation.setUserId(parameters.getUserId());
        myInformation.setUserName(parameters.getUserName());
        myInformation.setUserSex(parameters.getUserSex());
        myInformation.setAge(age);
        myInformation.setThSelect(jb1.getThSelect());
        return ResultUtil.success(myInformation);
    }

}
