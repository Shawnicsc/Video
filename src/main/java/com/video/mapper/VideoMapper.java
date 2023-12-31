package com.video.mapper;

import com.video.domain.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13627
* @description 针对表【video】的数据库操作Mapper
* @createDate 2023-11-07 13:24:32
* @Entity com.video.domain.Video
*/
@Mapper
public interface VideoMapper extends BaseMapper<Video> {

}




