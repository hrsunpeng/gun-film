package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserMoel;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user/")
@RestController
public class UserController {

    @Reference(interfaceClass = UserAPI.class)
    private UserAPI userAPI;


    @RequestMapping(value = "register",method = RequestMethod.POST)
    public ResponseVo register(UserMoel userMoel){
        if (userMoel.getUsername() ==null || userMoel.getUsername().trim().length()==0){
            return  ResponseVo.serviceFail("用户名不能为空");
        }

        if (userMoel.getPassword() ==null || userMoel.getPassword().trim().length()==0){
            return  ResponseVo.serviceFail("密码不能为空");
        }
        boolean register = userAPI.register(userMoel);
        if (register){
            return ResponseVo.success("注册成功");
        }else{
            return  ResponseVo.serviceFail("请重新注册");
        }
    }

    @RequestMapping(value = "check",method = RequestMethod.POST)
    public ResponseVo check(String  username){
        if (username !=null || username.length() > 0){
            boolean isExists = userAPI.checkUsername(username);
            if (isExists){
                return ResponseVo.success("用户名不存在");
            }else{
                return ResponseVo.serviceFail("用户名已存在");
            }
        }else{
            return  ResponseVo.serviceFail("用户名不能为空");
        }
    }

    @RequestMapping(value = "logout",method = RequestMethod.GET)
    public ResponseVo logout(){
        /**
         * 应用 :
         *      1.前端存储JWT【7天】，jwt的刷新
         *      2.服务器会存储活动用户信息【30分钟】
         *      3.jwt里面的userid为Key，查找活跃用户
         *
         * 退出：
         *      1.前端删除jwt
         *      2.后端服务器删除活跃用户的缓存
         *
         */
        return ResponseVo.success("用户退出成功");
    }

    @RequestMapping(value = "getUserInfo",method = RequestMethod.GET)
    public ResponseVo getUserInfo(){

        String currentUser = CurrentUser.getCurrentUser();
        if (currentUser != null && currentUser.trim().length()>0){
            UserInfoModel userInfo = userAPI.getUserInfo(currentUser);
            if (userInfo!=null){
                return ResponseVo.success(userInfo);
            }else{
                return ResponseVo.serviceFail("用户信息获取失败");
            }
        }
        return ResponseVo.serviceFail("用户未登陆");

    }




    @RequestMapping(value = "updateUserInfo",method = RequestMethod.POST)
    public ResponseVo updateUserInfo(UserInfoModel userInfoModel){
        String currentUser = CurrentUser.getCurrentUser();
        if (currentUser != null && currentUser.trim().length()>0){

            if (!currentUser.equals(userInfoModel.getUuid()+"")){
                return ResponseVo.serviceFail("请修改您个人的信息");
            }
            UserInfoModel userInfo = userAPI.updateUserInfo(userInfoModel);
            if (userInfo!=null){
                return ResponseVo.success(userInfo);
            }else{
                return ResponseVo.serviceFail("用户信息修改失败");
            }
        }
        return ResponseVo.serviceFail("用户未登陆");



    }





}
