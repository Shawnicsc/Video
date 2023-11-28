package com.video.commom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author shawni
 * @Description 为接口封装统一的返回类
 * @Date 2023/11/5 21:03
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Result success(){
        return new Result(Constants.CODE_200,"",null);
    }

    public static Result success(String msg){
        return new Result(Constants.CODE_200,msg,null);
    }
    public static Result success(Object data){
        return new Result(Constants.CODE_200,"",data);
    }

    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }

    public static Result error(){
        return new Result(Constants.CODE_500,"系统错误",null);
    }
}
