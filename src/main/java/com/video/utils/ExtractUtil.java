package com.video.utils;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @Author shawni
 * @Description TODO
 * @Date 2023/11/15 0:42
 * @Version 1.0
 */
public class ExtractUtil {

    public static int video_process(File file, String outputPath) {

        int frameInterval = 20; // 每隔20帧提取一个关键帧

        try {
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file);
            grabber.start();
            //视频总帧数
            int frameCount = grabber.getLengthInFrames();
            //System.out.println(frameCount);
            int frameRate = (int) grabber.getFrameRate();
            //提取图片
            for (int i = 0; i < frameCount; i += frameInterval) {
                Frame frame = grabber.grabImage();
                BufferedImage image = Java2DFrameUtils.toBufferedImage(frame);

                String outputFilePath = outputPath + "\\frame"+"_" + i + ".jpg";
                ImageIO.write(image, "jpg", new File(outputFilePath));
            }

            grabber.stop();
            return frameCount;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
