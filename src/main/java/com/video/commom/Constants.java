package com.video.commom;

/**
 *
 * @author shawni
 * @date 2023/11/5 21:03
 * 状态码
 */

public interface Constants {
    String USER_LOGIN_STATE = "USER_LOGIN_STATE";
    String CODE_200 = "200"; // 成功
    String CODE_500 = "500"; // 系统错误
    String CODE_401 = "401"; // 权限不足
    String CODE_400 = "400"; // 参数错误
    String CODE_600 = "600"; //其他业务异常
    String CODE_402 = "402"; // 文件转化异常
    String CODE_403 = "403"; // 关键帧提取异常

    static final String SALT = "LOVEu"; //盐值
}
