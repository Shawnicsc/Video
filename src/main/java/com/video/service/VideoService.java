package com.video.service;

import com.video.commom.Result;
import com.video.domain.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 13627
* @description 针对表【video】的数据库操作Service
* @createDate 2023-11-07 13:24:32
*/
public interface VideoService extends IService<Video> {
    Result upload(String title, String description, MultipartFile file, Integer request);

}
