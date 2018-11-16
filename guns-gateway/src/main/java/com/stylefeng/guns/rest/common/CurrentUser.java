package com.stylefeng.guns.rest.common;

public class CurrentUser {

    //线程绑定的存储空间
    private  static  final  ThreadLocal<String> threadLocal = new ThreadLocal<>();

    //将用户的ID 放入存储空间
    public static  void saveUserId(String userId){
        threadLocal.set(userId);
    }

    //将用户的ID 从存储空间中取出来
    public  static  String  getCurrentUser(){
        return threadLocal.get();
    }

}
