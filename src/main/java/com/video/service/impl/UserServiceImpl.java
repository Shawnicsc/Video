package com.video.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.Exception.MyException;
import com.video.commom.Constants;
import com.video.commom.Result;
import com.video.domain.User;
import com.video.domain.userDTO;
import com.video.service.UserService;
import com.video.mapper.UserMapper;
import org.springframework.stereotype.Service;
import sun.security.util.Password;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.video.commom.Constants.SALT;

/**
* @author 13627
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-11-05 20:55:39
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    private static final Log LOG = Log.get();



    // 登录业务
    @Override
    public Result login(User user, HttpServletRequest request) {
        /*
        校验
        1. 用户名不少于5位
        2. 密码长度不少于8位，且只含大小写字母和数字
         */
        if(StrUtil.hasEmpty(user.getUserName(),user.getPassword()))
            return Result.error(Constants.CODE_400,"参数错误");

        if(user.getUserName().length() < 5 || user.getPassword().length() < 8)
            return Result.error(Constants.CODE_400,"密码长度错误");

        if(!regexMatch(user.getPassword()))
            return Result.error(Constants.CODE_400,"参数错误");

        User userInfo = getUserInfo(user);
        if(userInfo != null){
            // 用户脱敏
            userDTO safeUser = new userDTO();
            safeUser.setUserId(userInfo.getUserId());
            safeUser.setUserName(userInfo.getUserName());

            //记录用户登录态
            request.getSession().setAttribute(Constants.USER_LOGIN_STATE,safeUser);
            return Result.success(safeUser);
        }
        else
            throw new MyException(Constants.CODE_400,"用户名或密码错误");
    }

    // 注册业务
    @Override
    public Result register(User user) {
        if(StrUtil.hasEmpty(user.getUserName(),user.getPassword()))
            return Result.error(Constants.CODE_400,"参数错误");

        if(user.getUserName().length() < 5 || user.getPassword().length() < 8)
            return Result.error(Constants.CODE_400,"参数长度错误");

        if(!regexMatch(user.getPassword()))
            return Result.error(Constants.CODE_400,"参数错误");


        // 用户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Username",user.getUserName());
        long count = this.count(queryWrapper);
        if(count > 0)
            return Result.error(Constants.CODE_600,"用户已存在");

        //对密码进行加盐处理
        String newPassword = DigestUtil.md5Hex((user.getPassword() + SALT).getBytes(StandardCharsets.UTF_8));

        User safeUser = new User();
        safeUser.setUserName(user.getUserName());
        safeUser.setPassword(newPassword);

        boolean flag = this.save(safeUser);

        if(!flag)
            return Result.error(Constants.CODE_500,"数据库保存错误");

        return Result.success("注册成功");

    }


    // 正则校验
    private Boolean regexMatch(String str){
        String pattern = "^[a-zA-Z0-9]+$";
        // 编译正则表达式模式
        Pattern regexPattern = Pattern.compile(pattern);
        // 创建匹配器
        Matcher matcher = regexPattern.matcher(str);

        return matcher.matches();
    }

    // 查询用户是否存在
    private User getUserInfo(User user){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("Username",user.getUserName());
        userQueryWrapper.eq("Password",DigestUtil.md5Hex((user.getPassword() + SALT).getBytes(StandardCharsets.UTF_8)));
        User one;

        try {
            one = getOne(userQueryWrapper);
        }catch(Exception e) {
            LOG.error(e);
            throw new MyException(Constants.CODE_500, "系统错误");
        }
        return one;
    }
}




