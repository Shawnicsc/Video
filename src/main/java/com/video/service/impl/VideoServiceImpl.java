package com.video.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.Exception.MyException;
import com.video.commom.Constants;
import com.video.commom.Result;
import com.video.domain.Video;
import com.video.service.VideoService;
import com.video.mapper.VideoMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.video.utils.ExtractUtil.multipartFileToFile;
import static com.video.utils.ExtractUtil.video_process;

/**
* @author 13627
* @description 针对表【video】的数据库操作Service实现
* @createDate 2023-11-07 13:24:32
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{
    private static final String date;
    static {
        // 设置 文件保存路径  E:\Video\src\main\resources\static\video\日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        date = sdf.format(new Date());
    }


    private static final String videoPath = "E:\\Video\\src\\main\\resources\\static\\video"+ "\\" + date;
    /**
     * 上传视频，保存到本地目录，并进行数据库保存title的hash值作为标识，进行关键帧细粒度提取
     * @author shawni
     * @date 2023/11/24 11:48
     * @param title
     * @param description
     * @param file
     * @param userId
     * @return Result
     */
    @Override
    public Result upload(String title, String description, MultipartFile file, Integer userId) {

        if(file.isEmpty())
            return Result.error(Constants.CODE_400,"文件上传失败");

        Video video = new Video();
        try{
            String saveName = saveVideo(file);

            keyFrame(saveName);

            video.setTitle(title);

            video.setDescription(description);

            video.setUserId(userId);

            video.setVideoFile(saveName);

            boolean flag = this.save(video);
            if(flag)
                return Result.success("保存成功");

        }catch (Exception e){
            throw new MyException(Constants.CODE_500,"系统错误");
        }

        return Result.error(Constants.CODE_401,"保存失败");
    }
    // 视频保存到本地目录
    private String saveVideo(MultipartFile file){
        // 获取文件名称
        String originName = file.getOriginalFilename();

        System.out.println(originName);
        //控制文件格式
        Set<String> set = new HashSet<>();
        set.add(".mp4");

        // 获取文件名和文件类型
        int count = 0;
        for(int i = 0; i < originName.length(); i++){
            if(originName.charAt(i) == '.'){
                count = i;
                break;
            }
        }
        String endName = originName.substring(count);

        String fileType = originName.substring(count + 1);
        // 检查文件类型是否正确
        if(!set.contains(endName)){
            throw new MyException(Constants.CODE_600, "只能上传MP4格式的文件");

        }

        File folder = new File(videoPath);

        // 检查文件夹是否存在，不存在则创建文件夹
        if(!folder.exists()){
            folder.mkdirs();
        }

        String saveName = DigestUtil.md5Hex(originName)+".mp4";

        try {
            // 保存到本地
            file.transferTo(new File(folder,saveName));

        } catch (IOException e) {
            throw new MyException(Constants.CODE_401, "文件保存本地失败");
        }

        return saveName;

    }
    // 关键帧提取
    private void keyFrame(String saveName)  {

        String[] split = saveName.split("\\.");

        String inPath = videoPath+"\\"+saveName;

        File file = new File(inPath);

        String outPath = "E:\\Video\\src\\main\\resources\\static\\image"+"\\"+split[0];

        File folder = new File(outPath);

        // 检查文件夹是否存在，不存在则创建文件夹
        if(!folder.exists()){
            folder.mkdirs();
        }


        try {
            int res = video_process(file, outPath);

            if(res < 0)
                throw new MyException(Constants.CODE_403,"关键帧提取异常");

        } catch (Exception e) {
            throw new MyException(Constants.CODE_402,"文件格式转化错误");
        }


    }

}




