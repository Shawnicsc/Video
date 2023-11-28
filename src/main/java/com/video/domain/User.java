package com.video.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户 ID
     */
    @TableId(value = "UserID" ,type = IdType.AUTO)

    private Integer userId;

    /**
     * 用户名
     */
    @TableField("UserName")
    private String userName;

    /**
     * 用户密码
     */
    @TableField("Password")
    private String password;

    /**
     * 创建时间
     */
    @TableField("CreationTime")
    private Date creationTime;

    /**
     * 逻辑删除
     */
    @TableField("IsDelete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}