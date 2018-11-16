package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserMoel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MoocUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocUserT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 *
 *  loadbalance = "roundrobin"   负载均衡 轮询
 */
@Component
@Service(interfaceClass = UserAPI.class,loadbalance = "roundrobin")
public class UserServiceImpl implements UserAPI {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private MoocUserTMapper moocUserTMapper;

    @Override
    public int login(String username, String password) {

        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(username);
        MoocUserT result = moocUserTMapper.selectOne(moocUserT);

        if (result != null && result.getUuid()>0){
            String md5InputPass = MD5Util.encrypt(password);
            String  dbPass = result.getUserPwd();
            if (md5InputPass.equals(dbPass)){
                return  result.getUuid();
            }
        }
        return 0;
    }

    @Override
    public boolean register(UserMoel userMoel) {

        //将注册信息主体转换为数据实体
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(userMoel.getUsername());
        //密码加密
        moocUserT.setUserPwd(MD5Util.encrypt(userMoel.getPassword()));
        moocUserT.setEmail(userMoel.getEmail());
        moocUserT.setAddress(userMoel.getAddress());
        moocUserT.setUserPhone(userMoel.getPhone());
        //将数据实体存入数据库
        Integer insert = moocUserTMapper.insert(moocUserT);
        if (insert>0){
            return true ;
        }else{
            return false;
        }

    }

    @Override
    public boolean checkUsername(String username) {
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(username);
        MoocUserT result = moocUserTMapper.selectOne(moocUserT);
        if (result!=null){
            return false;
        }else{
            return true;
        }

    }


    private UserInfoModel do2UserInfo(MoocUserT moocUserT){
        UserInfoModel user = new UserInfoModel();
        user.setUuid(moocUserT.getUuid());
        user.setUsername(moocUserT.getUserName());
        user.setNickname(moocUserT.getNickName());
        user.setEmail(moocUserT.getEmail());
        user.setPhone(moocUserT.getUserPhone());
        user.setSex(moocUserT.getUserSex());
        user.setBirthday(moocUserT.getBirthday());
        user.setLifeState(moocUserT.getLifeState()+"");
        user.setBiography(moocUserT.getBiography());
        user.setAddress(moocUserT.getAddress());
        user.setHeadAddress(moocUserT.getHeadUrl());
        user.setUpdateTime(moocUserT.getUpdateTime().getTime());
        user.setCreateTime(moocUserT.getBeginTime().getTime());
        return  user;
    }


    @Override
    public UserInfoModel getUserInfo(String uuid) {
        MoocUserT moocUserT = moocUserTMapper.selectById(Integer.parseInt(uuid));
        UserInfoModel userInfoModel = do2UserInfo(moocUserT);
        return userInfoModel;
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {

        logger.info("修改用户信息 -- >userInfoModel={}", JSON.toJSON(userInfoModel));

        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUuid(userInfoModel.getUuid());
        moocUserT.setUserName(userInfoModel.getUsername());
        moocUserT.setUpdateTime(new Date());
        moocUserT.setNickName(userInfoModel.getNickname());
        moocUserT.setLifeState(Integer.valueOf(userInfoModel.getLifeState()));
        moocUserT.setHeadUrl(userInfoModel.getHeadAddress());
        moocUserT.setBirthday(userInfoModel.getBirthday());
        moocUserT.setBiography(userInfoModel.getBiography());
       // moocUserT.setBeginTime(new Date(userInfoModel.getCreateTime()));
        moocUserT.setEmail(userInfoModel.getEmail());
        moocUserT.setAddress(userInfoModel.getAddress());
        moocUserT.setUserPhone(userInfoModel.getPhone());

        Integer integer = moocUserTMapper.updateById(moocUserT);

        logger.info("修改用户信息result -- >integer={}",integer);

        if (integer>0){
            UserInfoModel userInfo = getUserInfo(moocUserT.getUuid() + "");
            return userInfo;
        } else{
            return userInfoModel;
        }

    }
}
