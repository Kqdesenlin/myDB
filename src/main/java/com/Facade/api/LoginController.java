package com.Facade.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: zhangQY
 * @date: 2021/3/19
 * @description:登录控制
 */
@RestController
public class LoginController {

    @GetMapping("/index")
    public String login(){
        return "login";
    }
}
