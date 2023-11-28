package com.video.service;

import com.video.commom.Result;
import com.video.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
* @author 13627
* @description 针对表【user】的数据库操作Service
* @createDate 2023-11-05 20:55:39
*/

public interface UserService extends IService<User> {
    Result login(User user, HttpServletRequest request);

    Result register(User user);
}
