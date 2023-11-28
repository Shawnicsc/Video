package com.video.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName video
 */
@TableName(value ="video")
@Data
public class Video implements Serializable {
    /**
     * 视频 Id
     */
    @TableId(type = IdType.AUTO)
    private Integer videoId;

    /**
     * 作者Id
     */
    @TableField("UserID")
    private Integer userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 视频文件
     */
    @TableField("VideoFile")
    private String videoFile;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}