package com.stylefeng.guns.api.user;

import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserMoel;

public interface UserAPI {

    int login(String username, String password);

    boolean register(UserMoel userMoel);

    boolean checkUsername(String username);

    UserInfoModel getUserInfo(String  uuid);

    UserInfoModel updateUserInfo(UserInfoModel userInfoModel);


}
