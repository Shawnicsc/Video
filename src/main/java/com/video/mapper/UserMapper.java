package com.video.mapper;

import com.video.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13627
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-11-05 20:55:39
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




