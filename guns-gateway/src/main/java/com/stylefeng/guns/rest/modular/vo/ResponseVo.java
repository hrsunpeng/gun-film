package com.stylefeng.guns.rest.modular.vo;

import lombok.Data;

/**
 * 返回响应码
 * @param <M>
 */
@Data
public class ResponseVo<M> {

    private  int  status ; // 【0 表示成功  1 表示业务失败 999 表示系统错误】

    private  String  msg ;

    private  M  data;

    private  String imgPre;

    private ResponseVo(){}


    public static<M> ResponseVo success(M data){
            ResponseVo responseVo = new ResponseVo();
            responseVo.setStatus(0);
            responseVo.setMsg("成功");
            responseVo.setData(data);
            return responseVo;
    }

    public static<M> ResponseVo success(String imgPre,M data){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(0);
        responseVo.setMsg("成功");
        responseVo.setData(data);
        responseVo.setImgPre(imgPre);
        return responseVo;
    }


    public static<M> ResponseVo success(String msg){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(0);
        responseVo.setMsg(msg);
        return responseVo;
    }

    public static<M> ResponseVo serviceFail(String msg){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(1);
        responseVo.setMsg(msg);
        return responseVo;
    }

    public static<M> ResponseVo appFail(String msg){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(999);
        responseVo.setMsg(msg);
        return responseVo;
    }



}
