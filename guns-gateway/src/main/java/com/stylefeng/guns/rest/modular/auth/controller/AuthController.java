package com.stylefeng.guns.rest.modular.auth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthRequest;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthResponse;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 请求验证的
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@RestController
public class AuthController {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;



    @Reference(interfaceClass = UserAPI.class)
    private UserAPI userAPI ;



    @RequestMapping(value = "${jwt.auth-path}")
    public ResponseVo createAuthenticationToken(AuthRequest authRequest) {
        boolean validate = true ; //reqValidator.validate(authRequest);
        // 去掉 guns 自身 携带的 验证 ，用自己写的 登陆验证

        logger.info("authRequest.getUserName()={}",authRequest.getUserName());

        logger.info("authRequest.getPassword()={}",authRequest.getPassword());

        int userId =  userAPI.login(authRequest.getUserName(),authRequest.getPassword());

        logger.info("userId={}",userId);

        if (userId ==0){
           validate = false ;
        }

        if (validate) {
            final String randomKey = jwtTokenUtil.getRandomKey();
            final String token = jwtTokenUtil.generateToken(""+userId, randomKey);

            return ResponseVo.success(new AuthResponse(token, randomKey));
//            return ResponseEntity.ok(new AuthResponse(token, randomKey));
        } else {
            return ResponseVo.serviceFail("账号密码错误");
           // throw new GunsException(BizExceptionEnum.AUTH_REQUEST_ERROR);
        }
    }




}
