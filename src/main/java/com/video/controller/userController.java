package com.video.controller;

import com.video.commom.Constants;
import com.video.commom.Result;
import com.video.domain.User;
import com.video.domain.userDTO;
import com.video.service.UserService;
import com.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

/**
 * @Author shawni
 * @Description TODO
 * @Date 2023/11/5 21:01
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    private userDTO userDTO;

    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpServletRequest request){
        Result res = userService.login(user, request);
        Object login = request.getSession().getAttribute(Constants.USER_LOGIN_STATE);
        userDTO = (userDTO) login ;
        return res;
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        System.out.println(user);
        return userService.register(user);
    }

    /**
     * @TODO 测试接口 成功
     */
    @PostMapping("/upload")
    public Result upload(@PathParam("title") String title, @PathParam("description") String description, @PathParam("videoFile") MultipartFile videoFile, HttpServletRequest request){
        System.out.println(title);
        System.out.println(description);
        System.out.println(userDTO);
        return videoService.upload(title, description, videoFile, userDTO.getUserId());
    }
}
